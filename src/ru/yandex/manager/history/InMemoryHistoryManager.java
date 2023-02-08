package ru.yandex.manager.history;

import ru.yandex.entites.Task;

import java.util.ArrayList;
import java.util.HashMap;


public class InMemoryHistoryManager implements HistoryManager {
    private CustomLinkedList<Task> list = new CustomLinkedList<>();
    private HashMap<Integer, Node> containerLink = new HashMap<>();


    @Override
    public void add(Task task) {
        try {
            Node<Task> node = new Node<>(task, null, null);

            if (containerLink.containsKey(node.getData().getId())) {
                remove(node.getData().getId());
            }
            list.linkLast(node);
            containerLink.put(task.getId(), node);
        } catch (NullPointerException e) {
            throw new NullPointerException("Такой задачи нету.Неверный идентификатор");
        }


    }

    @Override
    public void remove(int id) {
        if (containerLink.containsKey(id)){
            containerLink.remove(list.removeNode(containerLink.get(id)));
        }
    }

    @Override
    public ArrayList<Task> getHistory() {

        return (ArrayList<Task>) list.getTasks();
    }


}
