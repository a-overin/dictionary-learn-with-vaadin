package com.aoverin.dictionarylearning.services

import com.aoverin.dictionarylearning.models.UserModel
import com.aoverin.dictionarylearning.models.UserRoles
import com.google.common.collect.ImmutableSet
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserServiceImpl(
    val passwordEncoder: PasswordEncoder,
    @Value("\${com.aoverin.dictionarylearning.admin.login}") val login: String,
    @Value("\${com.aoverin.dictionarylearning.admin.password}") val password: String
) : UserService {
    override fun findByUsername(userName: String): UserModel? {
        if (login != userName) {
            return null
        }
        return UserModel(
            -1,
            login,
            "Admin",
            passwordEncoder.encode(password),
            "1",
            ImmutableSet.of(UserRoles.USER, UserRoles.ADMIN)
        )
    }
}