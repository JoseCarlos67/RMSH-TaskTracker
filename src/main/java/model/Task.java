package model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import enums.Status;

import java.time.LocalDate;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Task {

  private static Integer idController = 1;

  private Integer id;
  private String description;
  private Status status;
  private LocalDate createdAt;
  private LocalDate updateAt;


  public Task(String description, Status status) {
    id = idController;
    idController++;
    this.description = description;
    this.status = status;
    this.createdAt = LocalDate.now();
  }

  @JsonCreator
  public Task(@JsonProperty("description") String description,
              @JsonProperty("status") Status status,
              @JsonProperty("createdAt") LocalDate createdAt,
              @JsonProperty("updateAt") LocalDate updateAt) {
    id = idController;
    idController++;
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

  public Integer getId() {
    return id;
  }

  public String getDescription() {
    return description;
  }

  public void updateDescription(String newDescription) {
    description = newDescription;
    updateAt = LocalDate.now();
  }

  public void updateStatus(Status newStatus) {
    status = newStatus;
    updateAt = LocalDate.now();
  }

  public Status getStatus() {
    return status;
  }

  public LocalDate getCreatedAt() {
    return createdAt;
  }

  public LocalDate getUpdateAt() {
    return updateAt;
  }
}
