package net.ddns.jaronsky.studia.tiwpr.websockets.web.controller

import net.ddns.jaronsky.studia.tiwpr.websockets.web.model.AvailableGame
import net.ddns.jaronsky.studia.tiwpr.websockets.web.model.GameState
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.web.bind.annotation.*
import java.util.stream.Stream

@RestController
@RequestMapping("/ttt/game")
class ServerController(
        val gameService: GameService
) {

    @GetMapping
    fun getGames(): List<AvailableGame> {
        return gameService.availableGames()
    }

    @PostMapping
    fun createGame(
            @RequestParam(value = "player") player: String,
            @RequestParam(value = "name") name: String
    ): GameState {
        return gameService.createGame(name, player)
    }

    @PatchMapping(params = arrayOf("id", "player"))
    fun joinGame(
            @RequestParam(value = "id") id: Int,
            @RequestParam(value = "player") player: String
    ): GameState? {
        return gameService.joinGame(id, player)
    }

    @PatchMapping(params = arrayOf("id", "player", "disconnect"))
    fun disconnectGame(
            @RequestParam("id") id: Int,
            @RequestParam("player") player: String,
            @RequestParam("disconnect") disconnect: Boolean): GameState? {
        return gameService.disconnect(id, player, disconnect)
    }

    private fun updateGame(id: Int, game: GameState) {
//        gameService.updateGameState(id, game)
    }

}