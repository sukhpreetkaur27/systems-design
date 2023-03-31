package com.design.leetcode.twitter;

class Tweet {

  private Integer id;
  private Integer time;
  private Tweet next;

  Tweet(Integer id, Integer time) {
    this.id = id;
    this.time = time;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public void setTime(Integer time) {
    this.time = time;
  }

  public Integer getTime() {
    return time;
  }

  public Tweet getNext() {
    return next;
  }

  public void setNext(Tweet next) {
    this.next = next;
  }

}
