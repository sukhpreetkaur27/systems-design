package com.design.leetcode.walnut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


/**
 * Design a banking SMS parsing and analytics application (like Walnut) which reads all the text messages received by users and analyzes the following:

The income and expenditure of a user.
The average income and expenditure of all users.
Text messages will be represented as a string, with words separated by ' '. The texts will be analyzed and will be considered valid if it satisfies the following criteria:

It contains words of exactly one of the following groups:
one or more of "credit", "credited", "deposit", or "deposited" to indicate an earning, or
one or more of "debit", "debited", "withdraw", "withdrawal", or "withdrawn" to indicate an expenditure.
It has exactly one occurrence of amount. It can be denoted as "USD x", "x USD", "USDx", "$ x", "x $", or "$x", where:
x denotes the denomination of the amount. It should lie in the range [0, 109]. Please note that it means "10000000000000000 USD" is an invalid amount.
x can have up to 2 decimal places. Please note that this means "$ 1.0005" is not a valid amount as it has more than 2 decimal places.
Design the Walnut class:

Walnut() Initializes the Walnut object with 0 users and 0 text messages.
void parseText(int userID, String text) Analyzes the text message represented as text, and updates it for the user userID if it is a valid text.
double getTotalUserEarnings(int userID) Returns the total earnings of user userID. In case no valid texts have been analyzed for userID, returns 0.
double getTotalUserExpenses(int userID) Returns the total expenses of user userID. In case no valid texts have been analyzed for userID, returns 0.
double getAverageUserEarnings() Returns the average earnings of all users whose texts have been analyzed and are valid (including users with only expenses), or 0 if no texts have been analyzed.
double getAverageUserExpenses() Returns the average expenses of all users whose texts have been analyzed and are valid (including users with only earnings), or 0 if no texts have been analyzed.
Note that all answers within 10-5 of the actual answer will be considered accepted.

 

Example 1:

Input
["Walnut", "getAverageUserEarnings", "parseText", "parseText", "parseText", "parseText", "parseText", "getTotalUserEarnings", "getTotalUserExpenses", "getAverageUserEarnings", "getAverageUserExpenses"]
[[], [], [1, "deposit $120.45"], [1, "credit $238.98"], [1, "Transaction Cost USD 25.55"], [1, "debited $14790"], [1, "deposited $1000.00"], [1], [1], [], []]
Output
[null, 0.0, null, null, null, null, null, 1359.43, 14790.0, 1359.43, 14790.0]

Explanation
Walnut walnut = new Walnut();    // initialize the object with 0 users and 0 parsed texts
walnut.getAverageUserEarnings(); // return 0.0
                                 // There are no users whose texts have been analyzed.
walnut.parseText(1, "deposit $120.45"); // $120.45 has been earned by user 1.
walnut.parseText(1, "credit $238.98");  // $238.98 has been earned by user 1, whose total earnings is $359.43.
walnut.parseText(1, "Transaction Cost USD 25.55"); // There is no clear indication whether it is debit or credit
                                                   // So this text is invalid
walnut.parseText(1, "debited $14790"); //
walnut.parseText(1, "deposited $1000.00"); // $1000 has been earned by user 1, whose total earnings is $1359.43.
walnut.getTotalUserEarnings(1);  // return 1359.43
                                 // This is the total amount earned by user 1.
walnut.getTotalUserExpenses(1);  // return 14790.0
                                 // This is the total amount expended by user 1.
walnut.getAverageUserEarnings(); // return 1359.43
                                 // Since the only analyzed texts have been for user 1, it returns their total earnings.
walnut.getAverageUserExpenses(); // return 14790.0
                                 // Since the only analyzed texts have been for user 1, it returns their total expenses.
 

Constraints:

1 <= userID <= 105
1 <= text.length <= 50
text consists of lowercase and uppercase English letters, digits ('0' - '9'), '$', ' ' and '.'.
At most 105 calls in total will be made to parseText, getTotalUserEarnings, getTotalUserExpenses, getAverageUserEarnings, and getAverageUserExpenses.
At least one call will be made to getTotalUserEarnings, getTotalUserExpenses, getAverageUserEarnings, or getAverageUserExpenses.
 * 
 * @author sukh
 *
 */
public class Walnut {

