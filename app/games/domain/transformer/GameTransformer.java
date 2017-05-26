package games.domain.transformer;

import com.google.inject.ImplementedBy;
import games.domain.models.Game;

import java.util.stream.Stream;

@ImplementedBy(games.infrastructure.transformer.json.GameTransformer.class)
public interface GameTransformer {
    Object transform(Game game);

    Object transform(Stream<Game> games);
}