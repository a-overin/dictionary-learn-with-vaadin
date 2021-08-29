package com.aoverin.dictionarylearning.dao

import com.aoverin.dictionarylearning.models.Word

interface WordsDao {
    fun add(value: String, lang: String): Int
    fun getById(id: Int): Word?
    fun getByLang(lang: String): List<Word>
    fun connectWords(wordsIdOne: Int, wordsIdTwo: Int)
    fun getLanguages(): List<String>
}