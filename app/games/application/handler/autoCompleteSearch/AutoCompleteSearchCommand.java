package games.application.handler.autoCompleteSearch;

public class AutoCompleteSearchCommand {
    private String query;

    public AutoCompleteSearchCommand(String query) {
        this.query = query;
    }

    String query() {
        return query;
    }
}
