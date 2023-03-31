package com.design.leetcode.twitter;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

/**
 * Design a simplified version of Twitter where users can post tweets, follow/unfollow another user, and is able to see the 10 most recent tweets in the user's news feed.

Implement the Twitter class:

Twitter() Initializes your twitter object.
void postTweet(int userId, int tweetId) Composes a new tweet with ID tweetId by the user userId. Each call to this function will be made with a unique tweetId.
List<Integer> getNewsFeed(int userId) Retrieves the 10 most recent tweet IDs in the user's news feed. Each item in the news feed must be posted by users who the user followed or by the user themself. Tweets must be ordered from most recent to least recent.
void follow(int followerId, int followeeId) The user with ID followerId started following the user with ID followeeId.
void unfollow(int followerId, int followeeId) The user with ID followerId started unfollowing the user with ID followeeId.
 

Example 1:

Input
["Twitter", "postTweet", "getNewsFeed", "follow", "postTweet", "getNewsFeed", "unfollow", "getNewsFeed"]
[[], [1, 5], [1], [1, 2], [2, 6], [1], [1, 2], [1]]
Output
[null, null, [5], null, null, [6, 5], null, [5]]

Explanation
Twitter twitter = new Twitter();
twitter.postTweet(1, 5); // User 1 posts a new tweet (id = 5).
twitter.getNewsFeed(1);  // User 1's news feed should return a list with 1 tweet id -> [5]. return [5]
twitter.follow(1, 2);    // User 1 follows user 2.
twitter.postTweet(2, 6); // User 2 posts a new tweet (id = 6).
twitter.getNewsFeed(1);  // User 1's news feed should return a list with 2 tweet ids -> [6, 5]. Tweet id 6 should precede tweet id 5 because it is posted after tweet id 5.
twitter.unfollow(1, 2);  // User 1 unfollows user 2.
twitter.getNewsFeed(1);  // User 1's news feed should return a list with 1 tweet id -> [5], since user 1 is no longer following user 2.
 

Constraints:

1 <= userId, followerId, followeeId <= 500
0 <= tweetId <= 104
All the tweets have unique IDs.
At most 3 * 104 calls will be made to postTweet, getNewsFeed, follow, and unfollow.
 * 
 * @author sukh
 *
 */
public class Twitter {

  private static int timestamp = 0;

  private Map<Integer, User> users;

  public Twitter() {
    this.users = new HashMap<>();
  }

  public void postTweet(int userId, int tweetId) {
    User user = users.get(userId);
    if (user == null) {
      user = new User(userId);
      users.put(userId, user);
    }
    synchronized (Twitter.class) {
      timestamp++;
    }
    user.post(tweetId, timestamp);
  }

  public List<Integer> getNewsFeed(int userId) {
    User user = users.get(userId);
    List<Integer> tweets = new LinkedList<>();
    if (user == null) {
      return tweets;
    }
    Set<Integer> following = user.getFollowing();
    PriorityQueue<Tweet> queue = new PriorityQueue<>(following.size(),
        new Comparator<Tweet>() {
          public int compare(Tweet a, Tweet b) {
            return b.getTime() - a.getTime();
          }
        });
    for (Integer id : following) {
      Tweet tweetHead = users.get(id).getTweetHead();
      if (tweetHead != null) {
        queue.offer(tweetHead);
      }
    }
    int i = 0;
    while (i < 10 && !queue.isEmpty()) {
      Tweet tweet = queue.poll();
      tweets.add(tweet.getId());
      i++;
      if (tweet.getNext() != null) {
        queue.offer(tweet.getNext());
      }
    }
    return tweets;
  }

  public void follow(int followerId, int followeeId) {
    User follower = users.get(followerId);
    if (follower == null) {
      follower = new User(followerId);
      users.put(followerId, follower);
    }
    User followee = users.get(followeeId);
    if (followee == null) {
      followee = new User(followeeId);
      users.put(followeeId, followee);
    }
    follower.follow(followeeId);
  }

  public void unfollow(int followerId, int followeeId) {
    User follower = users.get(followerId);
    User followee = users.get(followeeId);
    if (follower == null || followee == null || followeeId == followerId) {
      return;
    }
    follower.unfollow(followeeId);
  }

}
