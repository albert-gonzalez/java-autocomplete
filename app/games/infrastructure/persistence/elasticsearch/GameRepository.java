package games.infrastructure.persistence.elasticsearch;


import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import games.domain.models.Game;
import games.infrastructure.persistence.DatabaseExecutionContext;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import play.Configuration;
import play.libs.Json;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.stream.Stream;

import static java.util.concurrent.CompletableFuture.supplyAsync;

public class GameRepository implements games.domain.repositories.GameRepository {

    private final DatabaseExecutionContext executionContext;

    private final RestClient restClient;


    @Inject
    public GameRepository(
            DatabaseExecutionContext executionContext,
            Configuration configuration
    ) {
        this.executionContext = executionContext;

        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY,
                new UsernamePasswordCredentials("elastic", "changeme"));

        this.restClient = RestClient.builder(new HttpHost(configuration.getString("elastic_host"), 9200))
                .setHttpClientConfigCallback(new RestClientBuilder.HttpClientConfigCallback() {
                    @Override
                    public HttpAsyncClientBuilder customizeHttpClient(HttpAsyncClientBuilder httpClientBuilder) {
                        return httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
                    }
                })
                .build();
    }

    @Override
    public CompletionStage<Stream<Game>> add(Stream<Game> games) {
        return supplyAsync(() -> index(games));
    }

    @Override
    public CompletionStage<Stream<Game>> findAll() {
        return supplyAsync(this::list, executionContext);
    }

    @Override
    public CompletionStage<Stream<Game>> autoCompleteSearch(String query) {
        return supplyAsync(() -> {
            try {
                return list(URLEncoder.encode(query, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("Invalid query");
            }
        }, executionContext);
    }

    private Stream<Game> index(Stream<Game> games) {
        List<Game> addedGames= new ArrayList<>();
        games.forEach((game) -> {
            HttpEntity entity = new NStringEntity(
                    "{\n" +
                            "    \"name\" : \"" + StringEscapeUtils.escapeJson(game.getName()) + "\",\n" +
                            "    \"description\" : \"" + StringEscapeUtils.escapeJson(game.getDescription()) + "\",\n" +
                            "    \"url\" : \"" + StringEscapeUtils.escapeJson(game.getUrl()) + "\"\n" +
                            "}", ContentType.APPLICATION_JSON);

            try {
                restClient.performRequest(
                        "PUT",
                        "/games/game/" + game.getId(),
                        Collections.<String, String>emptyMap(),
                        entity);
                addedGames.add(game);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        return addedGames.stream();
    }

    private Stream<Game> list() {
        return list(null);
    }

    private Stream<Game> list(String query) {
        final Response indexResponse;
        try {
            indexResponse = restClient.performRequest(
                    "GET",
                    "/games/_search/" + (query != null? "?q=" + query : ""),
                    Collections.<String, String>emptyMap());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return transformResponseToGames(indexResponse);
    }

    private Stream<Game> transformResponseToGames(Response response) {
        try {
            JsonNode jsonGames = Json.parse(EntityUtils.toString(response.getEntity()));
            List<Game> games = new ArrayList<>();
            jsonGames.findPath("hits").findPath("hits").elements().forEachRemaining((gameNode) -> {
                games.add(transformJsonNodeToGame(gameNode));
            });

            return games.stream();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Game transformJsonNodeToGame(JsonNode gameNode) {
        JsonNode source = gameNode.path("_source");
        return new Game(
                Integer.parseInt(gameNode.path("_id").textValue()),
                source.path("name").textValue(),
                source.path("description").textValue(),
                source.path("url").textValue()
        );
    }
}
