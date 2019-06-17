package net.ddns.jaronsky.studia.tiwpr.websockets.web.model

import net.ddns.jaronsky.studia.tiwpr.websockets.game.model.Board
import net.ddns.jaronsky.studia.tiwpr.websockets.game.model.Game

class GameState(
        val name: String,
        val player1: String
) {
    companion object {
        var id = 0
        fun nextVal(): Int {
            return id++
        }
    }

    val id = nextVal()
    var player2: String? = null
    var currentPlayer: String = player1
    var winner: String = ""
    var started = false
    var draw = false
    var disconnect = false
    val game = Game()


    fun join(pl: String) {
        if (player2 == null) {
            player2 = pl
            started = true
        }
    }
}