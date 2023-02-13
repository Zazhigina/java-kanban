package ru.yandex.server;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ru.yandex.manager.task.FileBackedTasksManager;


import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HttpTaskManager extends FileBackedTasksManager {


    private final HttpClient kvServerClient = HttpClient.newHttpClient();
    private String API_TOKEN;

    private String url;

    public HttpTaskManager(String path) {
        super(path);
        this.url = path;
        URI uri = URI.create(url + "/register");

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .header("Content-type", "application/json")
                .uri(uri)
                .build();

        try {
            HttpResponse<String> apiToken = kvServerClient.send(request,  HttpResponse.BodyHandlers.ofString());
            this.API_TOKEN = apiToken.body();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    void put(String key, String json){

        URI tasksUri = URI.create("http://localhost:8078/save/"+ key + "?API_TOKEN=" + API_TOKEN);

        HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json)) // тело запроса - все задачи в формате json: "[{"id":1}, {"id":2}]"
                .uri(tasksUri)
                .build();
    }

    public String load (String key) throws IOException, InterruptedException {
        URI tasksUri = URI.create("http://localhost:8078/load/"+ key + "?API_TOKEN=" + API_TOKEN);
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .header("Content-type", "application/json")
                .uri(tasksUri)
                .build();

        return kvServerClient.send(request,  HttpResponse.BodyHandlers.ofString()).body();
    }

    @Override
    public void save() throws IOException{
        URI tasksUri = URI.create("http://localhost:8078/save/tasks?API_TOKEN=" + API_TOKEN);
        URI epicUri = URI.create("http://localhost:8078/save/epics?API_TOKEN=" + API_TOKEN);
        URI subtasksUri = URI.create("http://localhost:8078/save/subtasks?API_TOKEN=" + API_TOKEN);

        URI historyUri = URI.create("http://localhost:8078/save/history?API_TOKEN=" + API_TOKEN);

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter());
        Gson gson = gsonBuilder.create();

        try {
            HttpRequest requestTask = HttpRequest.newBuilder()
                    .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(tasks))) // тело запроса - все задачи в формате json: "[{"id":1}, {"id":2}]"
                    .uri(tasksUri)
                    .build();
            kvServerClient.send(requestTask, HttpResponse.BodyHandlers.ofString());

            HttpRequest requestEpic = HttpRequest.newBuilder()
                    .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(epics))) // тело запроса - все задачи в формате json: "[{"id":1}, {"id":2}]"
                    .uri(epicUri)
                    .build();
            kvServerClient.send(requestEpic, HttpResponse.BodyHandlers.ofString());

            HttpRequest requestSubtask = HttpRequest.newBuilder()
                    .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(subtasks))) // тело запроса - все задачи в формате json: "[{"id":1}, {"id":2}]"
                    .uri(subtasksUri)
                    .build();
            kvServerClient.send(requestSubtask, HttpResponse.BodyHandlers.ofString());

            HttpRequest requestHistory = HttpRequest.newBuilder()
                    .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(getHistory().toString()))) // тело запроса - все задачи в формате json: "[{"id":1}, {"id":2}]"
                    .uri(historyUri)
                    .build();
            kvServerClient.send(requestHistory, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new IOException("Не могу получить данные от kvserver");
        }
    }

}
