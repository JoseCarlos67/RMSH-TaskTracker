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
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;
import org.jline.utils.InfoCmp;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class TaskManager {
  private static JsonReaderService jsonReaderService = new JsonReaderService();

  public static void taskManager() {
    try (Terminal terminal = TerminalBuilder.builder().build()) {
      terminal.puts(InfoCmp.Capability.clear_screen);
      terminal.flush();

      terminal.writer().println("\u001B[35m" + // Cor Magenta
              "  _____              _      __  __                                \n" +
              " |_   _|_ _ ___ | | __ |  \\/  | __ _ _ __   __ _  __ _  ___ _ __ \n" +
              "   | |/ _` / __|| |/ / | |\\/| |/ _` | '_ \\ / _` |/ _` |/ _ \\ '__|\n" +
              "   | | (_| \\__ \\|   <  | |  | | (_| | | | | (_| | (_| |  __/ |   \n" +
              "   |_|\\__,_|___/|_|\\_\\ |_|  |_|\\__,_|_| |_|\\__,_|\\__, |\\___|_|   \n" +
              "                                                  |___/           " +
              "\u001B[0m");

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
          default:
            AttributedString error = new AttributedStringBuilder()
                    .append("\n\uD83D\uDEAB", AttributedStyle.DEFAULT)
                    .append("  Error: command " + command[0] + " not found!", AttributedStyle.DEFAULT.foreground(AttributedStyle.RED).bold())
                    .toAttributedString();
            terminal.writer().println(error.toAnsi());
        }
      }
    } catch (IOException e) {
      System.out.println("Erro: " + e.getMessage());
    }
  }

  private static void newTask(Terminal terminal, String prompt, LineReader lineReader) {
    try {
      String descPrompt = new AttributedStringBuilder()
              .append(" 📝 Description: ", AttributedStyle.DEFAULT.foreground(AttributedStyle.WHITE))
              .toAnsi();
      String description = lineReader.readLine(descPrompt);

      if (description == null || description.trim().isEmpty()) {
        terminal.writer().println("\u001B[91m ❌ The description cannot be empty!\u001B[0m");
        return;
      }

      Status status = showInteractiveStatusMenu(terminal);

      List<Task> taskList = jsonReaderService.listOfTaskInJsonFile();
      if(taskList == null) {
        taskList = new ArrayList<>();
      }
      JsonWriterService jsonWriterService = new JsonWriterService();

      Task newTask = new Task(description, status);
      taskList.add(newTask);

      jsonWriterService.updateJsonFile(taskList);

      terminal.writer().println(new AttributedStringBuilder()
              .append("\n ✅ Task successfully registered!", AttributedStyle.BOLD.foreground(AttributedStyle.GREEN))
              .toAnsi());
      terminal.writer().flush();
    } catch (UserInterruptException e) {
      terminal.writer().println(e.getMessage());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private static void listTasks(Terminal terminal) throws IOException {
    List<Task> taskList = jsonReaderService.listOfTaskInJsonFile();
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

  private static void deleteTask(String[] command, Terminal terminal) {
    if (command.length == 2) {
      int idToRemoveInt = Integer.parseInt(command[1]);
      List<Task> taskList = jsonReaderService.listOfTaskInJsonFile();

      if (taskList.removeIf(task -> task.getId() == idToRemoveInt)) {
        JsonWriterService jsonWriterService = new JsonWriterService();
        jsonWriterService.updateJsonFile(taskList);
        terminal.writer().println(new AttributedStringBuilder()
                .append("\n ✅ Task successfully deleted!", AttributedStyle.BOLD.foreground(AttributedStyle.GREEN))
                .toAnsi());
      } else {
        AttributedString error = new AttributedStringBuilder()
                .append("\n\uD83D\uDEAB", AttributedStyle.DEFAULT)
                .append("  Error: ID not found!", AttributedStyle.DEFAULT.foreground(AttributedStyle.RED).bold())
                .toAttributedString();
        terminal.writer().println(error.toAnsi());
      }


    } else {
      AttributedString error = new AttributedStringBuilder()
              .append("\n\uD83D\uDEAB", AttributedStyle.DEFAULT)
              .append("  Error: this command requires 1 parameter!", AttributedStyle.DEFAULT.foreground(AttributedStyle.RED).bold())
              .toAttributedString();
      terminal.writer().println(error.toAnsi());
    }
  }

  private static void updateTask(Terminal terminal, String prompt, LineReader lineReader, String[] command) {
    if (command.length == 3) {
      int taskId = Integer.parseInt(command[2]);
      List<Task> taskList = jsonReaderService.listOfTaskInJsonFile();

      if (Objects.equals(command[1], "description")) {
        Task taskToUpdate = taskList.stream().filter(t -> t.getId().equals(taskId)).findFirst().orElse(null);
        if (taskToUpdate != null) {
          String descPrompt = new AttributedStringBuilder()
                  .append("\n 📝 Description: ", AttributedStyle.DEFAULT.foreground(AttributedStyle.WHITE))
                  .toAnsi();
          String newDescription = lineReader.readLine(descPrompt);
          taskToUpdate.updateDescription(newDescription);
          terminal.writer().println(new AttributedStringBuilder()
                  .append("\n ✅ Task successfully updated!", AttributedStyle.BOLD.foreground(AttributedStyle.GREEN))
                  .toAnsi());
          terminal.writer().flush();

          if (newDescription == null || newDescription.trim().isEmpty()) {
            terminal.writer().println("\u001B[91m ❌ The description cannot be empty!\u001B[0m");
            return;
          }
        } else {
          AttributedString error = new AttributedStringBuilder()
                  .append("\n❌", AttributedStyle.DEFAULT)
                  .append("  Error: ID not found!", AttributedStyle.DEFAULT.foreground(AttributedStyle.RED).bold())
                  .toAttributedString();
          terminal.writer().println(error.toAnsi());
        }
      } else if (Objects.equals(command[1], "status")) {
        Task taskToUpdate = taskList.stream().filter(t -> t.getId().equals(taskId)).findFirst().orElse(null);
        if (taskToUpdate != null) {
          try {
            Status newStatus = showInteractiveStatusMenu(terminal);
            taskToUpdate.updateStatus(newStatus);
          } catch (UserInterruptException e) {
            terminal.writer().println(e.getMessage());
          } catch (IOException e) {
            throw new RuntimeException(e);
          }
        } else {
          AttributedString error = new AttributedStringBuilder()
                  .append("\n❌", AttributedStyle.DEFAULT)
                  .append("  Error: ID not found!", AttributedStyle.DEFAULT.foreground(AttributedStyle.RED).bold())
                  .toAttributedString();
          terminal.writer().println(error.toAnsi());
        }

      } else {
        AttributedString error = new AttributedStringBuilder()
                .append("\n\uD83D\uDEAB", AttributedStyle.DEFAULT)
                .append("  Error: command " + command[1] + " not found!", AttributedStyle.DEFAULT.foreground(AttributedStyle.RED).bold())
                .toAttributedString();
        terminal.writer().println(error.toAnsi());
      }

      JsonWriterService jsonWriterService = new JsonWriterService();
      jsonWriterService.updateJsonFile(taskList);
    } else {
      AttributedString error = new AttributedStringBuilder()
              .append("\n\uD83D\uDEAB", AttributedStyle.DEFAULT)
              .append("  Error: this command requires 2 parameters!", AttributedStyle.DEFAULT.foreground(AttributedStyle.RED).bold())
              .toAttributedString();
      terminal.writer().println(error.toAnsi());
    }
  }

  private static Status showInteractiveStatusMenu(Terminal terminal) throws IOException {
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
