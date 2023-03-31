package com.design.leetcode.twitter;

import java.util.HashSet;
import java.util.Set;

class User {

  private Integer id;
  private Set<Integer> following;
  private Tweet tweetHead;

  User(Integer id) {
    this.id = id;
    this.following = new HashSet<>();
    following.add(id);
  }

  Integer getId() {
    return id;
  }

  Set<Integer> getFollowing() {
    return following;
  }

  Tweet getTweetHead() {
    return tweetHead;
  }

  void follow(Integer id) {
    following.add(id);
  }

  void unfollow(Integer id) {
    following.remove(id);
  }

  void post(Integer id, Integer timestamp) {
    Tweet tweet = new Tweet(id, timestamp);
    tweet.setNext(tweetHead);
    tweetHead = tweet;
  }

}
