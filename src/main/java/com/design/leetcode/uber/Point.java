package com.design.leetcode.uber;

class Point implements Comparable<Point> {

  private Integer x;
  private Integer y;
  private Double distance;

  Point() {

  }

  Point(Integer x, Integer y) {
    this.x = x;
    this.y = y;
  }

  @Override
  public int compareTo(Point a) {
    if (this.x.intValue() < a.x.intValue()) {
      return -1;
    } else if (this.x.intValue() == a.x.intValue()) {
      if (this.y.intValue() < a.y.intValue()) {
        return -1;
      } else if (this.y.intValue() == a.y.intValue()) {
        return 0;
      }
    }
    return 1;
  }

  public Integer getX() {
    return x;
  }

  public void setX(Integer x) {
    this.x = x;
  }

  public Integer getY() {
    return y;
  }

  public void setY(Integer y) {
    this.y = y;
  }

  public Double getDistance() {
    return distance;
  }

  public void setDistance(Double distance) {
    this.distance = distance;
  }

}
