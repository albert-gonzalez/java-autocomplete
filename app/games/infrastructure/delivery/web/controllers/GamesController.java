package games.infrastructure.delivery.web.controllers;

import com.google.inject.Inject;
import games.application.handler.autoCompleteSearch.AutoCompleteSearchCommand;
import games.application.handler.autoCompleteSearch.AutoCompleteSearchCommandHandler;
import games.application.handler.copyGamesFromRepository.CopyGamesFromRepositoryCommandHandler;
import games.application.handler.updateGamesIndex.UpdateGamesIndexCommandHandler;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.concurrent.CompletionStage;

public class GamesController extends Controller {

    public Result index() {
        return ok(games.infrastructure.ui.views.html.templates.index.render());
    }
}
