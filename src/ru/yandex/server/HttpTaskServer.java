package ru.yandex.server;

import com.sun.net.httpserver.HttpServer;

import java.net.InetSocketAddress;

import java.io.IOException;

import ru.yandex.manager.task.FileBackedTasksManager;
import ru.yandex.server.handler.*;
import ru.yandex.server.handler.PrioritizedHandler;

public class HttpTaskServer {
    private static final int PORT = 8080;
    private static final String path = "resources/task_history.scv";
    //private final HttpServer httpServer;
    FileBackedTasksManager fileBackedTasksManager = FileBackedTasksManager.loadFromFile(path);

    static HttpServer httpServer;

    public HttpTaskServer() throws IOException {
        httpServer = HttpServer.create(); // создали веб-сервер
        httpServer.bind(new InetSocketAddress(PORT), 0);
        httpServer.createContext("/tasks/task/", new TaskHandler(fileBackedTasksManager));
        httpServer.createContext("/tasks/task/?id=", new TaskHandler(fileBackedTasksManager));
        httpServer.createContext("/tasks/epic/", new EpicHandler(fileBackedTasksManager));
        httpServer.createContext("/tasks/epic/?id=", new TaskHandler(fileBackedTasksManager));
        httpServer.createContext("/tasks/epic/Body:{epic..}", new TaskHandler(fileBackedTasksManager));
        httpServer.createContext("/tasks/subtask/", new SubtaskHandler(fileBackedTasksManager));
        httpServer.createContext("/tasks/subtask/?id=", new TaskHandler(fileBackedTasksManager));
        httpServer.createContext("/tasks/subtask/Body:{subtask..}", new TaskHandler(fileBackedTasksManager));
        httpServer.createContext("/tasks/subtask/epic/?id=", new TaskHandler(fileBackedTasksManager));
        httpServer.createContext("/tasks/history", new TaskHandler(fileBackedTasksManager));
        httpServer.createContext("/tasks/", new PrioritizedHandler(fileBackedTasksManager));
        httpServer.start();
        System.out.println("HTTP-сервер запущен на " + PORT + " порту!");
    }

    static public void stop()
    {
        httpServer.stop(0);
    }

}
