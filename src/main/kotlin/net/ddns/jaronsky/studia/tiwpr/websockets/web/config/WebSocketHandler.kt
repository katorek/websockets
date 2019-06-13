package net.ddns.jaronsky.studia.tiwpr.websockets.web.config

import org.springframework.web.socket.BinaryMessage
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.AbstractWebSocketHandler
import java.io.IOException

class WebSocketHandler : AbstractWebSocketHandler() {

    @Throws(IOException::class)
    override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        println("New Text Message Received")
        session.sendMessage(message)
    }

    @Throws(IOException::class)
    override fun handleBinaryMessage(session: WebSocketSession, message: BinaryMessage) {
        println("New Binary Message Received")
        session.sendMessage(message)
    }

}