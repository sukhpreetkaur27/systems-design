package com.design.leetcode.tests;

import static org.junit.Assert.fail;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.design.leetcode.uber.Uber;

public class UberApp {

  private Uber uber;

  /**
   * Set Up before each Test Case
   * 
   * @throws Exception
   */
  @BeforeEach
  public void setUp() throws Exception {
    uber = new Uber();
  }

  /**
   * Re-set After each test case
   * 
   * @throws Exception
   */
  @AfterEach
  public void tearDown() throws Exception {
    uber = null;
  }

  /**
   * Fail a test with given message
   */
  @Test
  @DisplayName("Implement Fail")
  public void implementFail() {
    fail("Not yet implemented");
  }

  /**
   * check for array equality
   */
  @Test
  @DisplayName("Start Trip")
  public void startTrip() {
    Assert.assertArrayEquals("No Cab Available", new int[] { -1, -1 },
        uber.startTrip(5, 10, 15));
    uber.addCab(10, 10);
    uber.addCab(30, 30);
    Assert.assertEquals("No Cab Available", Arrays.asList(-1, -1),
        uber.getNearestCabs(1, 12, 15));
  }

  @Test
  @DisplayName("Get Nearest Cabs")
  public void getNearestCabs() {
    uber.getNearestCabs(3, 5, -3);
    uber.getNearestCabs(3, 0, 0);
    uber.getNearestCabs(3, -3, -4);
  }

  public static void main1(String[] args) {
    Uber obj = new Uber();
    obj.getNearestCabs(3, 5, -3);
    obj.getNearestCabs(3, 0, 0);
    obj.getNearestCabs(3, -3, -4);
    obj.addCab(-4, 4);
    obj.addCab(1, -3);
    obj.endTrip(1, -5, 5);
    obj.endTrip(5, 1, -3);
    obj.startTrip(1, 5, -4);
    obj.getNearestCabs(2, -5, 2);
    obj.startTrip(4, 2, 1);
    obj.getNearestCabs(5, -2, 0);
    obj.getNearestCabs(5, -2, -3);
    obj.endTrip(1, -3, -2);
    obj.addCab(5, 3);
    obj.startTrip(2, -5, 1);
    obj.addCab(4, -1);
    obj.startTrip(5, -3, 4);
    obj.endTrip(5, -1, -3);
    obj.addCab(0, -3);
    obj.getNearestCabs(1, 4, -4);
    obj.getNearestCabs(5, 4, 1);
    obj.addCab(2, -4);
    obj.addCab(5, 0);
    obj.addCab(-3, 0);
    obj.startTrip(3, 4, -2);
    obj.addCab(-2, 3);
    obj.getNearestCabs(2, -4, 5);
    obj.getNearestCabs(4, 3, 2);
    obj.addCab(3, -2);
    obj.endTrip(3, -3, -3);
    obj.endTrip(4, 0, -4);
    obj.addCab(-1, -5);
    obj.startTrip(5, -2, -3);
    obj.startTrip(1, 1, 0);
    obj.addCab(4, 5);
    obj.addCab(-2, -2);
    obj.getNearestCabs(4, 0, -4);
    obj.getNearestCabs(3, 5, 2);
    obj.getNearestCabs(2, 0, 5);
    obj.getNearestCabs(4, 4, -2);
  }

}
