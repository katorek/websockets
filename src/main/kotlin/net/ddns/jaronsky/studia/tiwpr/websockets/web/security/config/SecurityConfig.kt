package net.ddns.jaronsky.studia.tiwpr.websockets.web.security.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Configurable
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.DelegatingPasswordEncoder
import org.springframework.security.crypto.password.NoOpPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder

@Configuration
@EnableWebSecurity
class SecurityConfig : WebSecurityConfigurerAdapter() {

    @Bean
    override fun userDetailsService(): UserDetailsService {
        return UserAuthentication()
    }

    @Autowired
    fun configureGlobal(auth: AuthenticationManagerBuilder) {
        auth.userDetailsService(userDetailsService()).passwordEncoder(passwordEncoder())
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {

//        val encoders = HashMap<String, PasswordEncoder>()
//        encoders.put("noop", NoOpPasswordEncoder.getInstance())
//        encoders.put("null", NoOpPasswordEncoder.getInstance())
//        encoders.put("bcrypt", BCryptPasswordEncoder())


//        return DelegatingPasswordEncoder("noop", encoders)
        return NoOpPasswordEncoder.getInstance()
    }

    override fun configure(http: HttpSecurity) {
        http.authorizeRequests()
//                .antMatchers("/resourcecs/**").permitAll()
                .anyRequest().authenticated()

                .and().httpBasic()
//                .loginPage("/login")
//                .permitAll()
//        super.configure(http)
    }

}