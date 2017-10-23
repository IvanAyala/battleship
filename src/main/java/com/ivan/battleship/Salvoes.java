package com.ivan.battleship;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Salvoes {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private int round;


    @ElementCollection
    @Column(name = "fireLocations")
    private List<String> fireLocations = new ArrayList<>();



    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name= "gamePlayer_id")
    private GamePlayer gamePlayer;


    public Salvoes(){}

    public Salvoes (int round, GamePlayer gamePlayer, List fireLocation) {

        this.round = round;
        this.gamePlayer = gamePlayer;
        this.fireLocations = fireLocation;

    }


    @JsonIgnore
    public long getId() {
        return id;

    }

    @JsonIgnore
    public GamePlayer getGamePlayer() {return gamePlayer; }


    public int getRound() {
        return round;
    }

    public void setRound(int round) {
        this.round = round;
    }


    public List<String> getFireLocations() {
        return fireLocations;
    }

    public void setFireLocations(List<String> fireLocations) {
        this.fireLocations = fireLocations;
    }


    public void setGamePlayer(GamePlayer gamePlayer) {
        this.gamePlayer = gamePlayer;
    }



}
