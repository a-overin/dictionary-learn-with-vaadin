package com.aoverin.dictionarylearning.services

import com.aoverin.dictionarylearning.models.Word

interface WordsService {
    fun add(value: String, lang: String): Word
    fun getById(id: Int): Word?
    fun getByLang(lang: String): List<Word>
    fun connectWords(wordsIdOne: Int, wordsIdTwo: Int)
    fun getLanguages(): List<String>
    fun addAndConnectWords(langOne: String, valueOne: String, langTwo: String, valueTwo: String)
}