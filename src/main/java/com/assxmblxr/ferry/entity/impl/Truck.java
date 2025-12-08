package com.assxmblxr.ferry.entity.impl;

import com.assxmblxr.ferry.entity.Loadable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Truck implements Loadable {
  private static final Logger log = LogManager.getLogger();
  private final int weight;
  private final String name;
  private final double area;

  public Truck() {
    weight = 5000;
    name = "";
    area = 30;
    log.info("Initialized base Truck {}: weight: {}, area: {}", name, weight, area);
  }

  public Truck(int weight, String name, double area) {
    if (weight < 3500) {
      throw new IllegalArgumentException("Weight must be between 0 and 3500");
    } else if (area < 0) {
      throw new IllegalArgumentException("Area must be between 0 and Infinity");
    }
    this.weight = weight;
    this.name = name;
    this.area = area;
    log.info("Initialized Truck {}: weight: {}, area: {}", name, weight, area);
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
  public double getArea() {
    return area;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append("Truck ");
    builder.append(getName());
    builder.append(" { weight: ");
    builder.append(getWeight());
    builder.append(", area: ");
    builder.append(getArea());
    builder.append(" }");
    return builder.toString();
  }
}