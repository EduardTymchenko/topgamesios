package com.tymchemko.eduard.topgamesios.service;

import com.tymchemko.eduard.topgamesios.domain.Game;
import com.tymchemko.eduard.topgamesios.domain.ResponseService;
import com.tymchemko.eduard.topgamesios.domain.TypeGames;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
@EnableScheduling
public class ScheduledTasks {
    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduledTasks.class);
    @Value("${maxLengthRecordsBase}")
    private long maxLengthRecordsBase;

    @Autowired
    GameService gameService;

    @Scheduled(fixedRate = 60 * 60 * 1000)
    public void getStatistics() {
        LOGGER.info("**** Start scheduled task getStatistics");

        for (TypeGames typeGames : TypeGames.values()) {
            String logMess = "**** Starting get statistic for " + typeGames + " games";
            LOGGER.info(logMess);
            try {
                gameService.addAllGames(typeGames, getListGamesFromRemoteService(typeGames.getUrlService()));
                gameService.normalizeBase(typeGames,maxLengthRecordsBase);
            } catch (RestClientException e) {
                String logErrMess = "!!!! External service for " + typeGames + " not available. Base is not updated";
                LOGGER.warn(logErrMess);
            }
        }
    }

    private List<Game> getListGamesFromRemoteService(String url) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseService res = restTemplate.getForObject(url, ResponseService.class);
        return res.getFeed().getResults();
    }

}
