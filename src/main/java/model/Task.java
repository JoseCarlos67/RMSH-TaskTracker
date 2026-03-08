package model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import enums.Status;

import java.time.LocalDate;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Task {

  private static int idControler = 0;

  private int id;
  private String description;
  private Status status;
  private LocalDate createdAt;
  private LocalDate updateAt;

  public Task() {}

  public Task(String description, Status status) {
    id += idControler;
    idControler++;
    this.description = description;
    this.status = status;
    this.createdAt = LocalDate.now();
  }

  public Task(String description, Status status, LocalDate createdAt, LocalDate updateAt) {
    id += idControler;
    idControler++;
    this.description = description;
    this.status = status;
    this.createdAt = createdAt;
    this.updateAt = updateAt;
  }

  @Override
  public String toString() {
    return
            "id= " + id +
            "\ndescription=" + description +
            "\nstatus=" + status +
            "\ncreatedAt=" + createdAt +
            "\nupdateAt=" + updateAt +
            "\n";
  }

  public int getId() {
    return id;
  }

  public String getDescription() {
    return description;
  }

  public void updateDescription(String newDescription) {
    description = newDescription;
    updateAt = LocalDate.now();
  }

  public Status getStatus() {
    return status;
  }

  public void updateStatus(Status newStatus) {
    status = newStatus;
    updateAt = LocalDate.now();
  }

  public LocalDate getCreatedAt() {
    return createdAt;
  }

  public LocalDate getUpdateAt() {
    return updateAt;
  }
}
