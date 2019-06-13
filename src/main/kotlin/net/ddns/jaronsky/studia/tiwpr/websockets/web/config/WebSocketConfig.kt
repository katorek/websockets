package net.ddns.jaronsky.studia.tiwpr.websockets.web.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.socket.config.annotation.*
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean






@Configuration
@EnableWebSocket
//@EnableWebSocketMessageBroker
//class WebSocketConfig: AbstractWebSocketHandler() {
class WebSocketConfig:  WebSocketConfigurer {
//class WebSocketConfig: WebSocketMessageBrokerConfigurer {

    @Bean
    fun createWebSocketContainer(): ServletServerContainerFactoryBean {
        val container = ServletServerContainerFactoryBean()
        container.setMaxBinaryMessageBufferSize(1024000)
        return container
    }

    @Override
    override fun registerWebSocketHandlers(registry: WebSocketHandlerRegistry) {
        registry.addHandler(WebSocketHandler(), "/socket").setAllowedOrigins("*")
//        registry.addHandler(MyBinaryHandler(), "/binary").withSockJS()
    }



}