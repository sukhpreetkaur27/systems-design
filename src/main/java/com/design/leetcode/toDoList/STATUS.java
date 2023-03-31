package com.design.leetcode.toDoList;

enum STATUS {
  COMPLETED(1), PENDING(0);

  private int status;

  private STATUS(int status) {
    this.status = status;
  }

  public int getStatus() {
    return this.status;
  }
}