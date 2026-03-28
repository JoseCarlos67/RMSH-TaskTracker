package service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import model.Task;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JsonReaderService {

  public List<Task> listOfTaskInJsonFile(String filePath) {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());

    File jsonFile = new File(filePath);

    if (jsonFile.exists()){
      try (BufferedReader bufferedReader = new BufferedReader(new FileReader(jsonFile))){
        List<Task> taskList = objectMapper.readValue(bufferedReader, new TypeReference<List<Task>>(){});
        return taskList;
      } catch (IOException e) {
        System.out.println("Erro na leitura do arquivo .json: " + e.getMessage());
        return new ArrayList<>();
      }

    } else {
      return new ArrayList<>();
    }
  }
}
