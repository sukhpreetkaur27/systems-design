package com.design.leetcode.facebook;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class Facebook_Accepted {

  class Post {

    private Integer id;
    private String post;

    Post(Integer id, String post) {
      this.id = id;
      this.post = post;
    }

    public String getPost() {
      return post;
    }

    public Integer getId() {
      return id;
    }

  }

  class User {

    private Integer id;
    private List<Post> posts;
    private Set<User> friends;

    User(Integer id) {
      this.id = id;
      this.posts = new ArrayList<>();
      this.friends = new HashSet<>();
    }

    public void addPost(Post post) {
      posts.add(post);
    }

    public void addFriend(User user) {
      friends.add(user);
    }

    public Set<User> getFriends() {
      return friends;
    }

    public List<Post> getPosts() {
      return posts;
    }

  }

  private static Integer postId;
  static {
    postId = 0;
  }
  private Map<Integer, User> users;

  public Facebook_Accepted() {
    users = new ConcurrentHashMap<>();
  }

  public void writePost(int userId, String postContent) {
    User user = users.get(userId);
    if (user == null) {
      user = new User(userId);
      users.put(userId, user);
    }
    synchronized (Facebook.class) {
      postId++;
    }
    user.addPost(new Post(postId, postContent));
  }

  public void addFriend(int user1, int user2) {
    User user_1 = users.get(user1);
    if (user_1 == null) {
      user_1 = new User(user1);
      users.put(user1, user_1);
    }
    User user_2 = users.get(user2);
    if (user_2 == null) {
      user_2 = new User(user2);
      users.put(user2, user_2);
    }
    user_1.addFriend(user_2);
    user_2.addFriend(user_1);
  }

  public List<String> showPosts(int userId) {
    List<Post> posts = new ArrayList<>();
    User user = users.get(userId);
    if (user != null) {
      Set<User> friends = user.getFriends();
      for (User friend : friends) {
        for (Post post : friend.getPosts()) {
          posts.add(post);
        }
      }
    }
    Collections.sort(posts, (Post a, Post b) -> {
      return b.getId() - a.getId();
    });
    List<String> list = new ArrayList<>();
    for (Post post : posts) {
      list.add(post.getPost());
    }
    return list;
  }

}
