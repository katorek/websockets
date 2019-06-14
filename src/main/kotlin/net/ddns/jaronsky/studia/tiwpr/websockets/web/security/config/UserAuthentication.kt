package net.ddns.jaronsky.studia.tiwpr.websockets.web.security.config

import net.ddns.jaronsky.studia.tiwpr.websockets.web.security.model.UserFactory
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService

class UserAuthentication: UserDetailsService {
    override fun loadUserByUsername(p0: String): UserDetails {
        println("Logged in: $p0")
        return UserFactory(p0)
    }
}