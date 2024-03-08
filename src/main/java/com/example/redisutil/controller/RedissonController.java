package com.example.redisutil.controller;

import com.example.redisutil.config.RedissonConfig;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/redisson")
public class RedissonController {

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private RedissonClient getRedissonClient;

    @Autowired
    private RedissonConfig redissonConfig;

    @PostMapping("/printKey")
    public Iterable<String> printKey() {
        return redissonConfig.redissonClient().getKeys().getKeys();
    }

//    @PostMapping("/changeDatabase")
//    public void changeDatabase(@RequestBody Integer database) {
//        redissonConfig.changeDatabase(database);
//    }
//
//    @PostMapping("/setNewRedisson")
//    public void setNewRedisson(@RequestParam String domain, Integer port, String password) {
//        redissonConfig.setNewRedisson(domain, port, password);
//    }

}
