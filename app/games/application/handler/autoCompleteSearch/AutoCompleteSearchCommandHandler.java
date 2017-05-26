package games.application.handler.autoCompleteSearch;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import games.domain.repositories.GameRepository;
import games.domain.transformer.GameTransformer;

import java.util.concurrent.CompletionStage;

public class AutoCompleteSearchCommandHandler {
    @Inject @Named("game.elasticsearch")
    private GameRepository fullTextGameRepository;

    private GameTransformer gameTransformer;

    @Inject
    public AutoCompleteSearchCommandHandler(GameRepository fullTextGameRepository,
                                            GameTransformer gameTransformer) {
        this.fullTextGameRepository = fullTextGameRepository;
        this.gameTransformer = gameTransformer;
    }

    public CompletionStage<AutoCompleteSearchResponse> handle(AutoCompleteSearchCommand command) {
        return fullTextGameRepository.autoCompleteSearch(command.query())
                .thenApplyAsync(games -> new AutoCompleteSearchResponse(
                        gameTransformer.transform(games))
                );
    }
}
