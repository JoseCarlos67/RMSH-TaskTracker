package application;

import enums.Status;
import model.Task;
import service.JsonReaderService;

import java.io.IOException;
import java.util.List;

public class Program {
  public static void main(String[] args) throws IOException {
    JsonReaderService jsonReaderService = new JsonReaderService();
    List<Task> taskList = jsonReaderService.newOjbjectFromFileJson("src/main/java/files/tasks.json");

    taskList.stream().forEach(System.out::println);
  }
}
