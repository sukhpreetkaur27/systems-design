package com.design.leetcode.facebook;

public class FacebookApp {

  public static void main(String[] args) {
    Facebook fb = new Facebook();
    fb.addFriend(1, 2);
    fb.writePost(1, "postone");
    fb.writePost(2, "posttwo");
    fb.writePost(3, "postthree");
    fb.writePost(2, "postfour");
    System.out.println(fb.showPosts(2));
    fb.showPosts(3);
    fb.addFriend(2, 3);
    fb.showPosts(1);
    fb.showPosts(2);
    fb.showPosts(3);
  }

}
