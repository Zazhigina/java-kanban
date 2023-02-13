package ru.yandex.test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.entites.Epic;
import ru.yandex.entites.Subtask;
import ru.yandex.entites.Task;
import ru.yandex.manager.task.InMemoryTaskManager;
import ru.yandex.manager.task.TaskManager;
import ru.yandex.server.HttpTaskServer;
import ru.yandex.server.LocalDateTypeAdapter;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class HttpTaskServerTest extends HttpTaskServer {

    private static final String path = "http://localhost:8080";
    TaskManager taskManager = new InMemoryTaskManager();

    public HttpTaskServerTest() throws IOException, URISyntaxException {
    }

    @BeforeEach
    public void beforeEach() throws Exception {
        Task task = taskManager.createTask("Сходить в магазин", "Купить все по списку", LocalDate.of(2023, 02, 02), 1L);
        Task task1 = taskManager.createTask("Доделать задачу", "Исправить ошибки по учебе", LocalDate.of(2023, 02, 03), 1L);

        Epic epic = taskManager.createEpic("зачеты", "сдать долги");
        Subtask subtask = taskManager.createSubtask("матан", "долг по матрице", 3, LocalDate.of(2023, 01, 20), 1L);
        Subtask subtask1 = taskManager.createSubtask("химия", "зачет по полимерам", 3, LocalDate.of(2023, 01, 01), 1L);
        Subtask subtask2 = taskManager.createSubtask("Физика", "сдать курсач", 3, LocalDate.of(2023, 01, 07), 1L);

        Epic epic1 = taskManager.createEpic("Отпуск", "собрать вещи");

        System.out.println(taskManager.getTaskById(2));
        System.out.println(taskManager.getEpicById(3));
        System.out.println(taskManager.getSubtaskById(4));
        System.out.println(taskManager.getEpicById(7));
        System.out.println(taskManager.getSubtaskById(5));
        System.out.println("История вызовов " + taskManager.getHistory());

        System.out.println("Отсортированные задачи " + taskManager.getPrioritizedTasks());

        //new HttpTaskServer();

    }

    @AfterEach
    public void afterEach() {
        HttpTaskServer.stop();
    }

    @Test
    public void shouldCorrectlyRequestTaskGet() throws IOException, URISyntaxException, InterruptedException {
        URI uri = new URI(path + "/tasks/task/");

        // создаём объект, описывающий HTTP-запрос
        HttpRequest request = HttpRequest.newBuilder() // получаем экземпляр билдера
                .GET()    // указываем HTTP-метод запроса
                .uri(uri) // указываем адрес ресурса
                .header("Content-type", "application/json") // указываем заголовок
                .build(); // заканчиваем настройку и создаём ("строим") http-запрос

        // HTTP-клиент с настройками по умолчанию
        HttpClient client = HttpClient.newHttpClient();

        // получаем стандартный обработчик тела запроса с конвертацией содержимого в строку

        // отправляем запрос и получаем ответ от сервера
        HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
        assertNotNull(response.body(), "Ответ не отправлен.");
        assertEquals(response.statusCode(), 200, "Ошибка");
    }

    @Test
    public void shouldCorrectlyRequestEpicGet() throws IOException, URISyntaxException, InterruptedException {
        URI uri = new URI(path + "/tasks/epic/");

        // создаём объект, описывающий HTTP-запрос
        HttpRequest request = HttpRequest.newBuilder() // получаем экземпляр билдера
                .GET()    // указываем HTTP-метод запроса
                .uri(uri) // указываем адрес ресурса
                .header("Content-type", "application/json") // указываем заголовок
                .build(); // заканчиваем настройку и создаём ("строим") http-запрос

        // HTTP-клиент с настройками по умолчанию
        HttpClient client = HttpClient.newHttpClient();

        // получаем стандартный обработчик тела запроса с конвертацией содержимого в строку

        // отправляем запрос и получаем ответ от сервера
        HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
        assertNotNull(response.body(), "Ответ не отправлен.");
        assertEquals(response.statusCode(), 200, "Ошибка");
    }

    @Test
    public void shouldCorrectlyRequestSubtaskGet() throws IOException, URISyntaxException, InterruptedException {
        URI uri = new URI(path + "/tasks/subtask/");

        // создаём объект, описывающий HTTP-запрос
        HttpRequest request = HttpRequest.newBuilder() // получаем экземпляр билдера
                .GET()    // указываем HTTP-метод запроса
                .uri(uri) // указываем адрес ресурса
                .header("Content-type", "application/json") // указываем заголовок
                .build(); // заканчиваем настройку и создаём ("строим") http-запрос

        // HTTP-клиент с настройками по умолчанию
        HttpClient client = HttpClient.newHttpClient();

        // получаем стандартный обработчик тела запроса с конвертацией содержимого в строку

        // отправляем запрос и получаем ответ от сервера
        HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
        assertNotNull(response.body(), "Ответ не отправлен.");
        assertEquals(response.statusCode(), 200, "Ошибка");
    }

    @Test
    public void shouldCorrectlyRequestTaskGetID() throws IOException, URISyntaxException, InterruptedException {
        URI uri = new URI(path + "/tasks/task/?id=1");

        // создаём объект, описывающий HTTP-запрос
        HttpRequest request = HttpRequest.newBuilder() // получаем экземпляр билдера
                .GET()    // указываем HTTP-метод запроса
                .uri(uri) // указываем адрес ресурса
                .header("Content-type", "application/json") // указываем заголовок
                .build(); // заканчиваем настройку и создаём ("строим") http-запрос

        // HTTP-клиент с настройками по умолчанию
        HttpClient client = HttpClient.newHttpClient();

        // получаем стандартный обработчик тела запроса с конвертацией содержимого в строку

        // отправляем запрос и получаем ответ от сервера
        HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
        assertNotNull(response.body(), "Ответ не отправлен.");
        assertEquals(response.statusCode(), 200, "Ошибка");
    }

    @Test
    public void shouldCorrectlyRequestEpicGetID() throws IOException, URISyntaxException, InterruptedException {
        URI uri = new URI(path + "/tasks/epic/?id=3");

        // создаём объект, описывающий HTTP-запрос
        HttpRequest request = HttpRequest.newBuilder() // получаем экземпляр билдера
                .GET()    // указываем HTTP-метод запроса
                .uri(uri) // указываем адрес ресурса
                .header("Content-type", "application/json") // указываем заголовок
                .build(); // заканчиваем настройку и создаём ("строим") http-запрос

        // HTTP-клиент с настройками по умолчанию
        HttpClient client = HttpClient.newHttpClient();

        // получаем стандартный обработчик тела запроса с конвертацией содержимого в строку

        // отправляем запрос и получаем ответ от сервера
        HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
        assertNotNull(response.body(), "Ответ не отправлен.");
        assertEquals(response.statusCode(), 200, "Ошибка");
    }

    @Test
    public void shouldCorrectlyRequestSubtaskGetID() throws IOException, URISyntaxException, InterruptedException {
        URI uri = new URI(path + "/tasks/subtask/?id=4");

        // создаём объект, описывающий HTTP-запрос
        HttpRequest request = HttpRequest.newBuilder() // получаем экземпляр билдера
                .GET()    // указываем HTTP-метод запроса
                .uri(uri) // указываем адрес ресурса
                .header("Content-type", "application/json") // указываем заголовок
                .build(); // заканчиваем настройку и создаём ("строим") http-запрос

        // HTTP-клиент с настройками по умолчанию
        HttpClient client = HttpClient.newHttpClient();

        // получаем стандартный обработчик тела запроса с конвертацией содержимого в строку

        // отправляем запрос и получаем ответ от сервера
        HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
        assertNotNull(response.body(), "Ответ не отправлен.");
        assertEquals(response.statusCode(), 200, "Ошибка");
    }

    @Test
    public void shouldCorrectlyRequestEpicIDSubtaskGet() throws IOException, URISyntaxException, InterruptedException {
        URI uri = new URI(path + "/tasks/subtask/epic/?id=5");

        // создаём объект, описывающий HTTP-запрос
        HttpRequest request = HttpRequest.newBuilder() // получаем экземпляр билдера
                .GET()    // указываем HTTP-метод запроса
                .uri(uri) // указываем адрес ресурса
                .header("Content-type", "application/json") // указываем заголовок
                .build(); // заканчиваем настройку и создаём ("строим") http-запрос

        // HTTP-клиент с настройками по умолчанию
        HttpClient client = HttpClient.newHttpClient();

        // отправляем запрос и получаем ответ от сервера
        HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
        assertNotNull(response.body(), "Ответ не отправлен.");
        assertEquals(response.statusCode(), 200, "Ошибка");
    }

    @Test
    public void shouldCorrectlyRequestHistoryGet() throws IOException, URISyntaxException, InterruptedException {
        URI uri = new URI(path + "/tasks/history");

        // создаём объект, описывающий HTTP-запрос
        HttpRequest request = HttpRequest.newBuilder() // получаем экземпляр билдера
                .GET()    // указываем HTTP-метод запроса
                .uri(uri) // указываем адрес ресурса
                .header("Content-type", "application/json") // указываем заголовок
                .build(); // заканчиваем настройку и создаём ("строим") http-запрос

        // HTTP-клиент с настройками по умолчанию
        HttpClient client = HttpClient.newHttpClient();

        // отправляем запрос и получаем ответ от сервера
        HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
        assertNotNull(response.body(), "Ответ не отправлен.");
        assertEquals(response.statusCode(), 200, "Ошибка");
    }

    @Test
    public void shouldCorrectlyRequestPrioritizedGet() throws IOException, URISyntaxException, InterruptedException {
        URI uri = new URI(path + "/tasks/");

        // создаём объект, описывающий HTTP-запрос
        HttpRequest request = HttpRequest.newBuilder() // получаем экземпляр билдера
                .GET()    // указываем HTTP-метод запроса
                .uri(uri) // указываем адрес ресурса
                .header("Content-type", "application/json") // указываем заголовок
                .build(); // заканчиваем настройку и создаём ("строим") http-запрос

        // HTTP-клиент с настройками по умолчанию
        HttpClient client = HttpClient.newHttpClient();

        // отправляем запрос и получаем ответ от сервера
        HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
        assertNotNull(response.body(), "Ответ не отправлен.");
        assertEquals(response.statusCode(), 200, "Ошибка");
    }

    @Test
    public void shouldCorrectlyDeleteTaskAll() throws IOException, URISyntaxException, InterruptedException {
        URI uri = new URI(path + "/tasks/task/");

        // создаём объект, описывающий HTTP-запрос
        HttpRequest request = HttpRequest.newBuilder() // получаем экземпляр билдера
                .DELETE()    // указываем HTTP-метод запроса
                .uri(uri) // указываем адрес ресурса
                .header("Content-type", "application/json") // указываем заголовок
                .build(); // заканчиваем настройку и создаём ("строим") http-запрос

        // HTTP-клиент с настройками по умолчанию
        HttpClient client = HttpClient.newHttpClient();

        // отправляем запрос и получаем ответ от сервера
        HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
        assertNotNull(response.body(), "Ответ не отправлен.");
        assertEquals(response.statusCode(), 201, "Ошибка");
    }

    @Test
    public void shouldCorrectlyDeleteEpicAll() throws IOException, URISyntaxException, InterruptedException {
        URI uri = new URI(path + "/tasks/epic/");

        // создаём объект, описывающий HTTP-запрос
        HttpRequest request = HttpRequest.newBuilder() // получаем экземпляр билдера
                .DELETE()    // указываем HTTP-метод запроса
                .uri(uri) // указываем адрес ресурса
                .header("Content-type", "application/json") // указываем заголовок
                .build(); // заканчиваем настройку и создаём ("строим") http-запрос

        // HTTP-клиент с настройками по умолчанию
        HttpClient client = HttpClient.newHttpClient();

        // отправляем запрос и получаем ответ от сервера
        HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
        assertNotNull(response.body(), "Ответ не отправлен.");
        assertEquals(response.statusCode(), 201, "Ошибка");
    }

    @Test
    public void shouldCorrectlyDeleteSubtaskAll() throws IOException, URISyntaxException, InterruptedException {
        URI uri = new URI(path + "/tasks/subtask/");

        // создаём объект, описывающий HTTP-запрос
        HttpRequest request = HttpRequest.newBuilder() // получаем экземпляр билдера
                .DELETE()    // указываем HTTP-метод запроса
                .uri(uri) // указываем адрес ресурса
                .header("Content-type", "application/json") // указываем заголовок
                .build(); // заканчиваем настройку и создаём ("строим") http-запрос

        // HTTP-клиент с настройками по умолчанию
        HttpClient client = HttpClient.newHttpClient();

        // отправляем запрос и получаем ответ от сервера
        HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
        assertNotNull(response.body(), "Ответ не отправлен.");
        assertEquals(response.statusCode(), 201, "Ошибка");
    }

    @Test
    public void shouldCorrectlyDeleteTaskID() throws IOException, URISyntaxException, InterruptedException {
        URI uri = new URI(path + "/tasks/task/?id=1");

        // создаём объект, описывающий HTTP-запрос
        HttpRequest request = HttpRequest.newBuilder() // получаем экземпляр билдера
                .DELETE()    // указываем HTTP-метод запроса
                .uri(uri) // указываем адрес ресурса
                .header("Content-type", "application/json") // указываем заголовок
                .build(); // заканчиваем настройку и создаём ("строим") http-запрос

        // HTTP-клиент с настройками по умолчанию
        HttpClient client = HttpClient.newHttpClient();

        // отправляем запрос и получаем ответ от сервера
        HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
        assertNotNull(response.body(), "Ответ не отправлен.");
        assertEquals(response.statusCode(), 201, "Ошибка");
    }

    @Test
    public void shouldCorrectlyDeleteEpicID() throws IOException, URISyntaxException, InterruptedException {
        URI uri = new URI(path + "/tasks/epic/?id=3");

        // создаём объект, описывающий HTTP-запрос
        HttpRequest request = HttpRequest.newBuilder() // получаем экземпляр билдера
                .DELETE()    // указываем HTTP-метод запроса
                .uri(uri) // указываем адрес ресурса
                .header("Content-type", "application/json") // указываем заголовок
                .build(); // заканчиваем настройку и создаём ("строим") http-запрос

        // HTTP-клиент с настройками по умолчанию
        HttpClient client = HttpClient.newHttpClient();

        // отправляем запрос и получаем ответ от сервера
        HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
        assertNotNull(response.body(), "Ответ не отправлен.");
        assertEquals(response.statusCode(), 201, "Ошибка");
    }

    @Test
    public void shouldCorrectlyDeleteSubtaskID() throws IOException, URISyntaxException, InterruptedException {
        URI uri = new URI(path + "/tasks/subtask/?id=");

        // создаём объект, описывающий HTTP-запрос
        HttpRequest request = HttpRequest.newBuilder() // получаем экземпляр билдера
                .DELETE()    // указываем HTTP-метод запроса
                .uri(uri) // указываем адрес ресурса
                .header("Content-type", "application/json") // указываем заголовок
                .build(); // заканчиваем настройку и создаём ("строим") http-запрос

        // HTTP-клиент с настройками по умолчанию
        HttpClient client = HttpClient.newHttpClient();

        // отправляем запрос и получаем ответ от сервера
        HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
        assertNotNull(response.body(), "Ответ не отправлен.");
        assertEquals(response.statusCode(), 201, "Ошибка");
    }

    @Test
    public void shouldCorrectlyPostTask() throws IOException, URISyntaxException, InterruptedException {
        URI uri = new URI(path + "/tasks/task/");
        Task task = taskManager.getEpics().get(2);
        task.setName("Новое имя");

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter());
        Gson gson = gsonBuilder.create();

        HttpRequest request = HttpRequest.newBuilder() // получаем экземпляр билдера
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(task)))    // указываем HTTP-метод запроса
                .uri(uri) // указываем адрес ресурса
                .build(); // заканчиваем настройку и создаём ("строим") http-запрос

        // HTTP-клиент с настройками по умолчанию
        HttpClient client = HttpClient.newHttpClient();
        // отправляем запрос и получаем ответ от сервера
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
        assertNotNull(response.body(), "Ответ не отправлен.");
        assertEquals(response.statusCode(), 201, "Ошибка");
    }

    @Test
    public void shouldCorrectlyPostEpic() throws IOException, URISyntaxException, InterruptedException {
        URI uri = new URI(path + "/tasks/epic/");
        Epic epic = taskManager.getEpics().get(3);
        epic.setName("Новое имя");

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter());
        Gson gson = gsonBuilder.create();

        // создаём объект, описывающий HTTP-запрос
        HttpRequest request = HttpRequest.newBuilder() // получаем экземпляр билдера
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(epic)))        // указываем HTTP-метод запроса
                .uri(uri) // указываем адрес ресурса
                .header("Content-type", "application/json") // указываем заголовок
                .build(); // заканчиваем настройку и создаём ("строим") http-запрос

        // HTTP-клиент с настройками по умолчанию
        HttpClient client = HttpClient.newHttpClient();

        // отправляем запрос и получаем ответ от сервера
        HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
        assertNotNull(response.body(), "Ответ не отправлен.");
        assertEquals(response.statusCode(), 201, "Ошибка");
    }

    @Test
    public void shouldCorrectlyPostSubtask() throws IOException, URISyntaxException, InterruptedException {
        URI uri = new URI(path + "/tasks/subtask/");
        Subtask subtask = taskManager.getSubtasks().get(5);
        subtask.setName("Новое имя");

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter());
        Gson gson = gsonBuilder.create();

        // создаём объект, описывающий HTTP-запрос
        HttpRequest request = HttpRequest.newBuilder() // получаем экземпляр билдера
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(subtask)))      // указываем HTTP-метод запроса
                .uri(uri) // указываем адрес ресурса
                .header("Content-type", "application/json") // указываем заголовок
                .build(); // заканчиваем настройку и создаём ("строим") http-запрос

        // HTTP-клиент с настройками по умолчанию
        HttpClient client = HttpClient.newHttpClient();

        // отправляем запрос и получаем ответ от сервера
        HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
        assertNotNull(response.body(), "Ответ не отправлен.");
        assertEquals(response.statusCode(), 201, "Ошибка");
    }

}
