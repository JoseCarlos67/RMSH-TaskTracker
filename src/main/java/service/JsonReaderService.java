package service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import model.Task;

import java.io.File;
import java.io.IOException;

public class JsonReaderService {

  public Task newOjbjectFromFileJson() throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());

    Task task = objectMapper.readValue(new File("/home/carlos/Development/Java/RoadMapSh/RMSH-TaskTracker/src/main/java/files/tasks.json"), Task.class);

    return task;
  }

}
