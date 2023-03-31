package com.design.leetcode.toDoList;

import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

class User {

  private Integer id;
  private Map<Integer, Task> tasks;
  private Map<Tag, Set<Task>> tagsTasks;

  public User() {

  }

  public User(Integer id) {
    this.id = id;
    this.tasks = new ConcurrentHashMap<>();
    this.tagsTasks = new ConcurrentHashMap<>();
  }

  public void addTask(Task task) {
    tasks.put(task.getId(), task);
    tagTask(task);
  }

  private void tagTask(Task task) {
    for (String tag : task.getTags()) {
      Set<Task> tasks = this.tagsTasks.getOrDefault(new Tag(tag), new HashSet<>());
      tasks.add(task);
      this.tagsTasks.put(new Tag(tag), tasks);
    }
  }

  public boolean checkTaskExists(int taskId) {
    return tasks.containsKey(taskId);
  }

  public Task getTask(int taskId) {
    return tasks.get(taskId);
  }

  public Map<Integer, Task> getTasks() {
    return this.tasks;
  }

  public Map<Tag, Set<Task>> getTagsTasks() {
    return this.tagsTasks;
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
    User other = (User) obj;
    return Objects.equals(id, other.id);
  }

}
