package net.ddns.jaronsky.studia.tiwpr.websockets.web.controller

import net.ddns.jaronsky.studia.tiwpr.websockets.web.model.AvailableGame
import net.ddns.jaronsky.studia.tiwpr.websockets.web.model.GameState
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Service
import java.util.stream.Stream

@Service
class GameService(
        val template: SimpMessagingTemplate
) {
    val games = mutableMapOf<Int, GameState>()

    fun createGame(name: String, player: String): GameState {
        val newGame = GameState(name, player)
        games.put(newGame.id, newGame)

        return newGame
    }

    fun getById(id: Int): GameState {
        return games.get(id)!!
    }

    fun update(id: Int, game: GameState) {
        games.put(id, game)
    }

    fun updateGameState(id: Int, game: GameState) {
        template.convertAndSend("/ttt/gamestate/$id", game)

    }

    fun availableGames(): Stream<AvailableGame>? {
//        return games.stream().filter { g -> !g.started && !g.disconnect }.map { g -> AvailableGame(g.id, g.name) }
        return games.map(Map.Entry<Int, GameState>::value)
                .stream().filter { g -> !g.started && !g.disconnect }
                .map { g -> AvailableGame(g.id, g.name) }
    }

    fun disconnect(id: Int, player: String, disconnect: Boolean): GameState? {
        if (disconnect) {
            val game = getById(id)
            game.disconnect(player)
            updateGameState(id, game)
            return game
        }
        return null
    }

    fun joinGame(id: Int, player: String): GameState? {
        val game = getById(id)

        if (!game.started && !game.disconnect) {
            game.join(player)
            update(id, game)
            updateGameState(id, game)
            return game
        }

        return null
    }

    fun findByPlayer(playerId: String?): GameState {
        return games.entries.stream()
                .map(MutableMap.MutableEntry<Int, GameState>::value)
                .filter { it.player1.equals(playerId) || it.player2.equals(playerId) }
                .sorted(Comparator.comparing(GameState::date))
                .findFirst().get()
    }

    fun update(game: GameState) {
        return update(game.id, game)
    }


}