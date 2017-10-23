$(document).ready(function() {

    var player;

$("#logoutButton").on("click", function(){
    $.post("/api/logout",function(){
        window.location.href = "/web/login.html";
    })
});

$(document).on("click", "tr.own-game",  function(){
    window.location.href = "/web/game.html?gp=" + $(this).data("gameplayer-id");
})

$(document).on("click", ".joinBtn", function(){
    console.log("clicked")

    var gameID = $(this).closest("tr").data("game-id");

    $.post("/api/games/" + gameID + "/players").done(function(data){
        window.location.href = "/web/game.html?gp=" + data.id
    }).fail(function(){
        console.log("fail")
    })
});


$("#newGameButton").on("click",function(){
    $.post("/api/games").done(function(data){
        window.location.href = "/web/game.html?gp=" + data.id
    }).fail(function(){
        alert("something went wrong..")
    })
})




function getGames(){


    $.get("/api/games").done(function(data){
        console.log(data)

        player = data.player;



        $('.username').text(player.name);

        var games = data.games.reverse();

        var gameRows = []
        $.each(games, function(i,game){
           var tr = $("<tr>").attr( "data-game-id", game.id );

           var gameplayerID = false;

           tr.append( $("<td>").text( moment(game.created).fromNow() ) )


           tr.append( $("<td>").text( game.gamePlayers[0].player.username ) )

           if( game.gamePlayers[0].player.id == player.id){
            gameplayerID = game.gamePlayers[0].id;
           }

           if (game.gamePlayers[1]){
             tr.append( $("<td>").text( game.gamePlayers[1].player.username ) )
             if( game.gamePlayers[1].player.id == player.id){
              gameplayerID = game.gamePlayers[1].id;
             }
           } else {

              var btn = $("<button>").text("Join").addClass("btn btn-outline-danger btn-sm joinBtn")
              if(gameplayerID) btn = "";
              tr.append( $("<td>").append( btn ))

           }
           tr.append( $("<td>").text("-") )

           if(gameplayerID){
             tr.addClass("own-game").attr("data-gameplayer-id", gameplayerID)
           }

           gameRows.push(tr);
           // push table row into gameRows
        });

        $("#gamesTable tbody").html(gameRows);
        // add gameRows to the table

        getPlayers();

    }).fail(function(data){
        window.location.href = "/web/login.html"
    })


}

function getPlayers() {
    $.ajax({
  url: "/api/players",
  success: function(data) {
    console.log(data)

    data = data.sort(function(a, b) {
      return b.totalScore - a.totalScore;
    });

    var tablebody = $('#players tbody');

    data.forEach(function(player, i) {

      var row = $('<tr>');
      $('<td>').text(player.username).appendTo(row);
      $('<td>').text(player.totalScore).appendTo(row);
      $('<td>').text(player.wins).appendTo(row);
      $('<td>').text(player.lost).appendTo(row);
      $('<td>').text(player.ties).appendTo(row);

      tablebody.append(row);

    });




  }

})
}


getGames();

});