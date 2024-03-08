package com.example.redisutil.controller;

import cn.hutool.core.util.ObjectUtil;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/stringRedisTemplate")
public class StringRedisTemplateController {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @PostMapping("/keys")
    public Set<String> keys(@RequestParam(required=false) String pattern) {
        if (ObjectUtil.isEmpty(pattern)){
            pattern = "*";
        }
        Set<String> keys = stringRedisTemplate.keys(pattern);
        return keys;
    }

    @PostMapping("/get")
    public String get(@RequestParam String key) {

        String value = stringRedisTemplate.opsForValue().get(key);
        return value;
    }

    @PostMapping("/set")
    public void set(@RequestParam String key, String value) {
        stringRedisTemplate.opsForValue().set(key, value);
    }
}
