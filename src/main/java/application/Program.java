package application;

import enums.Status;
import model.Task;
import service.JsonReaderService;
import service.JsonWriterService;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class Program {
  public static void main(String[] args) throws IOException {
    JsonReaderService jsonReaderService = new JsonReaderService();
    List<Task> taskList = jsonReaderService.newOjbjectFromJsonFile("src/main/java/files/tasks.json");

    taskList.stream().forEach(System.out::println);

    taskList.add(new Task("Teste de nova tarefa", Status.TODO));

    taskList.add(new Task("Teste de nova tarefa", Status.IN_PROGRESS));
    JsonWriterService jsonWriterService = new JsonWriterService();
    jsonWriterService.writeToTheNewJsonFile(taskList);
  }
}
