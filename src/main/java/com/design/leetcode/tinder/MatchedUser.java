package com.design.leetcode.tinder;

class MatchedUser {

  private Integer id;
  private Integer commonInterests;

  MatchedUser() {

  }

  MatchedUser(Integer id) {
    this.id = id;
    commonInterests = 0;
  }

  public Integer getId() {
    return this.id;
  }

  public Integer getCommonInterests() {
    return commonInterests;
  }

  public void addCommonInterest() {
    commonInterests++;
  }

}
