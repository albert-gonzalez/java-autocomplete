package games.application.handler.autoCompleteSearch;

import com.fasterxml.jackson.databind.JsonNode;
import games.application.handler.autoCompleteSearch.AutoCompleteSearchCommand;
import games.application.handler.autoCompleteSearch.AutoCompleteSearchCommandHandler;
import games.application.handler.autoCompleteSearch.AutoCompleteSearchResponse;
import games.domain.models.Game;
import games.domain.repositories.GameRepository;
import games.domain.transformer.GameTransformer;
import org.junit.Test;

import java.util.concurrent.CompletionStage;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Stream;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;
import static java.util.concurrent.CompletableFuture.supplyAsync;

public class AutoCompleteSearchCommandHandlerTest {
    final String QUERY = "the query";

    @Test
    public void itShouldReturnAListOfGames() throws InterruptedException {
        Stream<Game> gameList = createGameList();
        GameRepository gameRepository = mock(GameRepository.class);
        when(gameRepository.autoCompleteSearch(QUERY)).thenReturn(supplyAsync(() -> gameList));

        GameTransformer transformer = new games.infrastructure.transformer.json.GameTransformer();
        AutoCompleteSearchCommandHandler handler = new AutoCompleteSearchCommandHandler(
                gameRepository,
                transformer
        );

        CountDownLatch latch = new CountDownLatch(1);

        CompletionStage<AutoCompleteSearchResponse> handlerResponse = handler.handle(new AutoCompleteSearchCommand(QUERY));
        handlerResponse.thenRun(latch::countDown);

        latch.await();

        assertThat(handlerResponse.toCompletableFuture())
            .isCompletedWithValueMatching((response) -> response.getGames() instanceof JsonNode,"Should return JsonNode games")
                .isCompletedWithValueMatching((response ) -> checkJsonGame(response, 0), "Should return correct first JSON games")
                .isCompletedWithValueMatching((response ) -> checkJsonGame(response, 1), "Should return correct second JSON games")
                .isCompletedWithValueMatching((response ) -> checkJsonGame(response, 2), "Should return correct third JSON games");
    }

    private boolean checkJsonGame(AutoCompleteSearchResponse response, int index) {
        JsonNode games = (JsonNode) response.getGames();
        String name = String.format("game %s", index + 1);
        String description = String.format("description %s", index + 1);
        String url = String.format("url %s", index + 1);

        return name.equals(games.get(index).path("name").asText())
                && description.equals(games.get(index).path("description").asText())
                && url.equals(games.get(index).path("url").asText());
    }

    private Stream<Game> createGameList() {
        return Stream.of(
                new Game("game 1", "description 1", "url 1"),
                new Game("game 2", "description 2", "url 2"),
                new Game("game 3", "description 3", "url 3")
        );
    }
}