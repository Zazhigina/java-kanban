package ru.yandex.manager.history;
import ru.yandex.task.Task;

import java.util.ArrayList;


public interface HistoryManager {
    void add(Task task); // должен помечать задачи как просмотренные

    ArrayList<Integer> getHistory(); // возвращать их список.

}
