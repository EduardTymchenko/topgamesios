package com.tymchemko.eduard.topgamesios;

import com.tymchemko.eduard.topgamesios.domain.Game;
import com.tymchemko.eduard.topgamesios.domain.TypeGames;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;


@SpringBootApplication
public class TopgamesiosApplication {
    @Value("${spring.redis.host}")
    private String hostRedis;
    @Value("${spring.redis.port}")
    private int portRedis;

    @Bean
    JedisConnectionFactory jedisConnectionFactory() {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration(hostRedis, portRedis);
        return new JedisConnectionFactory(redisStandaloneConfiguration);
    }

    @Bean
    RedisTemplate<TypeGames, Game> redisTemplate() {
        RedisTemplate<TypeGames, Game> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(jedisConnectionFactory());
        return redisTemplate;
    }

    public static void main(String[] args) {
        SpringApplication.run(TopgamesiosApplication.class, args);
    }

}
