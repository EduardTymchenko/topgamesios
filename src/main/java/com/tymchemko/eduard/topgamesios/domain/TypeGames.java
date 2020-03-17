package com.tymchemko.eduard.topgamesios.domain;

public enum TypeGames {
    FREE("https://rss.itunes.apple.com/api/v1/ua/ios-apps/top-free/games/100/explicit.json"),
    PAID("https://rss.itunes.apple.com/api/v1/ua/ios-apps/top-paid/games/100/explicit.json"),
    GROSSING("https://rss.itunes.apple.com/api/v1/ua/ios-apps/top-grossing/all/100/explicit.json");

    private String urlService;

    TypeGames(String urlService) {
        this.urlService = urlService;
    }

    public String getUrlService() {
        return urlService;
    }
}
