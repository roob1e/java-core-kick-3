package com.assxmblxr.ferry.entity.impl;

import com.assxmblxr.ferry.entity.Loadable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Car implements Loadable {
  private final static Logger log = LogManager.getLogger();
  private final int weight;
  private final String name;
  private final double area;

  public Car() {
    weight = 2000;
    name = "";
    area = 10;
    log.info("Initialized base Car {}: weight: {}, area: {}", name, weight, area);
  }

  public Car(int weight, String name, double area) {
    if (weight < 0 || weight > 3500) {
      throw new IllegalArgumentException("Weight must be between 0 and 3500");
    }
    this.weight = weight;
    this.name = name;
    this.area = area;
    log.info("Initialized Car {}: weight: {}, area: {}", name, weight, area);
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
    builder.append("Car ");
    builder.append(getName());
    builder.append(" { weight: ");
    builder.append(getWeight());
    builder.append(", area: ");
    builder.append(getArea());
    builder.append(" }");
    return builder.toString();
  }
}
