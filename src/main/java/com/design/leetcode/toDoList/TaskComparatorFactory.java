package com.design.leetcode.toDoList;

import java.util.Comparator;

class TaskComparatorFactory {

  public static Comparator<Task> getComparatorByDueDate() {
    return (Task a, Task b) -> {
      return a.getDueDate() - b.getDueDate();
    };
  }

}
