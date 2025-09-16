package com.github.zaneway.domain.listener;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import org.pf4j.PluginManager;
import org.springframework.stereotype.Component;

@Component
public class PluginListener {

  @Resource
  private PluginManager manager;


  @PostConstruct
  public void listen() {
    new Thread(() -> {
      try {
        WatchService watchService = FileSystems.getDefault().newWatchService();
        Path pluginDir = manager.getPluginsRoot();

        pluginDir.register(
            watchService,
            StandardWatchEventKinds.ENTRY_CREATE,
            StandardWatchEventKinds.ENTRY_DELETE,
            StandardWatchEventKinds.ENTRY_MODIFY
        );

        System.out.println("开始监听插件目录: " + pluginDir.toAbsolutePath());

        while (true) {
          WatchKey key = watchService.take();

          for (WatchEvent<?> event : key.pollEvents()) {
            WatchEvent.Kind<?> kind = event.kind();
            Path filename = (Path) event.context();
            Path pluginPath = pluginDir.resolve(filename);

            if (!filename.toString().endsWith(".jar")) {
              continue; // 只处理 jar 文件
            }

            if (kind == StandardWatchEventKinds.ENTRY_CREATE) {
              System.out.println("检测到新插件: " + filename);
              String pluginId = manager.loadPlugin(pluginPath);
              manager.startPlugin(pluginId);
            } else if (kind == StandardWatchEventKinds.ENTRY_DELETE) {
              System.out.println("检测到插件被删除: " + filename);
              manager.getPlugins()
                  .stream()
                  .filter(p -> p.getPluginPath().endsWith(filename.toString()))
                  .findFirst()
                  .ifPresent(p -> {
                    manager.stopPlugin(p.getPluginId());
                    manager.unloadPlugin(p.getPluginId());
                  });
            } else if (kind == StandardWatchEventKinds.ENTRY_MODIFY) {
              System.out.println("检测到插件更新: " + filename);
              manager.getPlugins()
                  .stream()
                  .filter(p -> p.getPluginPath().endsWith(filename.toString()))
                  .findFirst()
                  .ifPresent(p -> {
                    manager.stopPlugin(p.getPluginId());
                    manager.unloadPlugin(p.getPluginId());
                  });

              String pluginId = manager.loadPlugin(pluginPath);
              manager.startPlugin(pluginId);
            }
          }
          key.reset();
        }
      } catch (IOException | InterruptedException e) {
        e.printStackTrace();
      }
    }, "Plugin-Watcher-Thread").start();
  }


}
