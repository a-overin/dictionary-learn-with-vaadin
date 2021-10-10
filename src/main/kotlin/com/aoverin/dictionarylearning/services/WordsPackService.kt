package com.aoverin.dictionarylearning.services

import com.aoverin.dictionarylearning.models.Word
import com.aoverin.dictionarylearning.models.WordsForPack
import com.aoverin.dictionarylearning.models.WordsPack

interface WordsPackService {

    fun getAllPack(): List<WordsPack>

    fun getWordsForPackId(id: Int): WordsForPack

    fun createNewPack(pack: WordsPack)

    fun addWordsToPack(pack: WordsPack, word1: Word, words2: Word)

    fun removeWordsFromPack(pack: WordsPack, word1: Word, words2: Word)
}