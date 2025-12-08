package com.assxmblxr.ferry.entity;

import com.assxmblxr.ferry.resource.LoadingArea;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Unloader implements Runnable {
  private static final Logger log = LogManager.getLogger();
  private final LoadingArea area;

  public Unloader() {
    this.area = LoadingArea.getInstance();
  }

  @Override
  public void run() {
    area.unload();
    log.info("Unloading finished");
  }
}
