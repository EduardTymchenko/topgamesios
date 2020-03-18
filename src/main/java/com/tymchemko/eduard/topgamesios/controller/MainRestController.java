package com.tymchemko.eduard.topgamesios.controller;

import com.tymchemko.eduard.topgamesios.domain.Game;
import com.tymchemko.eduard.topgamesios.domain.TypeGames;
import com.tymchemko.eduard.topgamesios.service.GameService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class MainRestController {
    private static final Logger LOGGER = LoggerFactory.getLogger(MainRestController.class);
    private GameService gameService;
    @Value("${nameLimitParameter}")
    private String nameLimitParameter;
    @Value("${minLimitParameter}")
    private long minLimitParameter;
    @Value("${maxLimitParameter}")
    private long maxLimitParameter;


    public MainRestController(GameService gameService){
        this.gameService = gameService;
    }


    @GetMapping("/ios/games/charts/{type}")
    public ResponseEntity<List<Game>> getListGames(@PathVariable String type,
                                                   @RequestParam(required = false) Map<String, String> allParams) {
        String logInReq = "Client send request: /ios/games/charts/" + type + ", parameters: " + allParams.toString();
        LOGGER.info(logInReq);
        try {
            if (!type.equals(type.toLowerCase())) {
                String logWarn = "Endpoint not correct: /ios/games/charts/" + type;
                LOGGER.warn(logWarn);
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            TypeGames typeGames = TypeGames.valueOf(type.toUpperCase());
            long lengthList = getParameterFromRequest(nameLimitParameter,minLimitParameter, maxLimitParameter, allParams);
            List<Game> gamesList = gameService.getListGameLength(typeGames, lengthList);
            gamesList.sort((game1, game2) -> game1.getName().compareToIgnoreCase(game2.getName()));
            return new ResponseEntity<>(gamesList, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            String logWarn = "Path is not correct: /ios/games/charts/" + type;
            LOGGER.warn(logWarn);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    private long getParameterFromRequest(String namePapameter, long minValue, long maxValue, Map<String, String> allParams) {
        if (allParams.size() == 1 && allParams.containsKey(namePapameter)) {
            try {
                long lengthListGames = Long.parseLong(allParams.get(namePapameter));
                if (lengthListGames < minValue || lengthListGames > maxValue){
                    String logWarn = "Parameter is outside diapason (" + minValue + "-" + maxValue + "): "
                            + namePapameter + " = " + lengthListGames + ". Set default value " + maxValue + ".";
                    LOGGER.warn(logWarn);
                    return maxValue;
                }
                String logInf = "Parameter is correct: " + namePapameter + " = " + lengthListGames;
                LOGGER.info(logInf);
                return lengthListGames;
            } catch (NumberFormatException e) {
                String logWarn = "Parameter \"" + namePapameter + "\" is not long. Set default max value "
                        + maxValue +".";
                LOGGER.warn(logWarn);
                return maxValue;
            }
        }
        String logWarn = "Parameter is absent or not correct. Set default max value " + maxValue +".";
        LOGGER.warn(logWarn);
        return maxValue;
    }
}
