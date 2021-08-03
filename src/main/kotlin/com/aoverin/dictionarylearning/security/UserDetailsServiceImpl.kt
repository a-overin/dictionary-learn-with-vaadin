package com.aoverin.dictionarylearning.security

import com.aoverin.dictionarylearning.models.UserModel
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import java.util.stream.Collectors.toList
import com.aoverin.dictionarylearning.services.UserService
import org.springframework.security.core.userdetails.UsernameNotFoundException

@Service
class UserDetailsServiceImpl(
    val userService: UserService
) : UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails {
        val user = userService.findByUsername(username)
            ?: throw UsernameNotFoundException("No user present with username: $username")
        return User(user.userName, user.hashedPassword, getAuthorities(user))
    }

    private fun getAuthorities(user: UserModel) : List<GrantedAuthority> {
        return user.roles.stream().map { role -> SimpleGrantedAuthority("ROLE_" + role.roleName) }
            .collect(toList());
    }
}