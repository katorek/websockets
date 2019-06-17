package net.ddns.jaronsky.studia.tiwpr.websockets.web.security.model
//
//import org.springframework.security.core.GrantedAuthority
//import org.springframework.security.core.authority.SimpleGrantedAuthority
//import org.springframework.security.core.userdetails.UserDetails
//
//class UserFactory(
//        val name: String
//): UserDetails {
//
//
//    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
//        return mutableListOf<GrantedAuthority>(SimpleGrantedAuthority("USER"))
//    }
//
//    override fun isEnabled(): Boolean {
//        return true
//    }
//
//    override fun getUsername(): String {
//        return name
//    }
//
//    override fun isCredentialsNonExpired(): Boolean {
//        return true
//    }
//
//    override fun getPassword(): String {
//        return ""
//    }
//
//    override fun isAccountNonExpired(): Boolean {
//        return true
//    }
//
//    override fun isAccountNonLocked(): Boolean {
//        return true
//    }
//}