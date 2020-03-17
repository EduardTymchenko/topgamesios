package com.tymchemko.eduard.topgamesios.service;

import com.tymchemko.eduard.topgamesios.domain.Game;
import com.tymchemko.eduard.topgamesios.domain.TypeGames;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Repository
public class GameServiceRedisRepositoryImpl implements GameService {
    private static final Logger LOGGER = LoggerFactory.getLogger(GameServiceRedisRepositoryImpl.class);
    private RedisTemplate<TypeGames, Game> redisTemplate;
    private String logErrMess = "**!!!** Database connection error! ";

    public GameServiceRedisRepositoryImpl(RedisTemplate<TypeGames, Game> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public long clearAllGamesBase() {
        List<TypeGames> listTypeGames = Arrays.asList(TypeGames.values());
        try {
            return redisTemplate.delete(listTypeGames);
        } catch (RedisConnectionFailureException e) {
            String logErr = logErrMess + " Return from clearAllGamesBase()";
            LOGGER.error(logErr);
            return -1;
        }
    }

    @Override
    public long addAllGames(TypeGames typeGame, List<Game> gameList) {
        if (gameList.isEmpty()) {
            LOGGER.info("List of games is EMPTY. Base did not change");
            return 0;
        }
        try {
            long lengthListAdded = redisTemplate.opsForList().leftPushAll(typeGame, gameList);
            if (lengthListAdded > 0) {
                String logMess = "**** Added new records to base " + typeGame ;
                LOGGER.info(logMess);
                String logMessLength = "*** Number of records in the database: " + lengthListAdded;
                LOGGER.info(logMessLength);
                return lengthListAdded;
            }
        } catch (RedisConnectionFailureException e) {
            String logErr = logErrMess + " Return from addAllGames(TypeGames typeGame, List<Game> gameList)";
            LOGGER.error(logErr);
            return 0;
        }
        String logWarm = "!!!! Problem of add new records to base " + typeGame;
        LOGGER.warn(logWarm);
        return 0;
    }

    @Override
    public boolean normalizeBase(TypeGames typeGame, long maxLength) {
        if (maxLength <= 0) {
            String logWarm = "!!!! The base has not returned to normal. Normalizer length <= 0: " + maxLength;
            LOGGER.warn(logWarm);
            return false;
        }
        try {
            redisTemplate.opsForList().trim(typeGame, 0L, maxLength - 1);
            long lengthListNormalized = redisTemplate.opsForList().size(typeGame);
            if (lengthListNormalized <= maxLength) {
                String logMess = "**** The base of " + typeGame + " games is normalized to no more "
                        + maxLength + " records. Length base: " + lengthListNormalized;
                LOGGER.info(logMess);
                return true;
            }
        } catch (RedisConnectionFailureException e) {
            String logErr = logErrMess + " Return from normalizeBase(TypeGames typeGame, long maxLength)";
            LOGGER.error(logErr);
            return false;
        }
        String logWarm = "!!!! Problem of normalize base " + typeGame;
        LOGGER.warn(logWarm);
        return false;
    }

    @Override
    public List<Game> getListGameLength(TypeGames typeGame, long length) {
        if (length <= 0) return Collections.emptyList();
        return redisTemplate.opsForList().range(typeGame, 0L, length - 1);
    }
}
