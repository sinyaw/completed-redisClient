package com.example.redisutil.config;

import cn.hutool.core.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.api.RedissonReactiveClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class RedissonConfig {
  @Value("${spring.redis.port:6379}")
  private String redisPort;
  @Value("${spring.redis.host:192.168.10.21}")
  private String redisHost;
  @Value("${spring.redis.password:V1dwT1UyRnJNVFpYVkVaT1ZrVnNObFJyVWxaTlp6MDk=}")
  private String password;

  private RedissonClient redissonClient = initRedisson();
  private Config config;

  private RedissonClient initRedisson() {
    Config config = new Config();
    config.useSingleServer().setAddress("redis://localhost:6379");
    config.useSingleServer().setDatabase(0);
    //        config.useSingleServer().setAddress("redis://192.168.10.21:6379");
    //        config.useSingleServer().setPassword("V1dwT1UyRnJNVFpYVkVaT1ZrVnNObFJyVWxaTlp6MDk=");
    //        redissonClient.shutdown();
    this.config = config;
    return Redisson.create(config);
  }

  /**Still not working*/
  public void changeDatabase(int database) {
    this.redissonClient.getConfig().useSingleServer().setDatabase(database);
  }

  /**Still not working*/
  public void setNewRedisson(String domain, Integer port, String password) {
//    this.redissonClient.shutdown();
    Config config = new Config();

    String redissonPort = ObjectUtil.isEmpty(port) ? String.valueOf(port) : "6379";
    String redisUrl = String.format("redis://%s:%s", domain, redissonPort);
    config.useSingleServer().setAddress(redisUrl);
    if (ObjectUtil.isEmpty(password)) {
      config.useSingleServer().setPassword(password);
    }
    config.useSingleServer().setDatabase(3);

    this.config = config;
    RedissonReactiveClient reactive = Redisson.createReactive(config);
  }

  @Bean
  public RedissonClient redissonClient() {
    return this.redissonClient;
  }
}
