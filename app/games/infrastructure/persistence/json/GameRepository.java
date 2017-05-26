package games.infrastructure.persistence.json;


import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import games.domain.models.Game;
import games.infrastructure.persistence.DatabaseExecutionContext;
import play.Environment;
import play.libs.Json;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.stream.Stream;

import static java.util.concurrent.CompletableFuture.supplyAsync;

public class GameRepository implements games.domain.repositories.GameRepository {

    private final DatabaseExecutionContext executionContext;

    private final Environment env;

    @Inject
    public GameRepository(DatabaseExecutionContext executionContext, Environment env) {
        this.executionContext = executionContext;
        this.env = env;
    }

    @Override
    public CompletionStage<Stream<Game>> findAll() {
        return supplyAsync(this::list, executionContext);
    }

    private Stream<Game> list() {
        InputStream is = env.resourceAsStream("data/games.json");
        final List<Game> gamesList = new ArrayList<>();
        final JsonNode json = Json.parse(is);
        for (JsonNode jsonNode : json) {
            gamesList.add(
                new Game(jsonNode.get("name").toString(), jsonNode.get("description").toString(), jsonNode.get("site_detail_url").toString()));
        }

        return gamesList.stream();
    }
}
