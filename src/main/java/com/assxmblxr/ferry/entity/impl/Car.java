package com.assxmblxr.ferry.entity.impl;

import com.assxmblxr.ferry.entity.Loadable;

public class Car implements Loadable {
  private int weight;
  private String name;

  public Car() {
    weight = 2000;
    name = "";
  }

  public Car(int weight, String name) {
    this.weight = weight;
    this.name = name;
  }

  @Override
  public int getWeight() {
    return weight;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public void setWeight(int weight) {
    if (weight < 3500) {
      this.weight = weight;
    } else {
      throw new IllegalArgumentException("Car weight must be under 3500");
    }
  }

  @Override
  public void setName(String name) {
    this.name = name;
  }
}
