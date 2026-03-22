package service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import model.Task;

import java.io.File;
import java.io.IOException;

public class JsonWriterService {
  public void writeToTheNewJsonFile(Task task) {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());

    try{
      objectMapper.writeValue(new File("src/main/java/files/newtasks.json"), task);
    } catch (IOException e) {
      System.out.println("Erro ao criar o arquivo: " + e.getMessage());
    }
  }
}
