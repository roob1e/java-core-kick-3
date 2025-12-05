package com.assxmblxr.ferry.entity.impl;

import com.assxmblxr.ferry.entity.Loadable;

public class Truck implements Loadable {
  private int weight;
  private String name;

  public Truck() {
    weight = 5000;
    name = "";
  }

  public Truck(int weight, String name) {
    this.weight = weight;
    this.name = name;
  }

  @Override
  public int getWeight() {
    return this.weight;
  }

  @Override
  public String getName() {
    return this.name;
  }

  @Override
  public void setWeight(int weight) {
    if (weight >= 3500) {
      this.weight = weight;
    } else {
      throw new IllegalArgumentException("Truck weight must be more than 3500");
    }
  }

  @Override
  public void setName(String name) {
    this.name = name;
  }
}
