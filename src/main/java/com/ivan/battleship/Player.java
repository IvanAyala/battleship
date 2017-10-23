package com.ivan.battleship;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;

@Entity
public class Player {


    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long id;

    @OneToMany(mappedBy="player", fetch=FetchType.EAGER)
    private Set<GamePlayer> gamePlayers;

    @OneToMany(mappedBy="player", fetch=FetchType.EAGER)
    private Set<Score> scores;

    private String userName;

    private String password;



    public Player (){

    }


    public Player (String userName){
        this.userName = userName;
    }
    public Player(String userName, String password){

        this.userName = userName;
        this.password = password;
    }

    @JsonIgnore
    public List<Game> getGames() {return gamePlayers.stream().map(sub -> sub.getGame()).collect(toList());}

    public String getUserName() {
        return userName;
    }

    public void setUserName(String newUserName) {
        this.userName = newUserName;
    }

    public long getId() {
        return id;
    }

    @JsonIgnore
    public Score getScore(Game game) {

        return scores.stream().filter( score -> game.equals(score.getGame()) )
                .findFirst()
                .orElse(null);
    }

    public Set<Score> getScores (){return scores;}

    public double getTotalScore(){

        return scores.stream().mapToDouble(score -> score.getScore()).sum();
    }



    public int countPoints(double points){
        return (int) scores.stream().filter(s -> s.getScore() == points).count();
    }

    public int countWins(){
        return countPoints(Score.SCORE_WINNER);
    }

    public int countTie(){
        return countPoints(Score.SCORE_TIE);
    }

    public int countLose(){
        return countPoints(Score.SCORE_LOSE);
    }

    @JsonIgnore
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


}