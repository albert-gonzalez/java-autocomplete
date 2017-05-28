package games.domain.services;

import games.domain.models.Game;
import games.domain.repositories.GameRepository;

import java.util.concurrent.CompletionStage;
import java.util.stream.Stream;

public class CopyGamesService {
    public CompletionStage<Stream<Game>> copy(GameRepository source, GameRepository dest) {
        return source.findAll().thenComposeAsync(dest::add);
    }
}
