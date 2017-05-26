package games.application.handler.copyGamesFromRepository;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import games.domain.repositories.GameRepository;

import java.util.concurrent.CompletionStage;

public class CopyGamesFromRepositoryCommandHandler {

    @Inject
    @Named("game.jpa")
    private GameRepository destGameRepository;

    @Inject @Named("game.json")
    private GameRepository sourceGameRepository;

    @Inject
    public CopyGamesFromRepositoryCommandHandler(GameRepository gameRepository, GameRepository jsonGameRepository) {
        this.destGameRepository = gameRepository;
        this.sourceGameRepository = jsonGameRepository;
    }

    public CompletionStage<CopyGamesFromRepositoryResponse> handle() {
        return this.sourceGameRepository
                .findAll()
                .thenComposeAsync((gamesList) -> destGameRepository.add(gamesList))
                .thenApplyAsync((gameList) -> new CopyGamesFromRepositoryResponse(gameList.count()));
    }
}
