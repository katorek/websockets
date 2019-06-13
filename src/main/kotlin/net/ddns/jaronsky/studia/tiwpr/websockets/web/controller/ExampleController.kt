package net.ddns.jaronsky.studia.tiwpr.websockets.web.controller

import net.ddns.jaronsky.studia.tiwpr.websockets.web.model.Greeting
import net.ddns.jaronsky.studia.tiwpr.websockets.web.model.HelloMessage
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.util.HtmlUtils

@Controller
class ExampleController {

    @GetMapping("/hello2")
    fun helloWorld(): String {
        return "Hello World !"
    }

    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    fun greeting(msg: HelloMessage): Greeting {
        return Greeting("Hello, ${HtmlUtils.htmlEscape(msg.name)}!" )
    }
}