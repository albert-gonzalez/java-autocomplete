package games.application.handler.autoCompleteSearch;

public class AutoCompleteSearchResponse {
    private Object games;

    AutoCompleteSearchResponse(Object games) {
        this.games = games;
    }

    public Object getGames() {
        return games;
    }
}
