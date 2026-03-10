package service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import model.Task;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class JsonReaderService {

  public List<Task> newOjbjectFromFileJson(String filePath) {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());

    try {
      List<Task> taskList = objectMapper.readValue(new BufferedReader(new FileReader(filePath)), new TypeReference<List<Task>>(){});
      return taskList;
    } catch (IOException e) {
      System.out.println("Erro na leitura do arquivo .json: " + e.getMessage());
    }
    return null;
  }
}
