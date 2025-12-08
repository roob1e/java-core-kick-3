package com.assxmblxr.ferry;

import com.assxmblxr.ferry.entity.Loadable;
import com.assxmblxr.ferry.entity.Loader;
import com.assxmblxr.ferry.entity.Unloader;
import com.assxmblxr.ferry.factory.Cars;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {
  public static void main(String[] args) {
    List<Loadable> loadBatch1 = Stream.generate(Cars::randomCar)
            .limit(10)
            .collect(Collectors.toList());

    List<Loadable> loadBatch2 = Stream.generate(Cars::randomCar)
            .limit(10)
            .collect(Collectors.toList());

    Thread unloaderThread1 = new Thread(new Unloader(), "Ferry-Unloader-1");
    Thread unloaderThread2 = new Thread(new Unloader(), "Ferry-Unloader-2");
    unloaderThread1.start();
    unloaderThread2.start();

    Thread loader1 = new Thread(new Loader(loadBatch1), "Ferry-Loader-1");
    Thread loader2 = new Thread(new Loader(loadBatch2), "Ferry-Loader-2");
    loader1.start();
    loader2.start();

    try {
      loader1.join();
      loader2.join();
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }
    unloaderThread1.interrupt();
    unloaderThread2.interrupt();
  }
}