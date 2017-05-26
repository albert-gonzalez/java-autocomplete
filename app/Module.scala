import com.google.inject.AbstractModule
import com.google.inject.name.Names
import games.domain.repositories.GameRepository

class Module extends AbstractModule {
  def configure() = {

    bind(classOf[GameRepository])
      .annotatedWith(Names.named("game.jpa"))
      .to(classOf[games.infrastructure.persistence.jpa.GameRepository])

    bind(classOf[GameRepository])
      .annotatedWith(Names.named("game.json"))
      .to(classOf[games.infrastructure.persistence.json.GameRepository])

    bind(classOf[GameRepository])
      .annotatedWith(Names.named("game.elasticsearch"))
      .to(classOf[games.infrastructure.persistence.elasticsearch.GameRepository])
  }
}