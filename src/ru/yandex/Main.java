package ru.yandex;

import ru.yandex.manager.Managers;
import ru.yandex.manager.task.TaskManager;

import ru.yandex.task.*;

import java.util.HashMap;

public class Main {
    static TaskManager taskManager = Managers.getDefault();

    public static void main(String[] args) {


        Task task = taskManager.createTask("Сходить в магазин", "Купить все по списку");
        Task task1 = taskManager.createTask("Доделать задачу", "Исправить ошибки по учебе");

        Epic epic = taskManager.createEpic("зачеты", "сдать долги");
        Subtask subtask = taskManager.createSubtask("матан", "долг по матрице", epic);
        Subtask subtask1 = taskManager.createSubtask("химия", "зачет по полимерам", epic);
        Subtask subtask2 = taskManager.createSubtask("Физика", "сдать курсач", epic);

        Epic epic1 = taskManager.createEpic("Отпуск", "собрать вещи");


        System.out.println(taskManager.getTaskById(2));
        System.out.println(taskManager.getEpicById(3));
        System.out.println(taskManager.getSubtaskById(4));
        System.out.println(taskManager.getEpicById(7));
        System.out.println(taskManager.getSubtaskById(5));
        System.out.println(taskManager.getEpicById(3));
        System.out.println(taskManager.getTaskById(1));
        System.out.println(taskManager.getSubtaskById(4));
        System.out.println(taskManager.getEpicById(7));
        System.out.println(taskManager.getSubtaskById(5));
        System.out.println(taskManager.getSubtaskById(5));
        System.out.println(taskManager.getSubtaskById(6));
        System.out.println(taskManager.getTaskById(1));
        System.out.println(taskManager.getSubtaskById(5));
        System.out.println(taskManager.getSubtaskById(6));


        System.out.println("История вызовов " + taskManager.getHistory());
        System.out.println("Обновили.Посмотреть только Эпики ");
        watchTask();

        taskManager.removeEpic(epic);
        taskManager.removeTask(task1);


        System.out.println("История вызовов после удаления" + taskManager.getHistory());
        System.out.println("Обновили.Посмотреть только ЭПИКИ ");
        watchEpic();

        System.out.println("Обновили.Посмотреть только ЗАДАЧЧИ ");
        watchTask();

        System.out.println("Обновили.Посмотреть только ПОДЗАДАЧИ ");
        watchSubtask();
    }


    static void watchTask() {
        HashMap<Integer, Task> tasks = taskManager.getTasks();
        for (Task value : tasks.values()) {
            System.out.println(value.toString());
        }
    }

    static void watchEpic() {
        HashMap<Integer, Epic> epics = taskManager.getEpics();
        for (Epic value : epics.values()) {
            System.out.println(value.toString());
            for (Subtask subtask : value.getSubtasks().values()) {
                System.out.println(subtask.toString());
            }
        }
    }


    static void watchSubtask() {
        HashMap<Integer, Subtask> subtasks = taskManager.getSubtasks();
        for (Subtask sub : subtasks.values()) {
            System.out.println(sub.toString());
        }
    }
}



