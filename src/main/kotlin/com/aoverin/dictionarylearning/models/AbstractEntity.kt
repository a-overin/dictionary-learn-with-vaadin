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

data class Word(
    override val id: Int,
    val value: String,
    val lang: String
) : AbstractEntity()

data class WordsPack(
    override val id: Int,
    val name: String,
    val langOne: String,
    val langTwo: String
) : AbstractEntity()

data class WordsForPack(
    val packId: Int,
    val words: List<Pair<Word, Word>>
)