package service;

import enums.Status;
import model.Task;
import org.jline.reader.Completer;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.UserInterruptException;
import org.jline.reader.impl.completer.StringsCompleter;
import org.jline.terminal.Attributes;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import javax.swing.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class TaskManager {
  public void taskManager() {
    try (Terminal terminal = TerminalBuilder.builder().build()) {
      Completer autoCompleter = new StringsCompleter("new", "list", "delete", "update", "exit");

      LineReader lineReader = LineReaderBuilder.builder()
              .terminal(terminal)
              .completer(autoCompleter)
              .build();


      String prompt = "task-tracker>";

      while (true) {
        String line;

        try {
          line = lineReader.readLine(prompt);

          String comand = line.trim().toLowerCase();

          switch (comand) {
            case "new":
              newTask(terminal, prompt, lineReader);
              break;
            case "list":
              listTasks();
              break;
            case "delete":
              deleteTask(terminal, lineReader, prompt);
              break;
          }
        } catch (UserInterruptException e) {
          terminal.writer().println("\nProcesso interrompido (ctrl+c)!");
        }
      }
    } catch (IOException e) {
      System.out.println("Erro: " + e.getMessage());
    }
  }

  private void newTask(Terminal terminal, String prompt, LineReader lineReader) {
    String description = lineReader.readLine(prompt + "Task description: ");
    try {
      Status status = showInteractiveStatusMenu(terminal);
      JsonWriterService jsonWriterService = new JsonWriterService();
      jsonWriterService.writeToTheNewJsonFile(new Task(description, status));
    } catch (UserInterruptException e) {
      terminal.writer().println(e.getMessage());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private void listTasks() throws IOException {
    JsonReaderService jsonReaderService = new JsonReaderService();
    List<Task> taskList = jsonReaderService.newOjbjectFromJsonFile("src/main/java/files/tasks.json");
    System.out.println();
    taskList.stream().forEach(System.out::println);
  }

  private void deleteTask(Terminal terminal, LineReader lineReader, String promt) {
    String idToRemove = lineReader.readLine(promt + "Digite o ID da tarefa que deseja deletar: ");
    int idToRemoveInt = Integer.parseInt(idToRemove);
    JsonReaderService jsonReaderService = new JsonReaderService();
    List<Task> taskList = jsonReaderService.newOjbjectFromJsonFile("src/main/java/files/tasks.json");

    taskList.removeIf(task -> task.getId() == idToRemoveInt);
    JsonWriterService jsonWriterService = new JsonWriterService();
    jsonWriterService.writeListToJsonFile(taskList);
  }

  public Status showInteractiveStatusMenu(Terminal terminal) throws IOException {
    Attributes originalAtributes = terminal.enterRawMode();

    try {
      int selectionIndex = 0;
      boolean firstRender = true;

      terminal.writer().println("\r\n");

      List<String> options = Arrays.asList(
              "TODO",
              "IN_PROGRESS",
              "DONE"
      );

      terminal.writer().print("Task status: \r\n");

      while (true) {
        if (!firstRender) {
          terminal.writer().print("\u001b[" + options.size() + "A");
        }
        firstRender = false;

        for (int i = 0; i < options.size(); i++) {
          terminal.writer().print("\u001B[2K\r");

          if (i == selectionIndex) {
            terminal.writer().print(" > \u001B[32m" + options.get(i) + "\u001B[0m\r\n");
          } else {
            terminal.writer().print("   " + options.get(i) + "\r\n");
          }
        }
        terminal.writer().flush();

        int c = terminal.reader().read();

        if (c == 3) { // 3 é o código ASCII para Ctrl+c
          throw new UserInterruptException("Menu cancelado pelo usuário!");
        } else if (c == 13 || c == 10) { // são códigos para o Enter
          return Status.valueOf(options.get(selectionIndex));
        } else if (c == 27) { // 27 é a tecla esc (início de uma sequência de setas)
          int next1 = terminal.reader().read();
          int next2 = terminal.reader().read();

          if (next1 == '[') {
            if (next2 == 'A') { // Seta para cima
              selectionIndex = Math.max(0, selectionIndex - 1);
            } else if (next2 == 'B') { // Seta para baixo
              selectionIndex = Math.min(options.size() - 1, selectionIndex + 1);
            }
          }
        }
      }
    } finally {
      // 4. MUITO IMPORTANTE: Sempre devolve o terminal ao estado normal ao sair
      terminal.setAttributes(originalAtributes);
    }
  }
}
