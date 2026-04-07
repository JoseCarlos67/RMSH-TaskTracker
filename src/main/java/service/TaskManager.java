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
import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;

import javax.swing.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class TaskManager {
  private static String filePath = "src/main/java/files/tasks.json";
  private static JsonReaderService jsonReaderService = new JsonReaderService();

  public void taskManager() {
    try (Terminal terminal = TerminalBuilder.builder().build()) {
      Completer autoCompleter = new StringsCompleter("new", "list", "delete", "update", "exit");

      LineReader lineReader = LineReaderBuilder.builder()
              .terminal(terminal)
              .completer(autoCompleter)
              .build();


      String prompt = new AttributedStringBuilder()
              .append("\n task ", AttributedStyle.DEFAULT.background(AttributedStyle.BLUE).foreground(AttributedStyle.WHITE))
              .append("\uE0B0 ", AttributedStyle.DEFAULT.foreground(AttributedStyle.BLUE))
              .append("❯ ", AttributedStyle.BOLD.foreground(AttributedStyle.YELLOW))
              .toAnsi();

      while (true) {
        String line;

        try {
          line = lineReader.readLine(prompt);

          String command[] = line.trim().split("\\s+");

          switch (command[0]) {
            case "new":
              newTask(terminal, prompt, lineReader);
              break;
            case "list":
              listTasks(terminal);
              break;
            case "delete":
              deleteTask(command, terminal);
              break;
            case "update":
              updateTask(terminal, prompt, lineReader, command);
              break;
            case "exit":
              System.exit(0);
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
      List<Task> taskList = jsonReaderService.listOfTaskInJsonFile(filePath);
      JsonWriterService jsonWriterService = new JsonWriterService();
      Task newTask = new Task(description, status);
      taskList.add(newTask);
      jsonWriterService.updateJsonFile(taskList);
    } catch (UserInterruptException e) {
      terminal.writer().println(e.getMessage());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private void listTasks(Terminal terminal) throws IOException {
    List<Task> taskList = jsonReaderService.listOfTaskInJsonFile(filePath);
    terminal.writer().println(new AttributedStringBuilder()
            .append(String.format("\n %-4s | %-25s | %-12s", "ID", "DESCRIÇÃO", "STATUS")
              , AttributedStyle.DEFAULT.italic().foreground(AttributedStyle.BRIGHT))
            .toAnsi());
    terminal.writer().println("━".repeat(50));

    for (Task t : taskList) {
      String statusColor = switch (t.getStatus()) {
        case DONE -> "\u001B[32m"; // Verde
        case IN_PROGRESS -> "\u001B[34m"; // Azul
        default -> "\u001B[37m"; // Branco
      };

      terminal.writer().printf(" #%03d | %-25s | %s%s\u001B[0m\n",
              t.getId(), t.getDescription(), statusColor, t.getStatus());
    }
    terminal.writer().flush();
  }

  private void deleteTask(String[] command, Terminal terminal) {
    if (command.length == 2) {
      int idToRemoveInt = Integer.parseInt(command[2]);
      List<Task> taskList = jsonReaderService.listOfTaskInJsonFile(filePath);

      taskList.removeIf(task -> task.getId() == idToRemoveInt);
      JsonWriterService jsonWriterService = new JsonWriterService();
      jsonWriterService.updateJsonFile(taskList);
    } else {
      terminal.writer().println("Error: this command requires 1 parameter!");
    }
  }

  private void updateTask(Terminal terminal, String prompt, LineReader lineReader, String[] command) {
    if (command.length == 3){
      int taskId = Integer.parseInt(command[2]);
      List<Task> taskList = jsonReaderService.listOfTaskInJsonFile(filePath);
      Task taskToUpdate = taskList.stream().filter(t -> t.getId().equals(taskId)).findFirst().orElse(null);

      if (taskToUpdate != null) {
        if (Objects.equals(command[1], "description")) {
          String newDescription = lineReader.readLine(prompt + " New description: ", null, taskToUpdate.getDescription());
          taskToUpdate.updateDescription(newDescription);
        } else if (Objects.equals(command[1], "status")) {
          try {
            Status newStatus = showInteractiveStatusMenu(terminal);
            taskToUpdate.updateStatus(newStatus);
          } catch (UserInterruptException e) {
            terminal.writer().println(e.getMessage());
          } catch (IOException e) {
            throw new RuntimeException(e);
          }
        } else {
          terminal.writer().println("Command " + command[1] + " not found");
        }

        JsonWriterService jsonWriterService = new JsonWriterService();
        jsonWriterService.updateJsonFile(taskList);
      } else {
        terminal.writer().println("ID not found!");
      }
    } else {
      terminal.writer().println("Error: this command requires 2 parameters!");
    }
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
          String label = options.get(i);
          String icon = switch (label) {
            case "TODO" -> "\u26AA";
            case "IN_PROGRESS" -> "\uD83D\uDD35";
            case "DONE" -> "\u2705";
            default -> "\uD83D\uDCCB";
          };
          if (i == selectionIndex) {
            terminal.writer().print(" ➜ " + icon + "\u001B[32;1m" + label + "\u001B[0m\r\n");
          } else {
            terminal.writer().print("   " + icon + label + "\r\n");
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
