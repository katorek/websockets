package net.ddns.jaronsky.studia.tiwpr.websockets.web.controller

import net.ddns.jaronsky.studia.tiwpr.websockets.web.model.GameState
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class ServerController(
        val gameService: GameService,
        val template: SimpMessagingTemplate
) {


    @PostMapping("/game")
    fun createGame(
            @RequestParam(value = "player") player: String,
            @RequestParam(value = "name") name: String
    ): GameState {
        val game = gameService.createGame(player, name)
        return game
    }

    @PatchMapping("/game")
    fun joinGame(
            @RequestParam(value = "id") id: Int,
            @RequestParam(value = "player") player: String
    ) : GameState? {
        val game = gameService.getById(id)!!

        if( !game.started && !game.disconnect) {
            game.join(player)
            gameService.update(game)
            updateGame(id, game)
            return game
        }

        return null
    }

    private fun updateGame(id: Int, game: GameState) {

    }

}