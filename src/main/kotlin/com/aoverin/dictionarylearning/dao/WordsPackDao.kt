package com.aoverin.dictionarylearning.dao

import com.aoverin.dictionarylearning.models.WordsForPack
import com.aoverin.dictionarylearning.models.WordsPack

interface WordsPackDao {

    fun getAllPack(): List<WordsPack>

    fun getWordsForPackId(id: Int): WordsForPack

    fun addWordsToPack(packID: Int, wordId: Int)

    fun removeWordsFromPack(packID: Int, wordId: Int)
}