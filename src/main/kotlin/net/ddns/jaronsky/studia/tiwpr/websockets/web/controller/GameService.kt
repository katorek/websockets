package net.ddns.jaronsky.studia.tiwpr.websockets.web.controller

import net.ddns.jaronsky.studia.tiwpr.websockets.web.model.AvailableGame
import net.ddns.jaronsky.studia.tiwpr.websockets.web.model.GameState
import org.springframework.stereotype.Service
import java.util.concurrent.ConcurrentHashMap
import kotlin.streams.toList

@Service
class GameService(
//        val template: SimpMessagingTemplate
) {
    var nextId = 0
    val games = ConcurrentHashMap<Int, GameState>()

    fun createGame(name: String, player: String): GameState {

        val newGameAvailabelName = availableGameName(name)

        val newGame = GameState(nextId, newGameAvailabelName, player)

        games.putIfAbsent(newGame.id, newGame)
        nextId++

        return newGame
    }

    private fun availableGameName(name: String): String {
        val names = games.values.stream().map { it.name }.toList()
        if(names.contains(name)) {
            var i = 1
            while(names.contains("${name}_$i")){
                i++
            }
            return "${name}_$i"
        }
        return name
    }

    fun getById(id: Int): GameState {
        return games.get(id)!!
    }

    fun update(id: Int, game: GameState) {
        games.put(id, game)
    }

    fun updateGameState(id: Int, game: GameState) {
//        template.convertAndSend("/ttt/gamestate/$id", game)

    }

    fun availableGames(): List<AvailableGame> {
//        return games.stream().filter { g -> !g.started && !g.disconnect }.map { g -> AvailableGame(g.id, g.name) }
        return games.map(Map.Entry<Int, GameState>::value)
                .stream().filter { g -> !g.started && !g.disconnect }
                .map { g -> AvailableGame(g.id, g.name) }
                .toList()
    }

    fun disconnect(id: Int, player: String, disconnect: Boolean): GameState? {
        if (disconnect) {
            val game = getById(id)
            game.disconnect(player)
            update(game)
            return game
        }
        return null
    }

    @Synchronized
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

    fun findByPlayer(playerName: String?): GameState {
        return games.entries.stream()
                .map(MutableMap.MutableEntry<Int, GameState>::value)
                .filter { it.player1.equals(playerName) || it.player2.equals(playerName) }
                .sorted(Comparator.comparing(GameState::date))
                .findFirst().get()
    }

    fun update(game: GameState) {
        if(game.disconnect) {
            games.remove(game.id)
        } else {
            update(game.id, game)
        }
    }

    fun findByPlayerById(playerId: String?): GameState {
        return games.entries.stream()
                .map(MutableMap.MutableEntry<Int, GameState>::value)
                .filter { it.player1Id.equals(playerId) || it.player2Id.equals(playerId) }
                .sorted(Comparator.comparing(GameState::date))
                .findFirst().get()
    }

    fun getByIdIfExist(id: Int): GameState? {
        return games.get(id)
    }


}