package application;

import service.TaskManager;

import java.io.IOException;

public class Program {
  public static void main(String[] args) throws IOException {
    TaskManager taskManager = new TaskManager();
    taskManager.taskManager();
  }
}
