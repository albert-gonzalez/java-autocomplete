package games.application.handler.copyGamesFromRepository;

import games.domain.models.Game;
import games.domain.repositories.GameRepository;
import games.domain.services.CopyGamesService;
import org.junit.Test;

import java.util.concurrent.CompletionStage;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Stream;

import static java.util.concurrent.CompletableFuture.supplyAsync;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CopyGamesFromRepositoryCommandHandlerTest {

    @Test
    public void itShouldCopyGamesFromARepositoryToAnother() throws InterruptedException {
        Stream<Game> gameList = createGameList();
        GameRepository sourceGameRepository = mock(GameRepository.class);
        when(sourceGameRepository.findAll()).thenReturn(supplyAsync(() -> gameList));

        GameRepository destGameRepository = mock(GameRepository.class);
        when(destGameRepository.add(gameList)).thenReturn(supplyAsync(() -> gameList));

        CopyGamesFromRepositoryCommandHandler handler = new CopyGamesFromRepositoryCommandHandler(
                sourceGameRepository,
                destGameRepository,
                new CopyGamesService()
        );

        CountDownLatch latch = new CountDownLatch(1);

        CompletionStage<CopyGamesFromRepositoryResponse> handlerResponse = handler.handle();
        handlerResponse.thenRun(latch::countDown);

        latch.await();

        assertThat(handlerResponse.toCompletableFuture())
            .isCompletedWithValueMatching((response) -> response.getCount() == 3,"Should return copied games count");
    }

    private Stream<Game> createGameList() {
        return Stream.of(
                new Game("game 1", "description 1", "url 1"),
                new Game("game 2", "description 2", "url 2"),
                new Game("game 3", "description 3", "url 3")
        );
    }
}