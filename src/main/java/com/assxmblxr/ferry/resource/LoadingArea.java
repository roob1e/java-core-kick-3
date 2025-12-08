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
  private static LoadingArea instance;
  private static final Lock instanceLock = new ReentrantLock();

  private final Lock lock = new ReentrantLock();
  private final Condition canLoad = lock.newCondition();
  private final Condition canUnload = lock.newCondition();

  private static final int WEIGHT_CAPACITY = 10000;
  private static final double AREA_CAPACITY = 100;

  private final List<Loadable> loadedVehicles = new ArrayList<>();
  private int currentWeight = 0;
  private double currentArea = 0;
  private boolean loadingFinished = false;
  private int activeLoaders = 0;
  private boolean unloadingInProgress = false;

  private LoadingArea() {}

  public static LoadingArea getInstance() {
    instanceLock.lock();
    try {
      if (instance == null) {
        instance = new LoadingArea();
      }
      return instance;
    } finally {
      instanceLock.unlock();
    }
  }

  public void load(List<Loadable> vehicles) {
    lock.lock();
    try {
      activeLoaders++;
    } finally {
      lock.unlock();
    }

    try {
      for (Loadable vehicle : vehicles) {
        boolean vehicleLoaded = false;

        while (!vehicleLoaded) {
          lock.lock();
          try {
            while (unloadingInProgress) {
              canLoad.await();
            }

            if (currentWeight + vehicle.getWeight() <= WEIGHT_CAPACITY &&
                    currentArea + vehicle.getArea() <= AREA_CAPACITY) {
              loadedVehicles.add(vehicle);
              currentWeight += vehicle.getWeight();
              currentArea += vehicle.getArea();
              log.info("Added vehicle {}", vehicle);
              vehicleLoaded = true;

              boolean isFull = currentWeight >= WEIGHT_CAPACITY ||
                      currentArea >= AREA_CAPACITY;
              boolean nextWontFit = currentWeight + vehicle.getWeight() > WEIGHT_CAPACITY ||
                      currentArea + vehicle.getArea() > AREA_CAPACITY;

              if (isFull || nextWontFit) {
                canUnload.signal();
              }
            } else {
              canUnload.signal();
              canLoad.await();
            }
          } finally {
            lock.unlock();
          }

          if (vehicleLoaded) {
            TimeUnit.MILLISECONDS.sleep(200);
          }
        }
      }
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    } finally {
      lock.lock();
      try {
        activeLoaders--;
        if (activeLoaders == 0) {
          loadingFinished = true;
          canUnload.signal();
        }
      } finally {
        lock.unlock();
      }
    }
  }

  public void unload() {
    try {
      lock.lock();
      while (!loadingFinished || !loadedVehicles.isEmpty()) {
        while (loadedVehicles.isEmpty() && !loadingFinished) {
          canUnload.await();
        }

        if (loadedVehicles.isEmpty()) {
          break;
        }

        unloadingInProgress = true;
        List<Loadable> vehiclesToUnload = new ArrayList<>(loadedVehicles);
        loadedVehicles.clear();
        currentWeight = 0;
        currentArea = 0;
        lock.unlock();
        try {
          for (Loadable vehicle : vehiclesToUnload) {
            log.info("Unloaded car {}", vehicle);
            TimeUnit.MILLISECONDS.sleep(200);
          }
        } finally {
          lock.lock();
          unloadingInProgress = false;
          canLoad.signalAll();
        }
      }
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    } finally {
      lock.unlock();
    }
  }
}