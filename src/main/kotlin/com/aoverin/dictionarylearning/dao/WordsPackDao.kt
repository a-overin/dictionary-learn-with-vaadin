package com.aoverin.dictionarylearning.dao

import com.aoverin.dictionarylearning.models.WordsForPack
import com.aoverin.dictionarylearning.models.WordsPack

interface WordsPackDao {

    fun getAllPack(): List<WordsPack>

    fun getWordsForPackId(id: Int): WordsForPack

    fun addWordsToPack(packID: Int, wordId: Int)

    fun removeWordsFromPack(packID: Int, wordId: Int)

    fun createPack(name: String, lang1: String, lang2: String) : Int
}