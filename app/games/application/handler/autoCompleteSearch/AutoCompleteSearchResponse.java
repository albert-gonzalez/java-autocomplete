package games.application.handler.autoCompleteSearch;

import games.domain.models.Game;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AutoCompleteSearchResponse {
    private Object games;

    public AutoCompleteSearchResponse(Object games) {
        this.games = games;
    }

    public Object getGames() {
        return games;
    }
}
