import enums.Status;
import model.Task;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class TaskTest {
  @Test
  @DisplayName("Deve criar uma tarefa com os valores iniciais corretos e updateAt nulo")
  void shouldCreateTaskWithCorrectInitialValues() {
    Task task = new Task("Configurar ambiente Linux", Status.TODO);

    assertEquals("Configurar ambiente Linux", task.getDescription());
    assertEquals(Status.TODO, task.getStatus());
    assertEquals(LocalDate.now(), task.getCreatedAt());
    assertNull(task.getUpdateAt(), "O campo updateAt deve ser nulo na criação!");
  }

  @Test
  @DisplayName("Deve atualizar a descrição de uma tarefa e updateAt com a data da atualização")
  void shouldUpdateDescriptionAndSetUpdateDate() {
    Task task = new Task("Configurar ambiente Linux", Status.TODO);
    task.updateDescription("Configurar ambiente Linux para programação");

    assertEquals("Configurar ambiente Linux para programação", task.getDescription());
    assertNotNull(task.getUpdateAt(), "O campo updateAt não deve ser nulo na atualização!");
    assertEquals(LocalDate.now(), task.getUpdateAt());
  }

  @Test
  @DisplayName("Deve atualizar o status da tarefa a atualizar a data de modificação")
  void shouldUpdateStatusAndSetUpdateDate() {
    Task task = new Task("Configurar ambiente Linux", Status.TODO);
    task.updateStatus(Status.DONE);

    assertEquals(Status.DONE, task.getStatus());
    assertNotNull(task.getUpdateAt(), "O campo updateAt não deve ser nulo na atualização");
    assertEquals(LocalDate.now(), task.getUpdateAt());
  }

  @Test
  @DisplayName("Deve incrementar o ID ao criar múltiplas tarefas")
  void shouldIncrementIdForMultiplesTasks() {
    Task task1 = new Task("Primeira tarefa", Status.TODO);
    Task task2 = new Task("Segunda tarefa", Status.DONE);

    assertTrue(task2.getId() > task1.getId(), "O ID da tarefa subsequente deve ser maior!");
  }
}
