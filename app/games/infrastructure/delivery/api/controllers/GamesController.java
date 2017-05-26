package games.infrastructure.delivery.api.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import games.application.handler.autoCompleteSearch.AutoCompleteSearchCommand;
import games.application.handler.autoCompleteSearch.AutoCompleteSearchCommandHandler;
import games.application.handler.copyGamesFromRepository.CopyGamesFromRepositoryCommandHandler;
import games.application.handler.updateGamesIndex.UpdateGamesIndexCommandHandler;
import play.libs.Json;
import play.mvc.*;

import java.io.IOException;
import java.util.concurrent.CompletionStage;

public class GamesController extends Controller {

    private CopyGamesFromRepositoryCommandHandler copyGamesFromRepositoryCommandHandler;

    private UpdateGamesIndexCommandHandler updateGamesIndexCommandHandler;

    private AutoCompleteSearchCommandHandler autoCompleteSearchCommandHandler;

    @Inject
    public GamesController(CopyGamesFromRepositoryCommandHandler copyGamesFromRepositoryCommandHandler,
                           UpdateGamesIndexCommandHandler updateGamesIndexCommandHandler,
                           AutoCompleteSearchCommandHandler autoCompleteSearchCommandHandler) {
        this.copyGamesFromRepositoryCommandHandler = copyGamesFromRepositoryCommandHandler;
        this.updateGamesIndexCommandHandler = updateGamesIndexCommandHandler;
        this.autoCompleteSearchCommandHandler = autoCompleteSearchCommandHandler;
    }

    public CompletionStage<Result> loadFromJson() {
        return copyGamesFromRepositoryCommandHandler
                .handle()
                .thenApplyAsync((response) -> ok(Json.toJson(response)));
    }

    public CompletionStage<Result> updateESIndex() {
        return updateGamesIndexCommandHandler.handle()
                .thenApplyAsync((count) -> ok(Json.toJson(count)));
    }

    public CompletionStage<Result> autoCompleteSearch(String query) {
        return autoCompleteSearchCommandHandler
                .handle(new AutoCompleteSearchCommand(query))
                .thenApplyAsync(response -> ok(Json.toJson(response)));
    }
}
