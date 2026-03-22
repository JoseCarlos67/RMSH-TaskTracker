package service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import model.Task;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class JsonWriterService {
  String pathFile = "src/main/java/files/tasks.json";

  public void writeToTheNewJsonFile(Task task) {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());

    try{
      objectMapper.writeValue(new File(pathFile), task);
    } catch (IOException e) {
      System.out.println("Erro ao criar o arquivo: " + e.getMessage());
    }
  }

  public void writeListToJsonFile(List<Task> taskList) {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());

    try {
      objectMapper.writeValue(new File(pathFile), taskList);
    } catch (IOException e) {
      System.out.println("Erro ao criar o arquivo: " + e.getMessage());
    }
  }
}
