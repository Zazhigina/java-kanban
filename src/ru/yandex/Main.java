package ru.yandex;

import ru.yandex.manager.task.FileBackedTasksManager;
import ru.yandex.entites.*;
import ru.yandex.manager.task.TaskManager;
import ru.yandex.server.HttpTaskManager;
import ru.yandex.server.HttpTaskServer;
import ru.yandex.server.KVServer;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;

public class Main {

    //public static HttpTaskServer httpTaskServer;

    public static void main(String[] args) throws Exception {

/*
        KVServer kvServer ; // создали веб-сервер
        KVServer KVServer = new KVServer();

        HttpTaskManager askManager = new HttpTaskManager("http://localhost:8078");
        askManager.save();
        String tt = askManager.load("tasks");
        System.out.println(tt);

        Task task = taskManager.createTask("Сходить в магазин", "Купить все по списку", LocalDate.of(2023, 02, 02), Duration.ofDays(1));
        Task task1 = taskManager.createTask("Доделать задачу", "Исправить ошибки по учебе", LocalDate.of(2023, 02, 03), Duration.ofDays(7));

        Epic epic = taskManager.createEpic("зачеты", "сдать долги");
        Subtask subtask = taskManager.createSubtask("матан", "долг по матрице", 3, LocalDate.of(2023, 01, 20), Duration.ofDays(1));
        Subtask subtask1 = taskManager.createSubtask("химия", "зачет по полимерам", 3, LocalDate.of(2023, 01, 01), Duration.ofDays(1));
        Subtask subtask2 = taskManager.createSubtask("Физика", "сдать курсач", 3, LocalDate.of(2023, 01, 07), Duration.ofDays(6));

        Epic epic1 = taskManager.createEpic("Отпуск", "собрать вещи");

        System.out.println(taskManager.getTaskById(2));
        System.out.println(taskManager.getEpicById(3));
        System.out.println(taskManager.getSubtaskById(4));
        System.out.println(taskManager.getEpicById(7));
        System.out.println(taskManager.getSubtaskById(5));
        System.out.println("История вызовов " + taskManager.getHistory());

        System.out.println("Отсортированные задачи " + taskManager.getPrioritizedTasks());

*/

        // KVServer kvServer ; // создали веб-сервер
       // KVServer KVServer = new KVServer();

        //HttpTaskManager taskManager = new HttpTaskManager("http://localhost:8078");
        //File file = new File("resources/task_history.scv");
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager("resources/task_history.scv");

        Task task = fileBackedTasksManager.createTask("Сходить в магазин", "Купить все по списку", LocalDate.of(2023, 02, 02), 1L);
        Task task1 = fileBackedTasksManager.createTask("Доделать задачу", "Исправить ошибки по учебе", LocalDate.of(2023, 02, 03), 1L);

        Epic epic = fileBackedTasksManager.createEpic("зачеты", "сдать долги");
        Subtask subtask = fileBackedTasksManager.createSubtask("матан", "долг по матрице", 3, LocalDate.of(2023, 01, 20), 1L);
        Subtask subtask1 = fileBackedTasksManager.createSubtask("химия", "зачет по полимерам", 3, LocalDate.of(2023, 01, 01),1L);
        Subtask subtask2 = fileBackedTasksManager.createSubtask("Физика", "сдать курсач", 3, LocalDate.of(2023, 01, 07), 1L);

        Epic epic1 = fileBackedTasksManager.createEpic("Отпуск", "собрать вещи");

        System.out.println(fileBackedTasksManager.getTaskById(2));
        System.out.println(fileBackedTasksManager.getEpicById(3));
        System.out.println(fileBackedTasksManager.getSubtaskById(4));
        System.out.println(fileBackedTasksManager.getEpicById(7));
        System.out.println(fileBackedTasksManager.getSubtaskById(5));
        System.out.println("История вызовов " + fileBackedTasksManager.getHistory());

        System.out.println("Отсортированные задачи " + fileBackedTasksManager.getPrioritizedTasks());
        //taskManager.save();
        //String tt = taskManager.load("tasks");
        //System.out.println(tt);
        new HttpTaskServer();

    }


       /*
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

        */
}



