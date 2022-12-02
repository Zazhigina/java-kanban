import ru.yandex.manager.Manager;
import ru.yandex.task.*;
import ru.yandex.status.Status;

import java.util.HashMap;

public class Main {
    static Manager manager = new Manager();

    public static void main(String[] args) {


        manager.createTask("Сходить в магазин", "Купить все по списку");
        manager.createTask("Доделать задачу", "Исправить ошибки по учебе");

        Epic epic = manager.createEpic("зачеты", "сдать долги");
        Subtask subtask = manager.createSubtask("матан", "долг по матрице", epic);
        Subtask subtask1 = manager.createSubtask("химия", "зачет по полимерам", epic);

        Epic epic1 = manager.createEpic("Отпуск", "собрать вещи");
        Subtask subtask2 = manager.createSubtask("найти купальник", "купальник красного цвета", epic1);

        watchEpic();
        //manager.watchSubtask();
        //manager.getListSubtaskByEpic(3);
        // manager.updateStatusByIdSubtask(subtask1, ru.yandex.status.Status.DONE);
        manager.checkStatusByEpic(3);
        System.out.println("Посмотреть только подзадачи ");
        watchSubtask();
        // manager.removeEpic(epic);
        System.out.println("Посмотреть только Эпики ");
        watchEpic();
        manager.removeSubtask(subtask);
        System.out.println("Посмотреть только Эпики ");
        watchEpic();


    }

    static void watchTask() {
        HashMap<Integer, Task> tasks = manager.getTasks();
        for (Task value : tasks.values()) {
            System.out.println(value.toString());
        }
    }

    static void watchEpic() {
        HashMap<Integer, Epic> epics = manager.getEpics();
        for (Epic value : epics.values()) {
            System.out.println(value.toString());
            watchSubtask();
        }
    }


    static void watchSubtask() {
        HashMap<Integer, Subtask> subtasks = manager.getSubtasks();
        for (Subtask sub : subtasks.values()) {
            System.out.println(sub.toString());
        }
    }
}



