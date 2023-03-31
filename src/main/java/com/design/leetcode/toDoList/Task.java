package com.design.leetcode.toDoList;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

class Task {

  private Integer id;
  private String description;
  private Integer dueDate;
  private List<String> tags;
  private STATUS status;

  public Task(Integer id, String description, Integer dueDate, List<String> tags) {
    this.id = id;
    this.description = description;
    this.dueDate = dueDate;
    this.tags = new ArrayList<>(tags);
    this.status = STATUS.PENDING;
  }

  public Integer getId() {
    return this.id;
  }

  public STATUS getStatus() {
    return this.status;
  }

  public void setStatus(STATUS status) {
    this.status = status;
  }

  public String getDescription() {
    return description;
  }

  public Integer getDueDate() {
    return this.dueDate;
  }

  public List<String> getTags() {
    return this.tags;
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Task other = (Task) obj;
    return Objects.equals(id, other.id);
  }
}
