package com.ivan.battleship;


import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;

@Entity
public class Game {



    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long id;

    private Date creationDate;

    public Set<GamePlayer> getGamePlayers() {
        return gamePlayers;
    }

    @OneToMany(mappedBy="game", fetch=FetchType.EAGER)
    Set<GamePlayer> gamePlayers;

    @OneToMany(mappedBy="game", fetch=FetchType.EAGER)
    List<GamePlayer> score;


    public Game(){
        this.creationDate = new Date();
    }

    public Game(Date creationDate){
            this.creationDate = creationDate;
    }

    public long getId() {
        return id;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public List<Player> getPlayers() {
        return gamePlayers.stream().map(sub -> sub.getPlayer()).collect(toList());
    }


}
