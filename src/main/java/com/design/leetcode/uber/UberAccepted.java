package com.design.leetcode.uber;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class UberAccepted {

  // Mapping from cabId to cab coordinates
  Map<Integer, Point> cabs;

  // Mapping from customerId to cabId
  Map<Integer, Integer> customerToCabMapping;

  // Running counter to generate the id of the next cab when it's added.
  AtomicInteger nextCabId;

  public static class Point implements Comparable<Point> {
    public Integer x;
    public Integer y;
    // Just used for finding the nearest cabs.
    public double distance;

    public Point(Integer x, Integer y) {
      super();
      this.x = x;
      this.y = y;
    }

    @Override
    public int compareTo(Point o) {
      if (this.x.intValue() < o.x.intValue()) {
        return -1;
      } else if (this.x.intValue() == o.x.intValue()) {
        if (this.y.intValue() < o.y.intValue()) {
          return -1;
        } else if (this.y.intValue() == o.y.intValue()) {
          return 0;
        }
      }
      return 1;
    }
  }

  public UberAccepted() {
    cabs = new ConcurrentHashMap<>();
    customerToCabMapping = new ConcurrentHashMap<Integer, Integer>();
    nextCabId = new AtomicInteger(1);
  }

  public void addCab(int cabX, int cabY) {
    cabs.put(nextCabId.intValue(), new Point(cabX, cabY));
    nextCabId.incrementAndGet();
  }

  public int[] startTrip(int customerID, int customerX, int customerY) {
    int cabId = getNearestCab(customerX, customerY);
    if (cabId == -1) {
      return new int[] { -1, -1 };
    }
    customerToCabMapping.put(customerID, cabId);
    int nearX = cabs.get(cabId).x;
    int nearY = cabs.get(cabId).y;

    // To make the cabId non-seachable for other startTrip and getNearestCabs calls
    cabs.put(cabId, new Point(null, null));

    return new int[] { nearX, nearY };
  }

  public void endTrip(int customerID, int customerX, int customerY) {
    Integer cabId = customerToCabMapping.get(customerID);
    if (cabId == null) {
      return;
    }
    cabs.put(cabId, new Point(customerX, customerY));

    customerToCabMapping.remove(customerID);
  }

  public List<List<Integer>> getNearestCabs(int k, int customerX, int customerY) {

    final Point customerPoint = new Point(customerX, customerY);
    PriorityQueue<Point> maxHeap = getMaxHeapOfKNearestCabs(k, customerPoint);

    List<Point> nearbyCabs = new ArrayList<>();
    while (!maxHeap.isEmpty()) {
      Point p = maxHeap.poll();
      nearbyCabs.add(new Point(p.x, p.y));
    }
    Collections.reverse(nearbyCabs);
    List<List<Integer>> response = getSortedCabsListByXY(nearbyCabs);
    return response;
  }

  /*
   * Helper functions.
   */

  // Returns the cabId of the nearest cab. The reason we are not re-using
  // getNearestCabs with k = 1 here is that
  // to find just the nearest cab, the overhead of creating the heap would
  // actually slow us down.
  private int getNearestCab(int customerX, int customerY) {

    int nearestCabId = -1;
    Point nearestCabPoint = null;
    Point customerPoint = new Point(customerX, customerY);

    for (Entry<Integer, Point> entry : cabs.entrySet()) {
      int cabId = entry.getKey();
      Point cabPoint = entry.getValue();

      if (cabPoint.x == null || cabPoint.y == null) {
        continue;
      }

      if (isNear(nearestCabPoint, customerPoint, cabPoint)) {
        nearestCabId = cabId;
        nearestCabPoint = cabPoint;
      }
    }
    return nearestCabId;
  }

  private List<List<Integer>> getSortedCabsListByXY(List<Point> nearestCabs) {
    List<List<Integer>> response = new ArrayList<>();

    Collections.sort(nearestCabs);

    for (Point nc : nearestCabs) {
      response.add(Arrays.asList(nc.x, nc.y));
    }
    return response;

  }

  private PriorityQueue<Point> getMaxHeapOfKNearestCabs(int k,
      final Point customerPoint) {
    PriorityQueue<Point> maxHeap = new PriorityQueue<>(new Comparator<Point>() {

      @Override
      public int compare(Point o2, Point o1) {
        if (isNear(o1, customerPoint, o2)) {
          return 1;
        }
        return -1;
      }
    });

    for (Entry<Integer, Point> entry : cabs.entrySet()) {
      Point cabPoint = entry.getValue();
      Point newCabPoint = new Point(cabPoint.x, cabPoint.y);
      if (cabPoint.x == null || cabPoint.y == null) {
        continue;
      }
      newCabPoint.distance = getDistance(newCabPoint, customerPoint);

      if (maxHeap.size() < k) {
        maxHeap.add(newCabPoint);
      } else {
        // Decide if we need to remove the head of the heap and put something else.
        Point heapHeadPoint = maxHeap.peek();
        boolean isNear = isNear(heapHeadPoint, customerPoint, cabPoint);
        if (isNear) {
          maxHeap.poll();
          maxHeap.add(newCabPoint);
        }
      }
    }
    return maxHeap;
  }

  // Checks is cabPoint is near to customerPoint than nearestCabPoint
  private boolean isNear(Point nearestCabPoint, Point customerPoint, Point cabPoint) {

    double nearestCabDistance = getDistance(nearestCabPoint, customerPoint);
    double cabDistance = getDistance(cabPoint, customerPoint);
    if (Double.compare(cabDistance, nearestCabDistance) < 0) {
      nearestCabDistance = cabDistance;
      return true;
    }
    if (Double.compare(cabDistance, nearestCabDistance) == 0) {
      if (cabPoint.compareTo(nearestCabPoint) < 1) {
        return true;
      }
    }
    return false;
  }

  private double getDistance(Point cabPoint, Point customerPoint) {
    if (cabPoint == null || customerPoint == null) {
      return Double.MAX_VALUE;
    }
    return Math.sqrt(Math.pow(cabPoint.x.intValue() - customerPoint.x.intValue(), 2)
        + Math.pow(cabPoint.y.intValue() - customerPoint.y.intValue(), 2));
  }
}
