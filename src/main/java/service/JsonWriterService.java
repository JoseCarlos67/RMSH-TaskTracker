package service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import model.Task;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JsonWriterService {
  String pathFile = "src/main/java/files/tasks.json";

  public void updateJsonFile(List<Task> taskList) {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());

    try{
      objectMapper.writeValue(new File(pathFile), taskList);
    } catch (IOException e) {
      System.out.println("Erro ao criar o arquivo: " + e.getMessage());
    }
  }
}
