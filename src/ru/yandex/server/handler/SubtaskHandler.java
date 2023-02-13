package ru.yandex.server.handler;

import com.sun.net.httpserver.HttpExchange;
import ru.yandex.entites.Subtask;
import ru.yandex.manager.task.TaskManager;
import ru.yandex.server.Endpoint;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class SubtaskHandler extends TaskHandler {
    @Override
    protected Optional<Integer> getTaskId(HttpExchange exchange) {
        return super.getTaskId(exchange);
    }

    @Override
    protected Map<String, String> queryToMap(String query) {
        return super.queryToMap(query);
    }

    @Override
    protected Endpoint getEndpoint(String requestPath, String requestMethod) {
        return super.getEndpoint(requestPath, requestMethod);
    }

    @Override
    protected void writeResponse(HttpExchange exchange, String responseString, int responseCode) throws IOException {
        super.writeResponse(exchange, responseString, responseCode);
    }

    public SubtaskHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Endpoint endpoint = getEndpoint(exchange.getRequestURI().toString(), exchange.getRequestMethod());
        switch (endpoint) {
            case GET_TASK: {
                try {
                    HashMap<Integer, Subtask> epics = taskManager.getSubtasks();
                    writeResponse(exchange, gson.toJson(epics), 200);
                } catch (Exception ex) {
                    Integer epicws = 3;
                }
                break;
            }
            case GET_TASKID: {
                Optional<Integer> taskIdOpt = getTaskId(exchange);
                if (taskIdOpt.isEmpty()) {
                    writeResponse(exchange, "Некорректный идентификатор задачи", 400);
                    return;
                }
                int subtaskId = taskIdOpt.get();
                String subtaskJson = gson.toJson(taskManager.getSubtaskById(subtaskId));
                writeResponse(exchange, subtaskJson, 200);
                break;
            }
            case GET_EPIC_BY_SUBTASK:{
                Optional<Integer> taskIdOpt = getTaskId(exchange);
                if (taskIdOpt.isEmpty()) {
                    writeResponse(exchange, "Некорректный идентификатор задачи", 400);
                    return;
                }
                int taskId = taskIdOpt.get();
                if (!taskManager.getSubtasks().containsKey(taskId)){
                    writeResponse(exchange, "С таким идентификатором нет подзадачи", 400);
                }
                String taskJson = gson.toJson(taskManager.getEpicBySubtask(taskId));
                writeResponse(exchange, taskJson, 200);
                break;
            }
            case POST_TASK: {
                InputStream in = exchange.getRequestBody();
                String t = new String(in.readAllBytes(), DEFAULT_CHARSET);
                Subtask subtask = gson.fromJson(t, Subtask.class);
                if (taskManager.getSubtasks().containsKey(subtask.getId())) {
                    try {
                        taskManager.updateSubtask(subtask);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    writeResponse(exchange, "Задача обновлена", 201);
                    return;
                }
                writeResponse(exchange, "Задача " + gson.toJson(subtask) + " не найдена", 404);
                break;
            }
            case DELETE_TASK: {
                InputStream in = exchange.getRequestBody();
                String t = new String(in.readAllBytes(), DEFAULT_CHARSET);
                Subtask subtask = gson.fromJson(t, Subtask.class);
                try {
                    taskManager.removeSubtask(subtask);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                if (!taskManager.getSubtasks().containsValue(subtask)) {
                    writeResponse(exchange, "Задачa " + gson.toJson(subtask) + " больше нету", 201);
                    return;
                }
                writeResponse(exchange, "Ошибка при удалений", 404);
            }
            case DELETE_ALL:
                taskManager.removeAllSubtasks();
                if (taskManager.getSubtasks().isEmpty()) {
                    writeResponse(exchange, "Задачи удалены", 201);
                    return;
                }
                writeResponse(exchange, "Ошибка при удалений", 404);
            default:
                writeResponse(exchange, "Такого эндпоинта не существует", 404);
        }
    }

}
