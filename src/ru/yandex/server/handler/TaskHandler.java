package ru.yandex.server.handler;

import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.yandex.entites.Task;

import java.io.IOException;

import com.google.gson.Gson;
import ru.yandex.manager.task.TaskManager;
import ru.yandex.server.Endpoint;
import ru.yandex.server.LocalDateTypeAdapter;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.*;

public class TaskHandler implements HttpHandler {
    protected TaskManager taskManager;
    protected Gson gson;
    protected static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    public TaskHandler(TaskManager taskManager) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter());
        gson = gsonBuilder.create();
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Endpoint endpoint = getEndpoint(exchange.getRequestURI().toString(), exchange.getRequestMethod());
        switch (endpoint) {
            case GET_TASK: {
                HashMap<Integer, Task> tasks = taskManager.getTasks();
                writeResponse(exchange, gson.toJson(tasks), 200);
                break;
            }
            case GET_TASKID: {
                Optional<Integer> taskIdOpt = getTaskId(exchange);
                if (taskIdOpt.isEmpty()) {
                    writeResponse(exchange, "Некорректный идентификатор задачи", 400);
                    return;
                }
                int taskId = taskIdOpt.get();
                String taskJson = gson.toJson(taskManager.getTaskById(taskId));
                writeResponse(exchange, taskJson, 200);
                break;
            }
            case GET_HISTORY:{
                ArrayList<Task> getHistory = taskManager.getHistory();
                if (getHistory.isEmpty()){
                    writeResponse(exchange, "Пустая история задач", 204);
                    return;
                }
                writeResponse(exchange, gson.toJson(getHistory), 200);
                break;
            }
            case GET_PRIORITIZED:{
                List<Task> getPrioritizedTasks = taskManager.getPrioritizedTasks();
                if (getPrioritizedTasks.isEmpty()){
                    writeResponse(exchange, "Список пустой", 204);
                    return;
                }
                writeResponse(exchange, gson.toJson(getPrioritizedTasks), 200);
                break;
            }
            case POST_TASK: {
                InputStream in = exchange.getRequestBody();
                String body = new String(in.readAllBytes(), DEFAULT_CHARSET);
                Task task = gson.fromJson(body, Task.class);
                if (taskManager.getTasks().containsKey(task.getId())) {
                    taskManager.updateTask(task);
                    writeResponse(exchange, "Задача обновлена", 201);
                    return;
                }
                writeResponse(exchange, "Задача " + task.toString() + " не найдена", 404);
                break;
            }
            case DELETE_TASK: {
                InputStream in = exchange.getRequestBody();
                String t = new String(in.readAllBytes(), DEFAULT_CHARSET);
                Task task = gson.fromJson(t, Task.class);
                taskManager.removeTask(task);
                if (!taskManager.getTasks().containsValue(task)) {
                    writeResponse(exchange, "Задачa " + gson.toJson(task) + " больше нету", 201);
                    return;
                }
                writeResponse(exchange, "Ошибка при удалений", 404);
            }
            case DELETE_ALL:
                taskManager.removeAllTasks();
                if (taskManager.getTasks().isEmpty()) {
                    writeResponse(exchange, "Задачи удалены", 201);
                    return;
                }
                writeResponse(exchange, "Ошибка при удалений", 404);
            default:
                writeResponse(exchange, "Такого эндпоинта не существует", 404);
        }
    }

    protected Optional<Integer> getTaskId(HttpExchange exchange) {
        Map<String, String> pathParams = this.queryToMap(exchange.getRequestURI().getRawQuery());
        try {
            return Optional.of(Integer.parseInt(pathParams.get("id")));
        } catch (NumberFormatException exception) {
            return Optional.empty();
        }
    }

    protected Map<String, String> queryToMap(String query) {
        if(query == null) {
            return null;
        }
        Map<String, String> result = new HashMap<>();
        for (String param : query.split("&")) {
            String[] entry = param.split("=");
            if (entry.length > 1) {
                result.put(entry[0], entry[1]);
            }else{
                result.put(entry[0], "");
            }
        }
        return result;
    }

    protected Endpoint getEndpoint(String requestPath,
                                   String requestMethod) {
        String[] pathParts = requestPath.split("/");
        if (pathParts[2].equals("")) {
            return Endpoint.GET_PRIORITIZED;
        }
        if (pathParts.length == 3) {
            if (requestMethod.equals("GET")) {
                if (!pathParts[2].equals("history")) {
                    return Endpoint.GET_TASK;
                }else {
                    return Endpoint.GET_HISTORY;
                }
            }
            if (requestMethod.equals("DELETE")) {
                return Endpoint.DELETE_ALL;
            }
            if (requestMethod.equals("POST")) {
                return Endpoint.POST_TASK;
            }
        }
        if (pathParts.length == 4) {
            if (requestMethod.equals("GET")) {
                return Endpoint.GET_TASKID;
            }
            if (requestMethod.equals("DELETE")) {
                return Endpoint.DELETE_TASK;
            }
            if (requestMethod.equals("POST")) {
                return Endpoint.POST_TASK;
            }
        }
        if(pathParts.length == 5){
            return Endpoint.GET_EPIC_BY_SUBTASK;
        }
        return Endpoint.UNKNOWN;
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
