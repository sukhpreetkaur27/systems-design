package com.design.leetcode.tinder;

import java.util.Comparator;

class MatchComparator implements Comparator<MatchedUser> {

  public int compare(MatchedUser a, MatchedUser b) {
    if (a.getCommonInterests() == b.getCommonInterests()) {
      return a.getId() - b.getId();
    }
    return b.getCommonInterests() - a.getCommonInterests();
  }

}
