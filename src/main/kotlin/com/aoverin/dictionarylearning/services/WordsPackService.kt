package com.aoverin.dictionarylearning.services

import com.aoverin.dictionarylearning.models.WordsForPack
import com.aoverin.dictionarylearning.models.WordsPack

interface WordsPackService {

    fun getAllPack(): List<WordsPack>

    fun getWordsForPackId(id: Int): WordsForPack

    fun addWordsToPack(packId: Int, word1Id: Int, words2Id: Int)

    fun removeWordsFromPack(packId: Int, word1Id: Int, words2Id: Int)
}