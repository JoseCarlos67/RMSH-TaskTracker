package service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import model.Task;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class JsonWriterService {
  private final String folderPath = System.getProperty("user.home") + File.separator + ".rmsh-taskmanager";
  private final String fileName = "tasks.json";
  private final File file = new File(folderPath, fileName);

  public void updateJsonFile(List<Task> taskList) {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());

    try{
      File directory = new File(folderPath);
      if (!directory.exists()) {
        directory.mkdirs();
      }
      objectMapper.writeValue(file, taskList);
    } catch (IOException e) {
      System.out.println("❌ Error creating file: " + e.getMessage());
    }
  }
}
