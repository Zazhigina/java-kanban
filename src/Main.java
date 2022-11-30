import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Manager manager = new Manager();

        manager.createTask("Сходить в магазин", "Купить все по списку");
        manager.createTask("Доделать задачу", "Исправить ошибки по учебе");

        Epic epic = manager.createEpic("зачеты", "сдать долги");
        Task task = manager.createTask("матан", "долг по матрице");
        Task task1 = manager.createTask("химия", "зачет по полимерам");
        Subtask subtask = manager.createSubtask(task, epic);
        Subtask subtask1 = manager.createSubtask(task1, epic);

        Epic epic1 = manager.createEpic("Отпуск", "собрать вещи");
        Task task2 = manager.createTask("найти купальник", "купальник красного цвета");
        Subtask subtask2 = manager.createSubtask(task2, epic1);


        manager.watchEpic();
        //manager.watchSubtask();
        //manager.getListSubtaskByEpic(3);
        manager.updateStatusByIdSubtask(subtask, Status.DONE);
        // manager.updateStatusByIdSubtask(subtask1, Status.DONE);
        manager.checkStatusByEpic(3);
        System.out.println("Посмотреть только подзадачи ");
        manager.watchSubtask();
        // manager.removeEpic(epic);
        System.out.println("Посмотреть только Эпики ");
        manager.watchEpic();
        manager.removeSubtask(subtask);
        System.out.println("Посмотреть только Эпики ");
        manager.watchEpic();

    }
}

