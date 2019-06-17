package net.ddns.jaronsky.studia.tiwpr.websockets.web.controller

import net.ddns.jaronsky.studia.tiwpr.websockets.web.model.GameState
import org.springframework.stereotype.Service

@Service
class GameService {
    val games = mutableListOf<GameState>()

    fun createGame(name: String, player: String): GameState {
        val x = GameState(name, player)

        return GameState(name, player)
    }

    fun getById(id: Int): GameState? {
        return games.find { game -> game.id == id }
    }

    fun update(game: GameState) {

    }


}