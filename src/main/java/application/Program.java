package application;

import service.TaskManager;

public class Program {
  public static void main(String[] args) {
    System.setProperty("file.encondig", "UTF-8");
    TaskManager.taskManager();
  }
}
