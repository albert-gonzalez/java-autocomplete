package games.domain.repositories;

import com.google.inject.ImplementedBy;
import games.domain.models.Game;

import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.stream.Stream;

@ImplementedBy(games.infrastructure.persistence.jpa.GameRepository.class)
public interface GameRepository {
    default CompletionStage<Game> add(Game game) {
        throw new RuntimeException("Not Implemented");
    }

    default CompletionStage<Stream<Game>> add(Stream<Game> games) {
        throw new RuntimeException("Not Implemented");
    }

    default CompletionStage<Stream<Game>> findAll() {
        throw new RuntimeException("Not Implemented");
    }

    default CompletionStage<Stream<Game>> autoCompleteSearch(String query) {
        throw new RuntimeException("Not Implemented");
    }
}
