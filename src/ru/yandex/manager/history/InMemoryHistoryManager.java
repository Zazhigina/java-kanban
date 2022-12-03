package ru.yandex.manager.history;

import ru.yandex.task.Task;

import java.util.ArrayList;


public class InMemoryHistoryManager implements HistoryManager {
    private ArrayList<Integer> viewHistory = new ArrayList<>();

    @Override
    public void add(Task task) {
        if (viewHistory.size() > 9) {
            viewHistory.remove(0);
            viewHistory.add(task.getId());
        } else {
            viewHistory.add(task.getId());
        }
    }


    @Override
    public ArrayList<Integer> getHistory() {
        return viewHistory;
    }


}
