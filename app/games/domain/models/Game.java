package games.domain.models;

import com.fasterxml.jackson.annotation.JsonGetter;
import jdk.nashorn.internal.objects.annotations.Getter;

import javax.persistence.*;

@Entity
public class Game {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String url;

    private String description;

    public Game() {

    }

    public Game(String name, String description, String url) {
        this.name = name;
        this.description = description;
        this.url = url;
    }

    public Game(long id, String name, String description, String url) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.url = url;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public String getDescription() {
        return description;
    }
}
