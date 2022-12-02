package ru.yandex.manager.History;
import ru.yandex.task.*;

import java.util.ArrayList;


public interface HistoryManager {
    void add(Task task); // должен помечать задачи как просмотренные

    ArrayList<Task> getHistory(); // возвращать их список.

}
