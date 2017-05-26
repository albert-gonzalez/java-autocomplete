package games.infrastructure.persistence.jpa;

import com.google.inject.Inject;
import games.domain.models.Game;
import games.infrastructure.persistence.DatabaseExecutionContext;
import play.db.jpa.JPAApi;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.concurrent.CompletableFuture.supplyAsync;

public class GameRepository implements games.domain.repositories.GameRepository {

    private final JPAApi jpaApi;
    private final DatabaseExecutionContext executionContext;

    @Inject
    public GameRepository(JPAApi jpaApi, DatabaseExecutionContext executionContext) {
        this.jpaApi = jpaApi;
        this.executionContext = executionContext;
    }

    @Override
    public CompletionStage<Game> add(Game game) {
        return supplyAsync(() -> wrap(em -> insert(em, game)), executionContext);
    }

    @Override
    public CompletionStage<Stream<Game>> add(Stream<Game> games) {
        return supplyAsync(() -> wrapList(em -> insert(em, games)), executionContext);
    }

    @Override
    public CompletionStage<Stream<Game>> findAll() {
        return supplyAsync(() -> wrap(this::list), executionContext);
    }

    private <T> T wrap(Function<EntityManager, T> function) {
        return jpaApi.withTransaction(function);
    }

    private <K> Stream<K> wrapList(Function<EntityManager, Stream<K>> function) {
        return jpaApi.withTransaction(function);
    }

    private Game insert(EntityManager em, Game game) {
        em.persist(game);

        return game;
    }

    private Stream<Game> insert(EntityManager em, Stream<Game> games) {
        List<Game> addedGames = new ArrayList<>();

        games.forEach((game) -> {
            em.persist(game);
            addedGames.add(game);
        });

        return addedGames.stream();
    }

    private Stream<Game> list(EntityManager em) {
        return em.createQuery("select g from Game g", Game.class).getResultList().stream();
    }
}
