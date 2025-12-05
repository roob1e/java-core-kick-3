package com.assxmblxr.ferry.factory;

import com.assxmblxr.ferry.entity.CarType;
import com.assxmblxr.ferry.entity.Loadable;
import com.assxmblxr.ferry.entity.impl.Car;
import com.assxmblxr.ferry.entity.impl.Truck;

import java.util.Random;

public class Cars {
  private static final String[] CAR_NAMES = new String[]{
          "Volvo",
          "BMW",
          "Alpina",
          "Mercedes",
          "Toyota",
          "Renault",
          "Fiat",
          "Honda",
          "Chevrolet",
          "Ford",
          "Lada",
          "Volkswagen",
  };
  private static final Random RANDOM = new Random();

  public static Loadable randomCar() {
    String name = CAR_NAMES[RANDOM.nextInt(CAR_NAMES.length)];
    CarType type = CarType.values()[RANDOM.nextInt(CarType.values().length)];
    if (type == CarType.CAR) {
      int weight = RANDOM.nextInt(1000, 3500);
      return new Car(weight, name);
    } else {
      int weight = RANDOM.nextInt(3500, 10000);
      return new Truck(weight, name);
    }
  }

  public static Loadable singleCar(CarType type, int weight, String name) {
    if (type == CarType.CAR) {
      return new Car(weight, name);
    } else {
      return new Truck(weight, name);
    }
  }
}
