package net.ddns.jaronsky.studia.tiwpr.websockets.web.config

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.CorsRegistry


@Configuration
@EnableWebMvc
class WebMvcConfiguration: WebMvcConfigurer {

    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**")
        println("ALA MA KOTA A KOT MA ALE, ALE ALA NIE MA ALI BO KOTA ALI NIE MA PO KOPALI")
    }
}