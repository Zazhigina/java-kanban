package ru.yandex.manager;
import ru.yandex.manager.History.HistoryManager;
import ru.yandex.manager.History.InMemoryHistoryManager;
import ru.yandex.manager.Task.InMemoryTaskManager;
import ru.yandex.manager.Task.TaskManager;

public class Managers {
    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
