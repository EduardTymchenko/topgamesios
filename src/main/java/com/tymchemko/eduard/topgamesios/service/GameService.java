package com.tymchemko.eduard.topgamesios.service;

import com.tymchemko.eduard.topgamesios.domain.Game;
import com.tymchemko.eduard.topgamesios.domain.TypeGames;

import java.util.List;

public interface GameService {
    long addAllGames(TypeGames typeGame, List<Game> gameList);

    boolean normalizeBase(TypeGames typeGame, long maxLength);

    List<Game> getListGameLength(TypeGames typeGame, long length);

    //For debugging logic and tests
    long clearAllGamesBase();
}
