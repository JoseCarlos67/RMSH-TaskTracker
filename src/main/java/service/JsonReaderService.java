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
  private final String folderPath = System.getProperty("user.home") + File.separator + ".rmsh-taskmanager";
  private final String fileName = "tasks.json";
  private final File file = new File(folderPath, fileName);

  public List<Task> listOfTaskInJsonFile() {

    File directory = new File(folderPath);
    if (!directory.exists()) {
      directory.mkdirs();
    }

    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());


    if (file.exists()){
      try {
        return objectMapper.readValue(file, new TypeReference<List<Task>>() {});
      } catch (IOException e) {
        System.out.println("Error reading the .json file: " + e.getMessage());
        return new ArrayList<>();
      }
    } else {
      return new ArrayList<>();
    }
  }
}
