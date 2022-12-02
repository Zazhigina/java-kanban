package ru.yandex.manager.History;

import ru.yandex.task.Task;

import java.util.ArrayList;


public class InMemoryHistoryManager implements HistoryManager {
    private static ArrayList<Task> viewHistory = new ArrayList<>();

    @Override
    public void add(Task task) {

        viewHistory.add(task);
    }


    @Override
    public ArrayList<Task> getHistory() {
        return viewHistory;
    }

    public void checkListHistory() {
        if (viewHistory.size() > 9) {
            viewHistory.remove(0);

        } else {
            return;
        }
    }

}
