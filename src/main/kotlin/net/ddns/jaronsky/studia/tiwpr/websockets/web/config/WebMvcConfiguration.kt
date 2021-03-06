package net.ddns.jaronsky.studia.tiwpr.websockets.web.config

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry




@Configuration
//@EnableWebMvc
class WebMvcConsfiguration: WebMvcConfigurer {

    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/")
        registry.addResourceHandler("/webjars/**").addResourceLocations("/webjars/")
    }


//    override fun addCorsMappings(registry: CorsRegistry) {
//        registry.addMapping("/**").allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE")
//        println("ALA MA KOTA A KOT MA ALE, ALE ALA NIE MA ALI BO KOTA ALI NIE MA PO KOPALI -------------")
//    }
}