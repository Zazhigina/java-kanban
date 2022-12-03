package ru.yandex.manager;
import ru.yandex.manager.history.HistoryManager;
import ru.yandex.manager.history.InMemoryHistoryManager;
import ru.yandex.manager.task.InMemoryTaskManager;
import ru.yandex.manager.task.TaskManager;

public class Managers {
    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
