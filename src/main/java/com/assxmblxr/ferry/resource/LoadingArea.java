package com.assxmblxr.ferry.resource;

import com.assxmblxr.ferry.entity.Loadable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
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
    try {
      lock.lock();
      activeLoaders++;

      while (!vehicles.isEmpty()) {
        Loadable next = vehicles.getFirst();
        while (currentWeight + next.getWeight() > WEIGHT_CAPACITY
                || currentArea + next.getArea() > AREA_CAPACITY) {
          notFull.await();
        }

        vehicles.removeFirst();
        currentWeight += next.getWeight();
        currentArea += next.getArea();
        cars.add(next);
        log.info("Added vehicle {}", next);
        TimeUnit.MILLISECONDS.sleep(200);
        notEmpty.signal();
      }

      activeLoaders--;
      if (activeLoaders == 0) {
        finished = true;
        notEmpty.signal();
      }

    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    } finally {
      lock.unlock();
    }
  }

  public void unload() {
    try {
      lock.lock();
      while (!finished || !cars.isEmpty()) {
        if (!cars.isEmpty()) {
          Loadable car = cars.removeFirst();
          currentWeight -= car.getWeight();
          currentArea -= car.getArea();
          log.info("Unloaded car {}", car);
          TimeUnit.MILLISECONDS.sleep(200);
          notFull.signalAll();
        } else {
          notEmpty.await();
        }
      }
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    } finally {
      lock.unlock();
    }
  }
}