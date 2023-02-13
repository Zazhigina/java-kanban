package ru.yandex.manager;
import ru.yandex.manager.history.HistoryManager;
import ru.yandex.manager.history.InMemoryHistoryManager;
import ru.yandex.manager.task.InMemoryTaskManager;
import ru.yandex.manager.task.TaskManager;
import ru.yandex.server.HttpTaskManager;

import java.io.IOException;

public class Managers {
    public static TaskManager getDefault() throws IOException {
        return new HttpTaskManager("http://localhost:8078");
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
