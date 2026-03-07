package application;

import enums.Status;
import model.Task;

public class Program {
  public static void main(String[] args) {
    Task task = new Task("task 1", Status.TODO);
    Task task1 = new Task("task 2", Status.TODO);
    Task task2 = new Task("task 3", Status.TODO);

    System.out.println(task);
    System.out.println(task1);
    System.out.println(task2);
  }
}
