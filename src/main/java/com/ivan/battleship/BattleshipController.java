package com.ivan.battleship;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collector;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class BattleshipController {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private GamePlayerRepository gamePlayerRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private ShipRepository shipRepository;

    @Autowired
    private SalvoesRepository salvoesRepository;

    private Player getAuthPlayer(Authentication authentication) {
        String userName = authentication.getName();
        List<Player> players = playerRepository.findByUserName(userName);
        return players.stream().findFirst().orElse(null);
    }


    @RequestMapping("/game_view/{gamePlayerId}")
    public Map<String, Object> getGame(@PathVariable long gamePlayerId) {
        GamePlayer currentGamePlayer = gamePlayerRepository.findOne(gamePlayerId);
        Game currentGame = currentGamePlayer.getGame();

        Map<String, Object> response = new HashMap<>();

        response.put("id", currentGame.getId());
        response.put("created", currentGame.getCreationDate());
        response.put("gamePlayers", currentGame.getGamePlayers());
        response.put("ships", currentGamePlayer.getShips());

        Map<String, Object> salvoes = new HashMap<>();
        currentGame.getGamePlayers().forEach(gamePlayer -> {
            salvoes.put(String.valueOf(gamePlayer.getId()), gamePlayer.getSalvoes());
        });

        response.put("salvoes", salvoes);
        return response;
    }



    @RequestMapping("/games")
    public Map<String, Object> getGames(Authentication authentication) {


        //public List getGames() {
        //List<Game> allGames = gameRepository.findAll();

        // HashMap son lo que esta en los {}, las propiedades Array/HashMap/Array/HashMap y asi
        //gameArray is the most external container, an array
        //List<HashMap> gameArray = new ArrayList();
        //it loops allGames.
        // In the Map for each game, put keys and values for the game ID, creation date, and gamePlayers.

        //for(int i = 0; i < allGames.size(); i++){
        //  Game game = allGames.get(i);

        Player authPlayer = getAuthPlayer(authentication);
        HashMap<String, Object> returnMap = new HashMap<>();

        HashMap<String, Object> authPlayerMap = new HashMap<>();
        authPlayerMap.put("id", authPlayer.getId());
        authPlayerMap.put("userName", authPlayer.getUserName());

        List<Object> gamesList = new ArrayList();
        List<Game> allGames = gameRepository.findAll();

        allGames.forEach(game -> {

            HashMap<String, Object> gameMap = new HashMap<>();
            gameMap.put("id", game.getId());
            gameMap.put("created", game.getCreationDate());

            List<Map> gamePlayerList = new ArrayList();
            Set<GamePlayer> gamePlayers = game.getGamePlayers();

            gamePlayers.forEach(gamePlayer -> {

                HashMap<String, Object> gamePlayerMap = new HashMap<>();
                gamePlayerMap.put("id", gamePlayer.getId());

                //abajo. This is to get the player into a var but not putting in the map
                Player player = gamePlayer.getPlayer();


                HashMap<String, Object> playerMap = new HashMap<>();
                playerMap.put("id", player.getId());
                playerMap.put("email", player.getUserName());
                playerMap.put("score", gamePlayer.getScore());


                gamePlayerMap.put("player", playerMap);
                gamePlayerList.add(gamePlayerMap);

            });


            gameMap.put("gamePlayers", gamePlayerList);

            gamesList.add(gameMap);

        }); // END ALL GAMES LOOP

        returnMap.put("games", gamesList);
        returnMap.put("player", authPlayerMap);

        return returnMap;

        //return gameArray;
        //return gameRepository.findAll().stream().map(game -> game.getId()).collect(toList());
    }

    @RequestMapping(value = "/games", method = RequestMethod.POST)
    public GamePlayer createGame(Authentication authentication) {

        Player authPlayer = getAuthPlayer(authentication);

        Game newGame = new Game();
        gameRepository.save(newGame);

        GamePlayer newGameplayer = new GamePlayer(newGame, authPlayer);
        gamePlayerRepository.save(newGameplayer);

        return newGameplayer;
    }

    @RequestMapping(value = "/games/{gameID}/players", method = RequestMethod.POST)
    public GamePlayer joinGame(Authentication authentication, @PathVariable long gameID) {

        Player authPlayer = getAuthPlayer(authentication);

        Game openGame = gameRepository.findOne(gameID);

        GamePlayer newGameplayer = new GamePlayer(openGame, authPlayer);
        gamePlayerRepository.save(newGameplayer);

        return newGameplayer;
    }


    @RequestMapping(value = "/players", method = RequestMethod.POST)
    public ResponseEntity postPlayer(@RequestParam String username, String password) {

        HashMap<String, Object> response = new HashMap<>();

        List<Player> repoPlayer = playerRepository.findByUserName(username);

        if (repoPlayer.size() > 0) {
            response.put("error", "Name in use");
            return new ResponseEntity(response, HttpStatus.FORBIDDEN);
        } else {
            Player newplayer = new Player(username, password);
            playerRepository.save(newplayer);

            response.put("username", username);
            response.put("id", newplayer.getId());
            return new ResponseEntity(response, HttpStatus.CREATED);
        }
    }

    @RequestMapping(value = "/players",method = RequestMethod.GET)
    public List<Map> getPlayer() {

        List<Map> returnArray = new ArrayList<Map>();

        List<Player> players = playerRepository.findAll();


        players.forEach(player -> {
            HashMap<String, Object> playerMap = new HashMap<>();

            playerMap.put("totalScore", player.getTotalScore());
            playerMap.put("playerid", player.getId());
            playerMap.put("username", player.getUserName());

            playerMap.put("wins", player.countWins());
            playerMap.put("ties", player.countTie());
            playerMap.put("lost", player.countLose());


            returnArray.add(playerMap);
        });


        return returnArray;

    }
}
