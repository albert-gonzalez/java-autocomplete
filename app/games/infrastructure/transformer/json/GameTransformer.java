package games.infrastructure.transformer.json;

import com.fasterxml.jackson.databind.JsonNode;
import games.domain.models.Game;
import play.libs.Json;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GameTransformer implements games.domain.transformer.GameTransformer {

    @Override
    public JsonNode transform(Game game) {
        return Json.toJson(game);
    }

    @Override
    public JsonNode transform(Stream<Game> games) {
        return Json.toJson(games.collect(Collectors.toList()));
    }
}
