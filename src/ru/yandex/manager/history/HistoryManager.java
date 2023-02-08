package ru.yandex.manager.history;

import ru.yandex.entites.Task;

import java.util.ArrayList;


public interface HistoryManager {
    void add(Task task); // должен помечать задачи как просмотренные

    void remove(int id);

    ArrayList<Task> getHistory(); // возвращать их список.

}
