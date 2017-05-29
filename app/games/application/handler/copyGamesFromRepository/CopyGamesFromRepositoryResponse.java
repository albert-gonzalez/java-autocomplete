package games.application.handler.copyGamesFromRepository;

public class CopyGamesFromRepositoryResponse {
    private Long count;

    CopyGamesFromRepositoryResponse(Long count) {
        this.count = count;
    }

    public Long getCount() {
        return count;
    }
}
