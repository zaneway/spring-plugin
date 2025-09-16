package com.github.zaneway.domain.config;

import java.nio.file.Path;
import java.nio.file.Paths;
import org.pf4j.JarPluginManager;
import org.pf4j.PluginManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PluginConfig {

  @Bean
  public PluginManager manager(){
    //指定插件存放的目录
    Path path = Paths.get(
        "/Users/zhangzhenwei/SynologyDrive/code-space/IdeaProjects/learn/spring-plugin/domain/src/main/resources/plugins");
//    DefaultPluginManager manager = new DefaultPluginManager(path);
    JarPluginManager manager = new JarPluginManager(path);
    manager.loadPlugins();
    manager.startPlugins();
    return manager;
  }


}
