package com.design.leetcode.tinder;

enum Gender {

  MALE(0), FEMALE(1);

  private Integer gender;

  private Gender(Integer gender) {
    this.gender = gender;
  }

  Integer getGender() {
    return this.gender;
  }

  static Gender getGender(int gender) {
    if (gender == 0) {
      return Gender.MALE;
    } else {
      return Gender.FEMALE;
    }
  }

}
