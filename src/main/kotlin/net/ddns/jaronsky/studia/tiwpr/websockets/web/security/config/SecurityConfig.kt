package net.ddns.jaronsky.studia.tiwpr.websockets.web.security.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.NoOpPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.session.SessionManagementFilter

@Configuration
@EnableWebSecurity
class SecurityConfig : WebSecurityConfigurerAdapter() {

    @Bean
    fun corsFilter(): CorsFilter {
        return CorsFilter()
    }

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
        http
                .addFilterBefore(corsFilter(), SessionManagementFilter::class.java )
                .authorizeRequests()
                .anyRequest().authenticated()
                .and().httpBasic()
    }

//    @Throws(Exception::class)
//    override fun configure(http: HttpSecurity) {
//        http
//                .addFilterBefore(corsFilter(), SessionManagementFilter::class.java) //adds your custom CorsFilter
//                .exceptionHandling().authenticationEntryPoint(authenticationEntryPoint).and()
//                .formLogin()
//                .successHandler(ajaxSuccessHandler)
//                .failureHandler(ajaxFailureHandler)
//                .loginProcessingUrl("/authentication")
//                .passwordParameter("password")
//                .usernameParameter("username")
//                .and()
//                .logout()
//                .deleteCookies("JSESSIONID")
//                .invalidateHttpSession(true)
//                .logoutUrl("/logout")
//                .logoutSuccessUrl("/")
//                .and()
//                .csrf().disable()
//                .anonymous().disable()
//                .authorizeRequests()
//                .antMatchers("/authentication").permitAll()
//                .antMatchers("/oauth/token").permitAll()
//                .antMatchers("/admin/*").access("hasRole('ROLE_ADMIN')")
//                .antMatchers("/user/*").access("hasRole('ROLE_USER')")
//    }

}