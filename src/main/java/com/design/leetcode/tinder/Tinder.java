package com.design.leetcode.tinder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Design a simple dating system like Tinder with the following features:

Register a user with their gender, age, preferences, and interests.
Find matching users according to their preferred gender, preferred age range, and common interests.
Implement the Tinder class:

Tinder() Initializes the object.
void signup(int userId, int gender, int preferredGender, int age, int minPreferredAge, int maxPreferredAge, List<String> interests) Registers a user with the given attributes.
List<Integer> getMatches(int userId) Returns the top 5 matches for the given user. The returned matches should satisfy the following:
The returned user's gender should equal the given user's preferredGender.
The returned user's age should be between the given user's minPreferredAge and maxPreferredAge (inclusive).
There should be at least 1 common interest between the returned user and the given user.
The results should be sorted in decreasing order by the number of common interests. If there is a tie, it should be sorted in increasing order by userId.
If there are fewer than 5 matches, return as many as possible.
Note that the given user might not necessarily be a match for the returned users.
 

Example 1:

Input
["Tinder", "getMatches", "signup", "getMatches", "signup", "getMatches", "getMatches", "signup", "signup", "getMatches", "signup", "getMatches", "getMatches", "signup", "getMatches", "getMatches"]
[[], [1], [1, 0, 1, 25, 20, 30, ["Singing", "Dancing", "Reading", "Skating"]], [1], [2, 1, 0, 27, 23, 30, ["Painting", "Basketball"]], [1], [2], [3, 1, 0, 25, 20, 30, ["Singing", "Skating"]], [4, 1, 0, 25, 20, 30, ["Singing", "Writing", "Coding"]], [1], [5, 1, 0, 31, 24, 37, ["Singing", "Dancing", "Reading", "Skating"]], [1], [5], [6, 0, 1, 30, 25, 33, ["Volleyball", "Skating"]], [5], [6]]
Output
[null, [], null, [], null, [], [], null, null, [3, 4], null, [3, 4], [1], null, [1, 6], [3, 5]]

Explanation
Tinder tinder = new Tinder();
tinder.getMatches(1); // return [], there is no user with userId 1.
tinder.signup(1, 0, 1, 25, 20, 30, ["Singing", "Dancing", "Reading", "Skating"]);
tinder.getMatches(1); // return [], no users besides user 1 have signed up yet.
tinder.signup(2, 1, 0, 27, 23, 30, ["Painting", "Basketball"]);
tinder.getMatches(1); // return [], user 2 has no common interests with user 1, so there are no matches.
tinder.getMatches(2); // return [], similarly, user 1 has no common interests with user 2.
tinder.signup(3, 1, 0, 25, 20, 30, ["Singing", "Skating"]);
tinder.signup(4, 1, 0, 25, 20, 30, ["Singing", "Writing", "Coding"]);
tinder.getMatches(1); // return [3, 4], both users 3 and 4 are in the age range for user 1 and
                      // are the preferred gender of user 1. Since user 3 has 2 common interests
                      // and user 4 has 1 common interest, user 3 is returned before user 4.
tinder.signup(5, 1, 0, 31, 24, 37, ["Singing", "Dancing", "Reading", "Skating"]);
tinder.getMatches(1); // return [3, 4], user 5 has an age larger than the maxPreferredAge of user 1.
tinder.getMatches(5); // return [1], user 1 has the preferred age, gender, and has 4 common interests.
tinder.signup(6, 0, 1, 30, 25, 33, ["Volleyball", "Skating"]);
tinder.getMatches(5); // return [1, 6], user 6 has the preferred age, gender, and has 1 common interest.
tinder.getMatches(6); // return [3, 5], users 3 and 5 both have the preferred age, gender,
                      // and have 1 common interest. Since they both have 1 common interest
                      // and 3 < 5, user 3 is returned before user 5.
 

Constraints:

1 <= userId <= 104
0 <= gender, preferredGender <= 1
18 <= age <= 90
18 <= minPreferredAge <= maxPreferredAge <= 90
1 <= interests.length <= 5
1 <= interests[i].length <= 20
interests[i] consists of lowercase and uppercase English letters and ' '.
All strings in interests are unique.
At most 1000 calls will be made to signup and getMatches.
userId will be unique across all calls made to signup.
 * 
 * @author sukh
 *
 */
public class Tinder {

  private Map<Integer, User> users;

  public Tinder() {
    users = new ConcurrentHashMap<>();
  }

  public void signup(int userId, int gender, int preferredGender, int age,
      int minPreferredAge, int maxPreferredAge, List<String> interests) {
    User user = new User(userId, Gender.getGender(gender),
        Gender.getGender(preferredGender), age, minPreferredAge, maxPreferredAge,
        interests);
    users.put(userId, user);
  }

  public List<Integer> getMatches(int userId) {
    List<Integer> result = new ArrayList<>();
    Queue<MatchedUser> pq = new PriorityQueue<>(5, new MatchComparator());
    User user = users.get(userId);
    if (user == null) {
      return result;
    }
    for (Map.Entry<Integer, User> entry : users.entrySet()) {
      User match = entry.getValue();
      if (user.getId() == match.getId()) {
        continue;
      }
      if (!preferredGenderMatch(user, match)) {
        continue;
      }
      if (!preferredAgeMatch(user, match)) {
        continue;
      }
      MatchedUser matched = matchInterests(user, match);
      if (matched.getCommonInterests() == 0) {
        continue;
      }
      pq.offer(matched);
    }

    int count = 0;
    while (count < 5 && !pq.isEmpty()) {
      MatchedUser match = pq.poll();
      result.add(match.getId());
      count++;
    }
    return result;
  }

  private MatchedUser matchInterests(User user, User match) {
    MatchedUser matched = new MatchedUser(match.getId());
    for (String interest : user.getInterests()) {
      if (match.getInterests().contains(interest)) {
        matched.addCommonInterest();
      }
    }
    return matched;
  }

  private boolean preferredGenderMatch(User user, User match) {
    if (user.getPreferredGender() == match.getGender()) {
      return true;
    }
    return false;
  }

  private boolean preferredAgeMatch(User user, User match) {
    if (match.getAge() >= user.getMinPreferredAge()
        && match.getAge() <= user.getMaxPreferredAge()) {
      return true;
    }
    return false;
  }

}
