package ru.yandex.server.handler;

import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import ru.yandex.entites.Epic;
import ru.yandex.manager.task.TaskManager;
import ru.yandex.server.Endpoint;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class EpicHandler extends TaskHandler {
    public EpicHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Endpoint endpoint = getEndpoint(exchange.getRequestURI().toString(), exchange.getRequestMethod());
        switch (endpoint) {
            case GET_TASK: {
                try {
                    HashMap<Integer, Epic> epics = taskManager.getEpics();
                    writeResponse(exchange, gson.toJson(epics), 200);
                }catch (Exception ex) {
                    break;
                }
                break;
            }
            case GET_TASKID: {
                Optional<Integer> taskIdOpt = getTaskId(exchange);
                if (taskIdOpt.isEmpty()) {
                    writeResponse(exchange, "Некорректный идентификатор задачи", 400);
                    return;
                }
                int epicId = taskIdOpt.get();
                String epicJson = gson.toJson(taskManager.getEpicById(epicId));
                writeResponse(exchange, epicJson, 200);
                break;
            }
            case POST_TASK: {
                InputStream in = exchange.getRequestBody();
                String t = new String(in.readAllBytes(), DEFAULT_CHARSET);
                Epic epic = gson.fromJson(t, Epic.class);
                if (taskManager.getEpics().containsKey(epic.getId())) {
                    try {
                        taskManager.updateEpic(epic);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    writeResponse(exchange, "Задача обновлена", 201);
                    return;
                }
                writeResponse(exchange, "Задача " + gson.toJson(epic) + " не найдена", 404);
                break;
            }
            case DELETE_TASK: {
                InputStream in = exchange.getRequestBody();
                String t = new String(in.readAllBytes(), DEFAULT_CHARSET);
                Epic epic = gson.fromJson(t, Epic.class);
                try {
                    taskManager.removeEpic(epic);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                if (!taskManager.getEpics().containsValue(epic)) {
                    writeResponse(exchange, "Задачa " + gson.toJson(epic) + " больше нету", 201);
                    return;
                }
                writeResponse(exchange, "Ошибка при удалений", 404);
            }
            case DELETE_ALL:
                taskManager.removeAllEpics();
                if (taskManager.getEpics().isEmpty()) {
                    writeResponse(exchange, "Задачи удалены", 201);
                    return;
                }
                writeResponse(exchange, "Ошибка при удалений", 404);
            default:
                writeResponse(exchange, "Такого эндпоинта не существует", 404);
        }
    }


}
