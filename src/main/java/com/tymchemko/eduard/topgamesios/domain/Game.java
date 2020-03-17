package com.tymchemko.eduard.topgamesios.domain;

import lombok.Data;
import java.io.Serializable;

@Data
public class Game implements Serializable {
    private String name;
    private String url;
    private String releaseDate;
    private String artistName;
}
