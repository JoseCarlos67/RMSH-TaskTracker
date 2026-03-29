import enums.Status;
import model.Task;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import service.JsonReaderService;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class JsonReaderServiceTest {
  @Test
  @DisplayName("Deve retornar uma lista se o .json for valido")
  void newObjectFromJsonFile_ShouldReturnTaskList_WhenJsonFileIsValid() {
    JsonReaderService service = new JsonReaderService();
    String filePath = "src/test/java/files/tasks.json";

    List<Task> result = service.listOfTaskInJsonFile(filePath);

    assertNotNull(result, "A lista não deve ser nula para um .json válido!");

    Task task0 = result.get(0);
    assertEquals(0, task0.getId());
    assertEquals("Configurar ambiente Linux", task0.getDescription());
    assertEquals(Status.DONE,task0.getStatus());
    assertEquals(LocalDate.of(2026, 3, 8), task0.getCreatedAt());
    assertNull(task0.getUpdateAt());
  }
}
