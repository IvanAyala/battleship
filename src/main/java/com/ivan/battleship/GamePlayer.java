package com.ivan.battleship;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
public class GamePlayer {




    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long id;

    private Date creationDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="player_id")
    private Player player;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="game_id")
    private Game game;

    @OneToMany(mappedBy = "gamePlayer", fetch = FetchType.EAGER)
    private Set<Ship> ships = new HashSet<>();

    @OneToMany(mappedBy = "gamePlayer", fetch = FetchType.EAGER)
    private Set<Salvoes> salvoes = new HashSet<>();

    public GamePlayer (){}

    public GamePlayer (Game game, Player player) { this(new Date(), game, player);}

    public GamePlayer (Date creationDate, Game game, Player player) {
        this.creationDate = creationDate;
        this.game = game;
        this.player = player;
    };

    public long getId() {
        return id;
    }

    @JsonIgnore
    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    @JsonIgnore
    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    @JsonIgnore
    public Set<Ship> getShips(){
        return this.ships;
    }

    @JsonIgnore
    public Set<Salvoes> getSalvoes(){
        return this.salvoes;
    }

    @JsonIgnore
    public Score getScore (){
        return player.getScore(game);
    }



}
