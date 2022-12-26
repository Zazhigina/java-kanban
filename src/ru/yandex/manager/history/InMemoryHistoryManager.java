package ru.yandex.manager.history;

import ru.yandex.task.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class InMemoryHistoryManager implements HistoryManager {
    private CustomLinkedList<Task> list = new CustomLinkedList<>();
    private HashMap<Integer, Node> containerLink = new HashMap<>();

    public class CustomLinkedList<T> {
        private Node<T> head;
        private Node<T> tail;


        void linkLast(Node node) {
            final Node<T> oldTail = tail;
            node.prev = oldTail;
            tail = node;
            if (oldTail == null) {
                head = node;
            } else {
                oldTail.next = node;
            }
        }

        List<Task> getTasks() {
            List<Task> tasksList = new ArrayList<>();
            Node node = head;
            while (node != null) {
                tasksList.add((Task) node.data);
                node = node.next;
            }
            return tasksList;

        }

        void removeNode(Node node) {
            Node nodeNext = node.next;
            Node nodePrev = node.prev;
            if (nodeNext == null && nodePrev == null) {
                head = null;
                tail = null;
                node = null;
            }
            if (nodeNext != null && nodePrev == null) {
                head = nodeNext;
                nodeNext.prev = null;
            }
            if (nodeNext == null && nodePrev != null) {
                tail = nodePrev;
                nodePrev.next = null;
            }
            if (nodeNext != null && nodePrev != null) {
                nodePrev.next = nodeNext;
                nodeNext.prev = nodePrev;
            }
        }
    }

    @Override
    public void add(Task task) {
        Node<Task> node = new Node<>(task, null, null);
        if (containerLink.containsKey(node.data.getId())) {
            remove(node.data.getId());
        }
        list.linkLast(node);
        containerLink.put(task.getId(), node);
    }

    @Override
    public void remove(int id) {
        list.removeNode(containerLink.get(id));
        containerLink.remove(id);
    }

    @Override
    public ArrayList<Task> getHistory() {

        return (ArrayList<Task>) list.getTasks();
    }


}
