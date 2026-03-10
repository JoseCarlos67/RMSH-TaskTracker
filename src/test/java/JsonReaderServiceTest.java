import enums.Status;
import model.Task;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import service.JsonReaderService;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class JsonReaderServiceTest {
  @Test
  @DisplayName("Deve retornar uma lista se o .json for valido")
  void shouldReturnTaskListWhenJsonFileIsValid() {
    JsonReaderService service = new JsonReaderService();
    String filePath = "src/test/java/files/tasks.json";

    List<Task> result = service.newOjbjectFromFileJson(filePath);

    assertNotNull(result, "A lista não deve ser nula para um .json válido!");
    assertEquals(2, result.size(), "A lista deve conter exatamente 2 tarefas!");

    Task task0 = result.get(0);
    assertEquals(0, task0.getId());
    assertEquals("Configurar ambiente Linux", task0.getDescription());
    assertEquals(Status.DONE,task0.getStatus());
    assertEquals(LocalDate.of(2026, 3, 8), task0.getCreatedAt());
    assertEquals(null, task0.getUpdateAt());
  }
}
