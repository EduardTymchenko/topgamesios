package com.tymchemko.eduard.topgamesios;

import com.tymchemko.eduard.topgamesios.controller.MainRestController;
import com.tymchemko.eduard.topgamesios.domain.Game;
import com.tymchemko.eduard.topgamesios.domain.TypeGames;
import com.tymchemko.eduard.topgamesios.service.GameService;
import com.tymchemko.eduard.topgamesios.service.ScheduledTasks;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.Assert.*;

@SpringBootTest
class TopgamesiosApplicationTests {
    @MockBean
    ScheduledTasks scheduledTasks;

    @Autowired
    MainRestController mainController;

    @Autowired
    GameService gameService;

    // Tests MainRestController
    @Test
    public void EndPointWithoutParameterReturn200Test() {
        String[] endPoins = {"free", "paid", "grossing"};
        Map<String, String> parameters = Collections.emptyMap();
        int[] arrayLengthRecordBase = {0, 1, 20, 99, 100, 101, 150, 200};
        int expectedSizeResponse;
        for (int i = 0; i < arrayLengthRecordBase.length; i++) {
            createBaseByLength(arrayLengthRecordBase[i]);
            for (int j = 0; j < endPoins.length; j++) {
                ResponseEntity<List<Game>> resp = mainController.getListGames(endPoins[j], parameters);
                assertTrue(resp.getStatusCode().is2xxSuccessful());
                expectedSizeResponse = arrayLengthRecordBase[i];
                if (arrayLengthRecordBase[i] > 100) expectedSizeResponse = 100;
                assertEquals(expectedSizeResponse, resp.getBody().size());
            }
        }
    }

    @Test
    public void EndPointWithoutParameterReturn4xxTest() {
        String[] endPoins = {"", "Free", "fRee", "some", "paid1", "grossinG", " ", " free", "PAID"};
        Map<String, String> parameters = Collections.emptyMap();
        for (int i = 0; i < endPoins.length; i++) {
            ResponseEntity<List<Game>> resp = mainController.getListGames(endPoins[i], parameters);
            assertTrue(resp.getStatusCode().is4xxClientError());
        }
    }

    @Test
    public void EndPointWithParameterIsCorrectTest() {
        String correctParameter = "limit";
        String[] valueParam = {"1", "10", "99", "100"};
        String[] endPoins = {"free", "paid", "grossing"};
        Map<String, String> parameters = new HashMap<>();
        int[] arrayLengthRecordBase = {0, 1, 20, 99, 100, 101, 150, 200};
        int expectedSizeResponse;
        for (int b = 0; b < arrayLengthRecordBase.length; b++) {
            createBaseByLength(arrayLengthRecordBase[b]);
            for (int i = 0; i < endPoins.length; i++) {
                for (int j = 0; j < valueParam.length; j++) {
                    parameters.put(correctParameter, valueParam[j]);
                    ResponseEntity<List<Game>> resp = mainController.getListGames(endPoins[i], parameters);
                    assertTrue(resp.getStatusCode().is2xxSuccessful());
                    expectedSizeResponse = Integer.parseInt(valueParam[j]);
                    if (expectedSizeResponse > arrayLengthRecordBase[b])
                        expectedSizeResponse = arrayLengthRecordBase[b];
                    assertEquals(expectedSizeResponse, resp.getBody().size());
                }
            }
        }
    }

    @Test
    public void EndPointWithParameterValueIsNotCorrectTest() {
        String correctParameter = "limit";
        String[] valueParam = {"0", "-1", "101", "", " ", "any"};
        String[] endPoins = {"free", "paid", "grossing"};
        Map<String, String> parameters = new HashMap<>();
        int[] arrayLengthRecordBase = {0, 1, 20, 99, 100, 101, 150, 200};
        int expectedSizeResponse;
        for (int b = 0; b < arrayLengthRecordBase.length; b++) {
            createBaseByLength(arrayLengthRecordBase[b]);
            for (int i = 0; i < endPoins.length; i++) {
                for (int j = 0; j < valueParam.length; j++) {
                    parameters.put(correctParameter, valueParam[j]);
                    ResponseEntity<List<Game>> resp = mainController.getListGames(endPoins[i], parameters);
                    assertTrue(resp.getStatusCode().is2xxSuccessful());
                    expectedSizeResponse = 100;
                    if (expectedSizeResponse > arrayLengthRecordBase[b])
                        expectedSizeResponse = arrayLengthRecordBase[b];
                    assertEquals(expectedSizeResponse, resp.getBody().size());
                }
            }
        }
    }

    @Test
    public void EndPointWithParameterNameIsNotCorrectTest() throws Exception {
        String[] incorrectParameter = {"limit1", "Limit", " limit", "any", " ", "LIMIT"};
        String[] valueParam = {"0", "-1", "101", "", " ", "any", "1", "40", "100"};
        String[] endPoins = {"free", "paid", "grossing"};
        Map<String, String> parameters = new HashMap<>();
        int[] arrayLengthRecordBase = {0, 1, 20, 99, 100, 101, 150, 200};
        int expectedSizeResponse;
        for (int b = 0; b < arrayLengthRecordBase.length; b++) {
            createBaseByLength(arrayLengthRecordBase[b]);

            for (int i = 0; i < endPoins.length; i++) {
                for (int n = 0; n < incorrectParameter.length; n++) {
                    parameters.clear();
                    for (int j = 0; j < valueParam.length; j++) {
                        parameters.put(incorrectParameter[n], valueParam[j]);
                        ResponseEntity<List<Game>> resp = mainController.getListGames(endPoins[i], parameters);
                        assertTrue(resp.getStatusCode().is2xxSuccessful());
                        expectedSizeResponse = 100;
                        if (expectedSizeResponse > arrayLengthRecordBase[b])
                            expectedSizeResponse = arrayLengthRecordBase[b];
                        assertEquals(expectedSizeResponse, resp.getBody().size());
                    }
                }
            }
        }
    }

