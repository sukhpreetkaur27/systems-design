package com.design.leetcode.facebook;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Design a system like Facebook with the following features:

A user can write a post.
Two users can become friends with each other.
Users can see all the posts written by their friends.
Implement the Facebook class:

Facebook() Initializes the object.
void writePost(int userId, String postContent) The user with id userId writes a post with the content postContent.
void addFriend(int user1, int user2) user1 and user2 become friends with each other. This call should be ignored if user1 and user2 are already friends.
List<String> showPosts(int userId) Returns all the posts made by the friends of the user with id userId ordered by the latest ones first, including ones made before they became friends. Note that the posts made by user userId should not be returned.
 

Example 1:

Input
["Facebook", "addFriend", "writePost", "writePost", "writePost", "writePost", "showPosts", "showPosts", "addFriend", "showPosts", "showPosts", "showPosts"]
[[], [1, 2], [1, "postone"], [2, "posttwo"], [3, "postthree"], [2, "postfour"], [2], [3], [2, 3], [1], [2], [3]]
Output
[null, null, null, null, null, null, ["postone"], [], null, ["postfour", "posttwo"], ["postthree", "postone"], ["postfour", "posttwo"]]

Explanation
Facebook facebook = new Facebook();
facebook.addFriend(1, 2); // Users 1 and 2 become friends.
facebook.writePost(1, "postone"); // "postone" is posted by user 1.
facebook.writePost(2, "posttwo"); // "posttwo" is posted by user 2.
facebook.writePost(3, "postthree"); // "postthree" is posted by user 3.
facebook.writePost(2, "postfour"); // "postfour" is posted by user 2.
facebook.showPosts(2); // return ["postone"]
                       // User 2 has only one friend, which is user 1 who has posted one time so far.
facebook.showPosts(3); // return []
                       // User 3 does not have any friends yet, so we return [].
facebook.addFriend(2, 3); // Users 2 and 3 become friends.
facebook.showPosts(1); // return ["postfour", "posttwo"]
                       // The only friend of user 1 is user 2 who has two posts, so we return them.
facebook.showPosts(2); // return ["postthree", "postone"]
                       // Users 1 and 3 are the friends of user 2.
                       // Both users 1 and 3 have one post each, but user 3 posted last,
                       // so we return user 3's post first.
facebook.showPosts(3); // return ["postfour", "posttwo"]
                       // The only friend of user 3 is user 2 who has two posts.
 

Constraints:

1 <= userId <= 1000
1 <= postContent.length <= 100
user1 != user2
postContent consists of lowercase English letters.
At most 100 calls will be made to writePost.
At most 1000 calls will be made to addFriend.
At most 100 calls will be made to showPosts.
 * 
 * @author sukh
 *
 */
public class Facebook {

  private static Integer postId;
  static {
    postId = 0;
  }
  private Map<Integer, User> users;

  public Facebook() {
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
    user_1.addFriend(user2);
    user_2.addFriend(user1);
  }

  public List<String> showPosts(int userId) {
    List<String> list = new ArrayList<>();
    User user = users.get(userId);
    if (user != null) {
      Queue<Post> posts = new PriorityQueue<>((Post a, Post b) -> {
        return b.getId() - a.getId();
      });
      for (Integer friend : user.getFriends()) {
        Post post = users.get(friend).getPostHead();
        while (post != null) {
          posts.add(post);
          post = post.getNext();
        }
      }
      while (!posts.isEmpty()) {
        Post post = posts.poll();
        list.add(post.getPost());
      }
    }

    return list;
  }

}
