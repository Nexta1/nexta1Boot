package com.nexta1.core.config.redis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Configuration;
import redis.embedded.RedisServer;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * @author valarchie
 */
@Configuration
@ConditionalOnExpression("'${nexta1boot.embedded.redis}' == 'true'")
public class EmbeddedRedisConfig {

    @Value("${spring.redis.port}")
    private Integer port;

    private RedisServer redisServer;


    @PostConstruct
    public void postConstruct() {
        RedisServer redisServer = RedisServer.builder().port(port)
                .setting("maxheap 32M")
                .setting("daemonize no")
                .setting("appendonly no").build();
        this.redisServer = redisServer;
        redisServer.start();
    }

    @PreDestroy
    public void preDestroy() {
        redisServer.stop();
    }

}
