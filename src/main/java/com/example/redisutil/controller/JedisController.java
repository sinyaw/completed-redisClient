package com.example.redisutil.controller;

import cn.hutool.json.JSONUtil;
import com.example.redisutil.domain.vo.JedisLockReq;
import io.swagger.annotations.ApiOperation;
import java.util.HashMap;
import java.util.Set;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;

@RestController
@RequestMapping("/jedis")
public class JedisController {

  @Autowired
  private Jedis jedis;

  @ApiOperation(value = "set jedis lock")
  @PostMapping("/setWithExpire")
  public Boolean setWithExpire(@RequestBody @Validated(JedisLockReq.Set.class) JedisLockReq req) {
    Long timeSet = System.currentTimeMillis();
    if (ObjectUtils.isEmpty(req.getValue())) {
      req.setValue(timeSet.toString()); // value same as time now
    }
    if (ObjectUtils.isEmpty(req.getLockInSecond())) {
      req.setLockInSecond(2); // 2 second lock time
    }
    if (ObjectUtils.isEmpty(req.getWaitingInMillisecond())) {
      req.setWaitingInMillisecond(10 * 1000L); // 10 second waiting release lock
    }

    for (; ; ) {
      Long setnx = jedis.setnx(req.getKeyName(), req.getValue());
      System.out.println("setnx: " + setnx);
      if (setnx.compareTo(1l) == 0) {
        jedis.expire(req.getKeyName(), req.getLockInSecond());
        return true;
      }

      // 请求时间超过
      if (System.currentTimeMillis() > timeSet + req.getWaitingInMillisecond()) {
        return false;
      }
    }
  }

  @ApiOperation(value = "get Keys")
  @PostMapping("/getKeys")
  public Set<String> getKeys(@RequestBody String keyPattern) {
    // keyPattern like '*' for all, 'A*' start with A
    //        Jedis jedis = jedisPool.getResource();

    Set<String> setString = jedis.keys(keyPattern);
    setString.forEach(
        s -> {
          System.out.println(s);
        });
    return setString;
  }

  @ApiOperation(value = "get Value")
  @PostMapping("/getValue")
  public String getValue(@RequestBody String key) {
    //        Jedis jedis = jedisPool.getResource();
    return jedis.get(key);
  }

  @ApiOperation(value = "map class set and get")
  @PostMapping("/getMap")
  public String getMap(@RequestBody String key) {

    // set redis
    HashMap<String, String> map1 = new HashMap<>();
    System.out.println("map 1: " + map1);
    map1.put(key, key);
    jedis.set(key, JSONUtil.toJsonStr(map1));

    // get redis
    HashMap<String, String> map2 = new HashMap(JSONUtil.toBean(jedis.get("key"), HashMap.class));
    System.out.println("map 2: " + map2);

    return map2.get(key);
  }
}
