package model;

import enums.Status;

import java.time.LocalDate;

public class Task {
  private static int idControler = 0;

  private int id;
  private String description;
  private Status status;
  private final LocalDate createdAt;
  private LocalDate updateAt;

  public Task(String description, Status status) {
    id += idControler;
    idControler++;
    this.description = description;
    this.status = status;
    this.createdAt = LocalDate.now();
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

  public void setDescription(String newDescription) {
    description = newDescription;
    updateAt = LocalDate.now();
  }

  public Status getStatus() {
    return status;
  }

  public void setStatus(Status newStatus) {
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
