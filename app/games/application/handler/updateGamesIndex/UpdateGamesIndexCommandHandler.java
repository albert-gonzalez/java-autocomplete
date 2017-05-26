package games.application.handler.updateGamesIndex;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import games.domain.repositories.GameRepository;

import java.util.concurrent.CompletionStage;

public class UpdateGamesIndexCommandHandler {

    @Inject
    @Named("game.elasticsearch")
    private GameRepository destGameRepository;

    @Inject @Named("game.jpa")
    private GameRepository sourceGameRepository;

    @Inject
    public UpdateGamesIndexCommandHandler(GameRepository gameRepository, GameRepository jsonGameRepository) {
        this.destGameRepository = gameRepository;
        this.sourceGameRepository = jsonGameRepository;
    }

    public CompletionStage<UpdateGamesIndexResponse> handle() {
        return this.sourceGameRepository
                .findAll()
                .thenComposeAsync((gameList) -> destGameRepository.add(gameList))
                .thenApplyAsync((addedGames) -> new UpdateGamesIndexResponse(addedGames.count()));
    }
}
