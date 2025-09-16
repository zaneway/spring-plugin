package com.github.zaneway.plugin;

import org.pf4j.Plugin;
import org.pf4j.PluginWrapper;

public class DemoPlugin extends Plugin {

  public DemoPlugin(PluginWrapper pluginWrapper) {
    super(pluginWrapper);
  }


  public DemoPlugin() {
  }

  @Override
  public void start() {
    System.out.println("plugins start");
  }

  @Override
  public void stop() {
    System.out.println("plugins stop");
  }

  @Override
  public void delete() {
    System.out.println("plugins del");
  }
}