  private static final List<String> CREDIT_IDENTIFIERS = new ArrayList<>(
      Arrays.asList("credit", "credited", "deposit", "deposited"));
  private static final List<String> DEBIT_IDENTIFIERS = new ArrayList<>(
      Arrays.asList("debit", "debited", "withdraw", "withdrawal", "withdrawn"));

  private static final String USD = "USD";
  private static final String DOLLAR = "$";

  private double totalCredit;
  private double totalDebit;

  private Set<Integer> users;
  private Map<TXN_TYPE, Map<Integer, Double>> txns;

  public Walnut() {
    users = new HashSet<>();
    txns = new HashMap<>();
    txns.put(TXN_TYPE.CREDIT, new ConcurrentHashMap<>());
    txns.put(TXN_TYPE.DEBIT, new ConcurrentHashMap<>());
  }

  public void parseText(int userID, String text) {
    boolean isCredit = checkForCredit(text);
    boolean isDebit = checkForDebit(text);

    double amount = parseAmount(text);

    if (isCredit && !isDebit) {
      totalCredit += amount;
      Map<Integer, Double> userTxns = txns.get(TXN_TYPE.CREDIT);
      if (userTxns.get(userID) != null) {
        amount += userTxns.get(userID);
      }
      userTxns.put(userID, amount);
      users.add(userID);
    } else if (isDebit && !isCredit) {
      totalDebit += amount;
      Map<Integer, Double> userTxns = txns.get(TXN_TYPE.DEBIT);
      if (userTxns.get(userID) != null) {
        amount += userTxns.get(userID);
      }
      userTxns.put(userID, amount);
      users.add(userID);
    }
  }

  private double parseAmount(String text) {
    String[] words = text.split(" ");
    double amount = 0;
    for (int i = 0; i < words.length; i++) {
      /**
       * USD 40 (or) $ 40 (or) 40 USD (or) 40 $
       */
      if (USD.equals(words[i]) || DOLLAR.equals(words[i])) {
        /**
         * USD 40 (or) $ 40
         */
        if (i < words.length - 1) {
          if (isNumeric(words[i + 1])) {
            amount = parseNumber(words[i + 1]);
            return amount;
          }
        }
        /**
         * 40 USD (or) 40 $
         */
        else if (i > 0) {
          if (isNumeric(words[i - 1])) {
            amount = parseNumber(words[i - 1]);
            return amount;
          }
        }
      }
      /**
       * USD40 (or) $40 (or) 40USD (or) 40$
       */
      else if (words[i].contains(USD) || words[i].contains(DOLLAR)) {
        String num = words[i].replace(USD, "");
        num = num.replace(DOLLAR, "");
        if (isNumeric(num)) {
          amount = parseNumber(num);
          return amount;
        }
      }
    }
    return amount;
  }

  private double parseNumber(String num) {
    return Double.parseDouble(num);
  }

  private boolean isNumeric(String num) {
    try {
      Double.parseDouble(num);
      return true;
    } catch (NumberFormatException ex) {
      return false;
    }
  }

  private boolean checkForCredit(String text) {
    for (String word : CREDIT_IDENTIFIERS) {
      if (text.contains(word)) {
        return true;
      }
    }
    return false;
  }

  private boolean checkForDebit(String text) {
    for (String word : DEBIT_IDENTIFIERS) {
      if (text.contains(word)) {
        return true;
      }
    }
    return false;
  }

  public double getTotalUserEarnings(int userID) {
    Map<Integer, Double> userTxns = txns.get(TXN_TYPE.CREDIT);
    double userCredit = 0;
    if (userTxns.get(userID) != null) {
      userCredit = userTxns.get(userID);
    }
    return userCredit;
  }

  public double getTotalUserExpenses(int userID) {
    Map<Integer, Double> userTxns = txns.get(TXN_TYPE.DEBIT);
    double userDebit = 0;
    if (userTxns.get(userID) != null) {
      userDebit = userTxns.get(userID);
    }
    return userDebit;
  }

  public double getAverageUserEarnings() {
    if (users.size() > 0) {
      return totalCredit / users.size();
    }
    return 0;
  }

  public double getAverageUserExpenses() {
    if (users.size() > 0) {
      return totalDebit / users.size();
    }
    return 0;
  }

}
