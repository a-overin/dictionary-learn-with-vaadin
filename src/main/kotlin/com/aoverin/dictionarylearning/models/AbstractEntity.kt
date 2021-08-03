package com.aoverin.dictionarylearning.models

sealed class AbstractEntity {
    abstract val id: Int
}

data class UserModel(
    override val id: Int,
    val userName: String,
    val name: String,
    val hashedPassword: String,
    val saltPassword: String,
    val roles: Set<UserRoles>,
) : AbstractEntity()
