package com.design.leetcode.tinder;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

class User {

  private Integer id;
  private Gender gender;
  private Gender preferredGender;
  private Integer age;
  private Integer minPreferredAge;
  private Integer maxPreferredAge;
  private Set<String> interests;

  User() {
  }

  User(Integer id, Gender gender, Gender preferredGender, Integer age,
      Integer minPreferredAge, Integer maxPreferredAge, List<String> interests) {
    this.id = id;
    this.gender = gender;
    this.preferredGender = preferredGender;
    this.age = age;
    this.minPreferredAge = minPreferredAge;
    this.maxPreferredAge = maxPreferredAge;
    this.interests = new HashSet<>(interests);
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public Gender getGender() {
    return gender;
  }

  public void setGender(Gender gender) {
    this.gender = gender;
  }

  public Gender getPreferredGender() {
    return preferredGender;
  }

  public void setPreferredGender(Gender preferredGender) {
    this.preferredGender = preferredGender;
  }

  public Integer getAge() {
    return age;
  }

  public void setAge(Integer age) {
    this.age = age;
  }

  public Integer getMinPreferredAge() {
    return minPreferredAge;
  }

  public void setMinPreferredAge(Integer minPreferredAge) {
    this.minPreferredAge = minPreferredAge;
  }

  public Integer getMaxPreferredAge() {
    return maxPreferredAge;
  }

  public void setMaxPreferredAge(Integer maxPreferredAge) {
    this.maxPreferredAge = maxPreferredAge;
  }

  public Set<String> getInterests() {
    return interests;
  }

  public void setInterests(Set<String> interests) {
    this.interests = interests;
  }

}