    @Test
    public void EndPointWithSeveralParametersTest() {
        String[] incorrectParameter = {"limit", "limiter", "Limit", " limit", "any", " ", "LIMIT"};
        String[] valueParam = {"0", "-1", "101", "", " ", "any", "1", "40", "100"};
        String[] endPoins = {"free", "paid", "grossing"};
        Map<String, String> parameters = new HashMap<>();
        int[] arrayLengthRecordBase = {0, 1, 20, 99, 100, 101, 150, 200};
        int expectedSizeResponse;
        for (int b = 0; b < arrayLengthRecordBase.length; b++) {
            createBaseByLength(arrayLengthRecordBase[b]);
            for (int i = 0; i < endPoins.length; i++) {
                for (int n = 0; n < incorrectParameter.length; n++) {
                    parameters.clear();
                    for (int j = 0; j < valueParam.length; j++) {
                        parameters.put(incorrectParameter[n], valueParam[j]);
                        if (n == incorrectParameter.length - 1) parameters.put(incorrectParameter[0], valueParam[j]);
                        else parameters.put(incorrectParameter[n + 1], valueParam[j]);
                        ResponseEntity<List<Game>> resp = mainController.getListGames(endPoins[i], parameters);
                        assertTrue(resp.getStatusCode().is2xxSuccessful());
                        expectedSizeResponse = 100;
                        if (expectedSizeResponse > arrayLengthRecordBase[b])
                            expectedSizeResponse = arrayLengthRecordBase[b];
                        assertEquals(expectedSizeResponse, resp.getBody().size());
                    }
                }
            }
        }
    }

    // Tests GameService
    @Test
    public void clearAllGamesBaseGameServiceTest() {
        gameService.clearAllGamesBase();
        assertEquals(0, gameService.clearAllGamesBase());

    }

    @Test
    public void addAllGamesSavedGameServiceTest() {
        int[] lengthList = {0, 1, 20, 100, 101, 202};
        for (int i = 0; i < lengthList.length; i++) {
            gameService.clearAllGamesBase();
            for (TypeGames typeGame : TypeGames.values()) {
                assertEquals(lengthList[i], gameService.addAllGames(typeGame, generateListGameByLength(lengthList[i])));
            }
        }
    }

    @Test
    public void normalizeBaseCorrectLengthGameServiceTest() {
        int[] maxLengths = {1, 20, 99, 100, 101, 150, 200};
        int[] arrayLengthRecordBase = {0, 1, 20, 99, 100, 101, 150, 200};
        for (int i = 0; i < arrayLengthRecordBase.length; i++) {
            createBaseByLength(arrayLengthRecordBase[i]);
            for (int j = 0; j < maxLengths.length; j++) {
                for (TypeGames typeGame : TypeGames.values()) {
                    assertTrue(gameService.normalizeBase(typeGame, maxLengths[j]));
                }
            }
        }
    }

    @Test
    public void normalizeBaseNotCorrectLengthGameServiceTest() {
        int[] maxLengths = {0, -1, -100};
        int[] arrayLengthRecordBase = {0, 1, 20, 99, 100, 101, 150, 200};
        for (int i = 0; i < arrayLengthRecordBase.length; i++) {
            createBaseByLength(arrayLengthRecordBase[i]);
            for (int j = 0; j < maxLengths.length; j++) {
                for (TypeGames typeGame : TypeGames.values()) {
                    assertFalse(gameService.normalizeBase(typeGame, maxLengths[j]));
                }
            }
        }
    }

    @Test
    public void getListGameLengthCorrectLengthGameServiceTest() {
        int[] getListLengths = {1, 20, 99, 100, 101, 150, 200};
        int[] arrayLengthRecordBase = {0, 1, 20, 99, 100, 101, 150, 200};
        int sizeExpected;
        for (int i = 0; i < arrayLengthRecordBase.length; i++) {
            createBaseByLength(arrayLengthRecordBase[i]);
            for (int j = 0; j < getListLengths.length; j++) {
                for (TypeGames typeGame : TypeGames.values()) {
                    sizeExpected = getListLengths[j];
                    if (getListLengths[j] > arrayLengthRecordBase[i]) sizeExpected = arrayLengthRecordBase[i];
                    assertEquals(sizeExpected, gameService.getListGameLength(typeGame, getListLengths[j]).size());
                }
            }
        }
    }

    @Test
    public void getListGameLengthNotCorrectLengthGameServiceTest() {
        int[] getListLengths = {0, -20, -1};
        int[] arrayLengthRecordBase = {0, 1, 20, 99, 100, 101, 150, 200};
        for (int i = 0; i < arrayLengthRecordBase.length; i++) {
            createBaseByLength(arrayLengthRecordBase[i]);
            for (int j = 0; j < getListLengths.length; j++) {
                for (TypeGames typeGame : TypeGames.values()) {
                    assertEquals(0, gameService.getListGameLength(typeGame, getListLengths[j]).size());
                }
            }
        }
    }

    private List<Game> generateListGameByLength(int length) {
        List<Game> gameList = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            Game game = new Game();
            game.setName(String.valueOf(i));
            gameList.add(game);
        }
        return gameList;
    }

    private void createBaseByLength(int lengthRecover) {
        gameService.clearAllGamesBase();
        List<Game> testListGames = generateListGameByLength(lengthRecover);
        for (TypeGames typeGame : TypeGames.values()) {
            gameService.addAllGames(typeGame, testListGames);
        }
    }

}
