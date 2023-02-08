package ru.yandex.manager.history;

import java.util.ArrayList;
import java.util.List;


public class CustomLinkedList<T> {
    private Node<T> head;
    private Node<T> tail;


    void linkLast(Node<T> node) {
        final Node<T> oldTail = tail;
        node.setPrev(oldTail);
        tail = node;
        if (oldTail == null) {
            head = node;
        } else {
            oldTail.setNext(node);
        }
    }

    List<T> getTasks() {
        List<T> tasksList = new ArrayList<>();
        Node<T> node = head;
        while (node != null) {
            tasksList.add(node.getData());
            node = node.getNext();
        }
        return tasksList;

    }

    Node<T> removeNode(Node<T> node) {
        Node<T> nodeNext = node.getNext();
        Node<T> nodePrev = node.getPrev();

        if (nodeNext == null && nodePrev == null) {
            head = null;
            tail = null;
        }
        if (nodeNext != null && nodePrev == null) {
            head = nodeNext;
            nodeNext.setPrev(null);
        }
        if (nodeNext == null && nodePrev != null) {
            tail = nodePrev;
            nodePrev.setNext(null);
        }
        if (nodeNext != null && nodePrev != null) {
            nodePrev.setNext(nodeNext);
            nodeNext.setPrev(nodePrev);

        }
        return null;

    }

}


