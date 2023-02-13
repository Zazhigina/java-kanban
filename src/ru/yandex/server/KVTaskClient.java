package ru.yandex.server;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;



public class KVTaskClient {

    private final HttpClient kvServerClient = HttpClient.newHttpClient();
    private final String API_TOKEN;

    public KVTaskClient(String url) throws IOException, InterruptedException {
        URI uri = URI.create(url + "/register");

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .header("Content-type", "application/json")
                .uri(uri)
                .build();

        HttpResponse<String> apiToken = kvServerClient.send(request,  HttpResponse.BodyHandlers.ofString());
        API_TOKEN = apiToken.body();
    }
    void put(String key, String json){

        URI tasksUri = URI.create("http://localhost:8078/save/"+ key + "?API_TOKEN=" + API_TOKEN);

        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json)) // тело запроса - все задачи в формате json: "[{"id":1}, {"id":2}]"
                .uri(tasksUri)
                .build();
    }

    String load(String key) throws IOException, InterruptedException {
        URI tasksUri = URI.create("http://localhost:8078/load/"+ key + "?API_TOKEN=" + API_TOKEN);
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .header("Content-type", "application/json")
                .uri(tasksUri)
                .build();

        return kvServerClient.send(request,  HttpResponse.BodyHandlers.ofString()).body();
    }
}
