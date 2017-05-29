package games.application.handler.updateGamesIndex;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import games.domain.repositories.GameRepository;
import games.domain.services.CopyGamesService;

import java.util.concurrent.CompletionStage;

public class UpdateGamesIndexCommandHandler {

    @Inject
    @Named("game.elasticsearch")
    private GameRepository destGameRepository;

    @Inject @Named("game.jpa")
    private GameRepository sourceGameRepository;

    @Inject
    private CopyGamesService copyGamesService;

    @Inject
    public UpdateGamesIndexCommandHandler(GameRepository sourceGameRepository,
                                          GameRepository destGameRepository,
                                          CopyGamesService copyGamesService
    ) {
        this.destGameRepository = destGameRepository;
        this.sourceGameRepository = sourceGameRepository;
        this.copyGamesService = copyGamesService;
    }

    public CompletionStage<UpdateGamesIndexResponse> handle() {
        return this.copyGamesService.copy(sourceGameRepository, destGameRepository)
                .thenApplyAsync((addedGames) -> new UpdateGamesIndexResponse(addedGames.count()));
    }
}
