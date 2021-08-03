package com.aoverin.dictionarylearning.services

import com.aoverin.dictionarylearning.models.UserModel

interface UserService {

    fun findByUsername(userName: String): UserModel?
}