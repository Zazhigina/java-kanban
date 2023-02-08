package ru.yandex;

import ru.yandex.manager.Managers;
import ru.yandex.manager.task.TaskManager;
import ru.yandex.entites.*;

import java.time.Duration;
import java.time.LocalDate;
import java.util.HashMap;

public class Main {
    public static TaskManager taskManager = Managers.getDefault();

    public static void main(String[] args) throws Exception {
/*

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
    }*/
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



