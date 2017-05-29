package games.application.handler.updateGamesIndex;

public class UpdateGamesIndexResponse {
    private Long count;

    UpdateGamesIndexResponse(Long count) {
        this.count = count;
    }

    public Long getCount() {
        return count;
    }
}
