import ru.yandex.manager.Manager;
import ru.yandex.task.*;
import ru.yandex.status.Status;

public class Main {

    public static void main(String[] args) {
        Manager manager = new Manager();

        manager.createTask("Сходить в магазин", "Купить все по списку");
        manager.createTask("Доделать задачу", "Исправить ошибки по учебе");

        Epic epic = manager.createEpic("зачеты", "сдать долги");
        Subtask subtask = manager.createSubtask("матан", "долг по матрице", epic);
        Subtask subtask1 = manager.createSubtask("химия", "зачет по полимерам", epic);

        Epic epic1 = manager.createEpic("Отпуск", "собрать вещи");
        Subtask subtask2 = manager.createSubtask("найти купальник", "купальник красного цвета", epic1);


        manager.watchEpic();
        //manager.watchSubtask();
        //manager.getListSubtaskByEpic(3);
        // manager.updateStatusByIdSubtask(subtask1, ru.yandex.status.Status.DONE);
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

