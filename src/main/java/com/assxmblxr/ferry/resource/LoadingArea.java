package com.assxmblxr.ferry.resource;

import com.assxmblxr.ferry.entity.Loadable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LoadingArea {
  private static final Logger log = LogManager.getLogger();

  private static final Lock lock = new ReentrantLock();
  private static final Condition notFull = lock.newCondition();
  private static final Condition notEmpty = lock.newCondition();

  private static LoadingArea instance;

  private static final int WEIGHT_CAPACITY = 10000;
  private static final double AREA_CAPACITY = 100;

  private final List<Loadable> cars = new ArrayList<>();
  private int currentWeight = 0;
  private double currentArea = 0;
  private boolean finished = false;
  private int activeLoaders = 0;

  private LoadingArea() {}

  public static LoadingArea getInstance() {
    if (instance == null) {
      lock.lock();
      try {
        if (instance == null) {
          instance = new LoadingArea();
        }
      } finally {
        lock.unlock();
      }
    }
    return instance;
  }

  public void load(List<Loadable> vehicles) {
    lock.lock();
    try {
      activeLoaders++;
    } finally {
      lock.unlock();
    }

    try {
      lock.lock();
      while (!vehicles.isEmpty()) {
        if (currentWeight + vehicles.getFirst().getWeight() <= WEIGHT_CAPACITY
                && currentArea + vehicles.getFirst().getArea() <= AREA_CAPACITY) {
          var currentCar = vehicles.getFirst();
          currentWeight += currentCar.getWeight();
          currentArea += currentCar.getArea();
          cars.add(vehicles.removeFirst());
          log.info("Added vehicle {}", currentCar.toString());
          notEmpty.signal();
        } else {
          notFull.await();
        }
      }
      lock.lock();
      try {
        activeLoaders--;
        if (activeLoaders == 0) {
          finished = true;
          notEmpty.signal();
        }
      } finally {
        lock.unlock();
      }
    } catch (InterruptedException e) {
      log.error("Interrupted", e);
      Thread.currentThread().interrupt();
    } finally {
      lock.unlock();
    }
  }

  public void unload() {
    try {
      lock.lock();
      while (true) {
        if (!cars.isEmpty()) {
          Loadable currentCar = cars.getFirst();
          currentWeight -= currentCar.getWeight();
          currentArea -= currentCar.getArea();
          cars.remove(currentCar);
          log.info("Unloaded car {}", currentCar.toString());
          if (cars.isEmpty()) {
            notFull.signal();
          }
        } else if (finished) {
          break;
        } else {
          notEmpty.await();
        }
      }
    } catch (InterruptedException e) {
      log.error("Interrupted", e);
      Thread.currentThread().interrupt();
    } finally {
      lock.unlock();
    }
  }
}