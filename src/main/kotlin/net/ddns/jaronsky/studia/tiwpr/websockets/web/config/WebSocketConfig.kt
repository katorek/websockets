package net.ddns.jaronsky.studia.tiwpr.websockets.web.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.messaging.simp.config.MessageBrokerRegistry
import org.springframework.web.socket.config.annotation.*
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean


@Configuration
@EnableWebSocket
@EnableWebSocketMessageBroker
class WebSocketConfig: WebSocketConfigurer, AbstractWebSocketMessageBrokerConfigurer() {

    override fun configureMessageBroker(registry: MessageBrokerRegistry) {
        registry.enableSimpleBroker("/ttt")
        registry.setApplicationDestinationPrefixes("/ttt")
//        super.configureMessageBroker(registry)
    }

    override fun registerStompEndpoints(registry: StompEndpointRegistry) {
        registry.addEndpoint("/ttt-websocket").withSockJS()
    }

    @Bean
    fun createWebSocketContainer(): ServletServerContainerFactoryBean {
        val container = ServletServerContainerFactoryBean()
        container.setMaxBinaryMessageBufferSize(1024000)
        return container
    }

    @Override
    override fun registerWebSocketHandlers(registry: WebSocketHandlerRegistry) {
        registry.addHandler(WebSocketHandler(), "/socket").setAllowedOrigins("*")
    }

}