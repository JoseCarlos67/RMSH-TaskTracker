package model;

import enums.Status;

import java.time.LocalDate;

public class Task {
  private static int id;
  private String description;
  private Status status;
  private final LocalDate createdAt;
  private LocalDate updateAt;

  public Task(String description, Status status, LocalDate createdAt) {
    id += 1;
    this.description = description;
    this.status = status;
    this.createdAt = createdAt;
  }

  public static int getId() {
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
