package application;

import enums.Status;
import model.Task;
import service.JsonReaderService;

import java.io.IOException;

public class Program {
  public static void main(String[] args) throws IOException {
    JsonReaderService jsonReaderService = new JsonReaderService();
    Task task = jsonReaderService.newOjbjectFromFileJson();

    System.out.println(task);
  }
}
