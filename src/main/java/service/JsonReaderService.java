package service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import model.Task;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class JsonReaderService {

  public Task newOjbjectFromFileJson() {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());

    try {
      Task task = objectMapper.readValue(new BufferedReader(new FileReader("src/main/java/files/tasks.json")), Task.class);
      return  task;
    } catch (IOException e) {
      System.out.println("Erro na leitura do arquivo .json: " + e.getMessage());
    }
    return null;
  }

}
