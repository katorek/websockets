package net.ddns.jaronsky.studia.tiwpr.websockets.web.model

import net.ddns.jaronsky.studia.tiwpr.websockets.game.model.Game
import java.util.*

class GameState(
        val id: Int,
        val name: String,
        var player1: String
) {
    //    companion object {
//        var id = 0
//        fun nextVal(): Int {
//            return id++
//        }
//    }
//    var id: Int = 0
    var player2: String? = null
    var currentPlayer: String = player1
    var winner: String = ""
    var started = false
    var draw = false
    var disconnect = false
    val game = Game()
    val date = Date()

    var player1Id: String = ""
    var player2Id: String = ""


    fun join(pl: String) {
        if (player2 == null) {
            player2 = pl
            started = true
        }
    }

    fun disconnect(pl: String) {

    }

    fun getName(i: Any): String {
        return if (i == 1) "zielony" else "zolty";
    }

    fun websocketJoin(player: Any, playerId: String) {
        val playerName = getName(player)
        if (playerName.equals(player1)) {
            player1Id = playerId
        } else if (playerName.equals(player2)) {
            player2Id = playerId
        }
    }

    fun makeMove(column: Int, player: String) {
        if (game.getPossibleMoves().contains(column)) {
            if (currentPlayer == getName(player.toInt())){
                game.makeMove(column, player.toInt())
                swapCurrentPlayer();
            }
        }
        //todo check if winner
        val isWin = game.board.testWin()
        if(isWin.first) {
            winner = getName(isWin.second)
        }

        if(game.getPossibleMoves().isEmpty()) {
            draw = true
        }
    }

    fun disconnectById(playerId: String) {
        if (isValidPlayerId(playerId)) {
            disconnect = true
            if(playerId == player1Id) {
                player1Id = ""
            } else {
                player2Id = ""
            }
        }
    }

    private fun isValidPlayerId(playerId: String): Boolean {
        return playerId == player1Id || playerId == player2Id
    }

    private fun swapCurrentPlayer() {
        if (currentPlayer == player1) {
            currentPlayer = player2!!
        } else {
            currentPlayer = player1
        }
    }

    fun toMsgString(): String {
        return game.toString() + if (currentPlayer == "zielony") 1 else 2;
    }
}