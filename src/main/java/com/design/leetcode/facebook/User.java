package com.design.leetcode.facebook;

import java.util.HashSet;
import java.util.Set;

class User {

  private Integer id;
  private Set<Integer> friends;
  private Post postHead;

  User(Integer id) {
    this.id = id;
    this.friends = new HashSet<>();
  }

  public void addPost(Post post) {
    post.setNext(postHead);
    postHead = post;
  }

  public void addFriend(Integer user) {
    friends.add(user);
  }

  public Set<Integer> getFriends() {
    return friends;
  }

  public Post getPostHead() {
    return postHead;
  }

}
