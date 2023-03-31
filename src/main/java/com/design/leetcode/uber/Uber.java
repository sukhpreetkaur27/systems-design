package com.design.leetcode.uber;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class Uber {

  private AtomicInteger cabs;
  private Map<Integer, Point> cabLocation;
  private Map<Integer, Integer> customerCab;

  public Uber() {
    cabs = new AtomicInteger(1);
    cabLocation = new ConcurrentHashMap<>();
    customerCab = new ConcurrentHashMap<>();
  }

  public void addCab(int cabX, int cabY) {
    cabLocation.put(cabs.intValue(), new Point(cabX, cabY));
    cabs.incrementAndGet();
  }

  public int[] startTrip(int customerId, int customerX, int customerY) {
    Point customerPoint = new Point(customerX, customerY);
    int cabId = getNearestCab(customerPoint);

    if (cabId == -1) {
      return new int[] { -1, -1 };
    }

    customerCab.put(customerId, cabId);

    Point cab = cabLocation.get(cabId);
    int x = cab.getX().intValue();
    int y = cab.getY().intValue();

    /**
     * marking the cab unavailable for other trips
     */
    cabLocation.put(cabId, new Point());

    return new int[] { x, y };
  }

  public void endTrip(int customerId, int customerX, int customerY) {
    Integer cabId = customerCab.get(customerId);
    if (cabId == null) {
      return;
    }
    cabLocation.put(cabId, new Point(customerX, customerY));
    customerCab.remove(customerId);
  }

  public List<List<Integer>> getNearestCabs(int k, int x, int y) {
    final Point origin = new Point(x, y);
    Queue<Point> maxHeap = getMaxHeapOfKNearestCabs(k, origin);
    List<Point> sortedPoints = getSortedPoints(maxHeap);
    List<List<Integer>> points = getSortedPoints(sortedPoints);
    return points;
  }

  private List<List<Integer>> getSortedPoints(List<Point> sortedPoints) {
    List<List<Integer>> points = new ArrayList<>();
    Collections.sort(sortedPoints);
    for (Point point : sortedPoints) {
      List<Integer> p1 = Arrays.asList(point.getX(), point.getY());
      points.add(p1);
    }
    return points;
  }

  private List<Point> getSortedPoints(Queue<Point> maxHeap) {
    List<Point> points = new ArrayList<>();
    while (!maxHeap.isEmpty()) {
      Point poll = maxHeap.poll();
      points.add(poll);
    }
    Collections.reverse(points);
    return points;
  }

  private Queue<Point> getMaxHeapOfKNearestCabs(int k, Point origin) {
    Queue<Point> maxHeap = new PriorityQueue<>(k, new Comparator<Point>() {
      @Override
      public int compare(Point a, Point b) {
        /**
         * NOTE: Sort in descending order
         */
        if (isNear(origin, a, b)) {
          return 1;
        }
        return -1;
      }
    });
    for (Entry<Integer, Point> entry : cabLocation.entrySet()) {
      Point point = entry.getValue();

      if (point.getX() == null || point.getY() == null) {
        continue;
      }

      Point cab = new Point(point.getX(), point.getY());
      cab.setDistance(calculateDistance(cab, origin));

      if (maxHeap.size() < k) {
        maxHeap.offer(cab);
      } else {
        Point peek = maxHeap.peek();
        if (isNear(origin, cab, peek)) {
          maxHeap.poll();
          maxHeap.offer(cab);
        }
      }
    }
    return maxHeap;
  }

  private int getNearestCab(Point customerPoint) {
    int nearestCab = -1;
    Point nearestCabPoint = null;
    for (Entry<Integer, Point> entry : cabLocation.entrySet()) {
      int cabId = entry.getKey();
      Point cabPoint = entry.getValue();

      /**
       * Cab is running on a trip
       */
      if (cabPoint.getX() == null || cabPoint.getY() == null) {
        continue;
      }

      if (isNear(customerPoint, cabPoint, nearestCabPoint)) {
        nearestCabPoint = cabPoint;
        nearestCab = cabId;
      }
    }
    return nearestCab;
  }

  private boolean isNear(Point customerPoint, Point cabPoint, Point nearestCabPoint) {
    Double distanceCab = calculateDistance(customerPoint, cabPoint);
    Double distanceNearestCab = calculateDistance(customerPoint, nearestCabPoint);
    if (Double.compare(distanceCab, distanceNearestCab) < 0) {
      return true;
    } else if (Double.compare(distanceCab, distanceNearestCab) == 0) {
      if (cabPoint.compareTo(nearestCabPoint) < 1) {
        return true;
      }
    }
    return false;
  }

  private Double calculateDistance(Point a, Point b) {
    if (a == null || b == null) {
      return Double.MAX_VALUE;
    }

    return Math.sqrt(Math.pow(a.getX().intValue() - b.getX().intValue(), 2)
        + Math.pow(a.getY().intValue() - b.getY().intValue(), 2));
  }

}
