package com.design.leetcode.walnut;

public class WalnutApp {

  public static void main(String[] args) {
    Walnut obj = new Walnut();
    System.out.println(obj.getAverageUserEarnings());
    obj.parseText(1, "deposit $120.45");
    obj.parseText(1, "credit $238.98");
    obj.parseText(1, "Transaction Cost USD 25.55");
    obj.parseText(1, "debited $14790");
    obj.parseText(1, "deposited $1000.00");
    System.out.println(obj.getTotalUserEarnings(1));
    System.out.println(obj.getTotalUserExpenses(1));
    System.out.println(obj.getAverageUserEarnings());
    System.out.println(obj.getAverageUserExpenses());
  }

}
