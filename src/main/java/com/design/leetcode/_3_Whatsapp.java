package com.design.leetcode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Design a system like Whatsapp with the following features:

Send a message to a user.
Create a group with some initial users.
Add more users to a group.
Send a message to a group.
Get messages for a user.
Implement the WhatsApp class:

WhatsApp() Initializes the object.
void sendMessage(int toUser, String message) Sends a personal message with the text message to the user with id: toUser.
int createGroup(int[] initialUsers) Creates a new group that initially contains users whose ids are in the list initialUsers, and the group id is returned. For each group created, increment the ids sequentially. For the first group to be created id = 1, for the second group id = 2, and so on.
void addUserToGroup(int groupId, int userId) Adds the user with id: userId to the group with id: groupId. This call should be ignored if the user is already in that group, or if the group does not exist.
void sendGroupMessage(int fromUser, int groupId, String message) Sends a message with the text message by the user with id: fromUser to the group with id: groupId. The message should be sent to all members of the group except the sender. Users added afterwards to the group should not receive the message. Also, this call should be ignored if the user is not a part of the group, or if the group does not exist.
List<String> getMessagesForUser(int userId) Returns all the personal and group messages that were sent to the user with id: userId ordered by the latest ones first.
 

Example 1:

Input
["WhatsApp", "createGroup", "sendMessage", "sendMessage", "getMessagesForUser", "sendGroupMessage", "getMessagesForUser", "addUserToGroup", "sendGroupMessage", "getMessagesForUser", "getMessagesForUser"]
[[], [[1, 2, 3]], [2, "hellotwo"], [4, "hellofour"], [2], [1, 1, "helloeveryone"], [2], [1, 4], [1, 1, "seeyousoon"], [2], [4]]
Output
[null, 1, null, null, ["hellotwo"], null, ["helloeveryone", "hellotwo"], null, null, ["seeyousoon", "helloeveryone", "hellotwo"], ["seeyousoon", "hellofour"]]

Explanation
WhatsApp whatsApp = new WhatsApp();
whatsApp.createGroup([1, 2, 3]); // return 1
                                 // The first group is created containing the users [1,2,3].
                                 // Since it is the first group, its id will be 1.
whatsApp.sendMessage(2, "hellotwo"); // User 2 receives a personal message "hellotwo".
whatsApp.sendMessage(4, "hellofour"); // User 4 receives a personal message "hellofour".
whatsApp.getMessagesForUser(2); // return ["hellotwo"]
                                // User 2 only received the message "hellotwo" so far.
whatsApp.sendGroupMessage(1, 1, "helloeveryone"); // User 1 sends a message to group 1.
                                                  // So both users 2 and 3 receive the message.
whatsApp.getMessagesForUser(2); // return ["helloeveryone", "hellotwo"]
                                // Two messages were sent to user 2 so far.
whatsApp.addUserToGroup(1, 4); // User 4 is added to group 1.
whatsApp.sendGroupMessage(1, 1, "seeyousoon"); // User 1 sends a message to group 1.
                                               // So users 2, 3, and 4 receive the message.
whatsApp.getMessagesForUser(2); // return ["seeyousoon", "helloeveryone", "hellotwo"]
                                // Three messages were sent to user 2.
whatsApp.getMessagesForUser(4); // return ["seeyousoon", "hellofour"]
                               // Two messages were sent to user 4, so we return them.
                               // Note that user 4 did not receive the message "helloeveryone".
 

Constraints:

1 <= userId <= 1000
1 <= message.length <= 100
The ids in initialUsers are distinct.
At most 100 personal messages will be sent to each user.
At most 100 groups will be created.
At most 100 users will be in each group.
At most 50 messages will be sent to each group.
At most 100 calls will be made to getMessagesForUser.
message consists of only lowercase English letters.
 * 
 * @author sukh
 *
 */
public class _3_Whatsapp {

  private Integer group;
  private Map<Integer, Set<Integer>> groupUserMap;
  private Map<Integer, List<String>> userMsgMap;

  public _3_Whatsapp() {
    group = 0;
    groupUserMap = new ConcurrentHashMap<>();
    userMsgMap = new ConcurrentHashMap<>();
  }

  public void sendMessage(int toUser, String message) {
    List<String> msgs = userMsgMap.get(toUser);
    if (msgs == null) {
      msgs = new ArrayList<String>();
      userMsgMap.put(toUser, msgs);
    }
    msgs.add(message);
  }

  public int createGroup(int[] initialUsers) {
    group++;
    Set<Integer> users = Arrays.stream(initialUsers).boxed().collect(Collectors.toSet());
//    Set<Integer> users = new HashSet<>(Arrays.asList(initialUsers));
//    Collections.addAll(users, initialUsers);
    groupUserMap.put(group, users);
    return group;
  }

  public void addUserToGroup(int groupId, int userId) {
    Set<Integer> users = groupUserMap.get(groupId);
    if (users == null) {
      return;
    }
    users.add(userId);
    groupUserMap.put(groupId, users);
  }

  public void sendGroupMessage(int fromUser, int groupId, String message) {
    Set<Integer> users = groupUserMap.get(groupId);
    if (users == null) {
      return;
    }
    if (!users.contains(fromUser)) {
      return;
    }
    for (Integer user : users) {
      if (user != fromUser) {
        sendMessage(user, message);
      }
    }
  }

  public List<String> getMessagesForUser(int userId) {
    List<String> msgs = userMsgMap.get(userId);
    if (msgs == null) {
      return new ArrayList<>();
    }
    List<String> list = new ArrayList<>(msgs);
    Collections.reverse(list);
    return list;
  }

}
