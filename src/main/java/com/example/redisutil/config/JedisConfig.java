package com.example.redisutil.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
public class JedisConfig {

    JedisPoolConfig poolConfig = buildPoolConfig();
    JedisPool jedisPool = jedisConnectionFactory();

    private JedisPoolConfig buildPoolConfig() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(128);
        jedisPoolConfig.setMaxIdle(128);
        jedisPoolConfig.setMinIdle(16);
        jedisPoolConfig.setTestOnBorrow(true);
        jedisPoolConfig.setTestOnReturn(true);
        jedisPoolConfig.setTestWhileIdle(true);
//        jedisPoolConfig.setMinEvictableIdleTimeMillis(Duration.ofSeconds(60).toMillis());
//        jedisPoolConfig.setTimeBetweenEvictionRunsMillis(Duration.ofSeconds(30).toMillis());
        jedisPoolConfig.setNumTestsPerEvictionRun(3);
        jedisPoolConfig.setBlockWhenExhausted(true);
        return jedisPoolConfig;
    }

    private JedisPool jedisConnectionFactory() {
        return new JedisPool(poolConfig, "localhost",6379);
    }

    @Bean
    public Jedis getJedis(){
        return this.jedisPool.getResource();
    }
}
