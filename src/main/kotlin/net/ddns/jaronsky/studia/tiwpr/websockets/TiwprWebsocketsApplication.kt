package net.ddns.jaronsky.studia.tiwpr.websockets

import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class TiwprWebsocketsApplication

fun main(args: Array<String>) {
    runApplication<TiwprWebsocketsApplication>(*args)
}
