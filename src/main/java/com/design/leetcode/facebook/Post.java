package com.design.leetcode.facebook;

class Post {

  private Integer id;
  private String post;
  private Post next;

  Post(Integer id, String post) {
    this.id = id;
    this.post = post;
  }

  String getPost() {
    return post;
  }

  Integer getId() {
    return id;
  }

  Post getNext() {
    return next;
  }

  void setNext(Post next) {
    this.next = next;
  }

}
