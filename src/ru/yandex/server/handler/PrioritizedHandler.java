package ru.yandex.server.handler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.yandex.entites.Task;
import ru.yandex.manager.task.TaskManager;
import ru.yandex.server.LocalDateTypeAdapter;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;

public class PrioritizedHandler implements HttpHandler {
    protected TaskManager taskManager;
    protected Gson gson;
    protected static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    public PrioritizedHandler(TaskManager taskManager) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter());
        gson = gsonBuilder.create();
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (exchange.getRequestMethod().equals("GET")) {
            List<Task> getPrioritizedTasks = taskManager.getPrioritizedTasks();
            writeResponse(exchange, gson.toJson(getPrioritizedTasks), 200);
        }
    }


    protected void writeResponse(HttpExchange exchange,
                                 String responseString,
                                 int responseCode) throws IOException {
        if (responseString.isBlank()) {
            exchange.sendResponseHeaders(responseCode, 0);
        } else {
            byte[] bytes = responseString.getBytes(DEFAULT_CHARSET);
            exchange.sendResponseHeaders(responseCode, bytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(bytes);
            }
        }
        exchange.close();
    }
}
