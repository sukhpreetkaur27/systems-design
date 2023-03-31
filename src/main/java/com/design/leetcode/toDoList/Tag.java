package com.design.leetcode.toDoList;

import java.util.Objects;

class Tag {

  private String tag;

  public Tag(String tag) {
    this.tag = tag;
  }

  @Override
  public int hashCode() {
    return Objects.hash(tag);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    Tag tag = (Tag) obj;
    return this.tag.equals(tag.tag);
  }

}
