package net.ddns.jaronsky.studia.tiwpr.websockets.web.config

import net.ddns.jaronsky.studia.tiwpr.websockets.web.controller.GameService
import org.springframework.context.ApplicationListener
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.messaging.simp.stomp.StompHeaderAccessor
import org.springframework.stereotype.Component
import org.springframework.web.socket.messaging.SessionDisconnectEvent

@Component
class SessionDisconnectEventListener(
        val gameService: GameService,
        val template: SimpMessagingTemplate
): ApplicationListener<SessionDisconnectEvent> {



    override fun onApplicationEvent(p0: SessionDisconnectEvent) {
        val headerAccessor = StompHeaderAccessor.wrap(p0.getMessage())

        val playerId = headerAccessor.sessionId

        val game = gameService.findByPlayer(playerId)

//        val game = repository.findTopByPlayer1IdOrPlayer2IdOrderByCreatedDesc(playerId, playerId)
        // todo disconnect
//        game.disconnect(playerId)
        gameService.update(game)
//        repository.save(game)
        template.convertAndSend("/ttt/gamestate/" + game.id, game)
    }
}