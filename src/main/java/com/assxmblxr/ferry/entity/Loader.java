package com.assxmblxr.ferry.entity;

import com.assxmblxr.ferry.resource.LoadingArea;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class Loader implements Runnable {
  Logger log = LogManager.getLogger();
  private final LoadingArea area = LoadingArea.getInstance();
  private final List<Loadable> vehicles;

  public Loader(List<Loadable> vehicles) {
    this.vehicles = vehicles;
  }

  @Override
  public void run() {
    area.load(vehicles);
    log.info("Loading finished");
  }
}