package net.ddns.jaronsky.studia.tiwpr.websockets.web.config

import net.ddns.jaronsky.studia.tiwpr.websockets.web.controller.GameService
import net.ddns.jaronsky.studia.tiwpr.websockets.web.model.GameState
import org.springframework.web.socket.BinaryMessage
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.AbstractWebSocketHandler
import java.io.IOException
import java.nio.charset.Charset
import java.util.concurrent.ConcurrentHashMap

class WebSocketHandler(
        val gameService: GameService
) : AbstractWebSocketHandler() {

    val sessionsGames = ConcurrentHashMap<String, Int>()
    val sessions = ConcurrentHashMap<String, WebSocketSession>()

    @Throws(IOException::class)
    override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        session.sendMessage(message)
    }

    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
        val game = gameService.getByIdIfExist(sessionsGames.get(session.id)!!)
        if (game != null) {
            sendMsgToPlayers(game, "DISCONNECT")
            gameService.update(game)
        }
    }

    @Throws(IOException::class)
    override fun handleBinaryMessage(session: WebSocketSession, message: BinaryMessage) {
        val decoded = Charset.forName("ISO-8859-1").decode(message.payload).toString()

        when {
            decoded.startsWith("JOIN") -> {
                val tokens = decoded.split(':')
                val gameId = tokens[1].toInt()
                val game = gameService.getById(gameId)
                val playerId = tokens[2]
                val playerNum = tokens[3].toInt()
                game.websocketJoin(playerNum, playerId)
                gameService.update(game)
                sessionsGames.put(session.id, game.id)
                sessions.put(playerId, session)

                if (game.started) {
                    sendMsgToPlayers(game, "STARTED")
                }
            }
            decoded.startsWith("MOVE") -> {
                val tokens = decoded.split(":")
                val playerNum = tokens[1]
                val column = tokens[2]
                val game = gameService.getById(sessionsGames.get(session.id)!!)
                game.makeMove(column.toInt(), playerNum)
                gameService.update(game)
                sendMsgToPlayers(game, game.toMsgString())
                when {
                    game.winner !== "" -> sendMsgToPlayers(game, "WINNER:${getWinner(game.winner)}")
                    game.draw -> sendMsgToPlayers(game, "DRAW")
                }
            }
            decoded.startsWith("DISCONNECT") -> {
                val tokens = decoded.split(":")
                val gid = tokens[1]
                val uid = tokens[2]
                val game = gameService.getByIdIfExist(sessionsGames.get(session.id)!!)
                if (game != null) {
                    sendMsgToPlayers(game, "DISCONNECT")
                    game.disconnectById(uid)
                    gameService.update(game)
                    sessions.remove(uid)
                }
            }
            decoded.startsWith("STATE") -> {
                val tokens = decoded.split(":")
                val gid = tokens[1]
                val game = gameService.getByIdIfExist(gid.toInt())
                sessionsGames.put(session.id, gid.toInt())
                if (game != null) {
                    when {
                        game.started -> sendMsgToPlayers(game, game.toMsgString())
                        game.winner !== "" -> sendMsgToPlayers(game, "WINNER:${getWinner(game.winner)}")
                        game.draw -> sendMsgToPlayers(game, "DRAW")
                    }
                }
            }
        }
    }

    private fun getWinner(winner: String): String {
        when (winner) {
            "zielony" -> return "1"
            "zolty" -> return "2"
        }
        return "0"
    }

    private fun sendMsgToPlayers(game: GameState, msg: String) {
        val uid1 = game.player1Id
        val uid2 = game.player2Id
        println("U:[$uid1, $uid2], MSG: $msg")
        val binaryMsg = BinaryMessage(msg.toByteArray())

        val s1 = sessions.get(uid1)
        val s2 = sessions.get(uid2)

        if (isSessionOpen(s1)) s1?.sendMessage(binaryMsg)
        else sessions.remove(uid1)
        if (isSessionOpen(s2)) s2?.sendMessage(binaryMsg)
        else sessions.remove(uid2)
    }

    private fun isSessionOpen(session: WebSocketSession?): Boolean {
        return session != null && session.isOpen
    }

}