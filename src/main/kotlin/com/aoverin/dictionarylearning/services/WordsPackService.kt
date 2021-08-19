package com.aoverin.dictionarylearning.services

import com.aoverin.dictionarylearning.models.WordsForPack
import com.aoverin.dictionarylearning.models.WordsPack

interface WordsPackService {

    fun getAllPack(): List<WordsPack>

    fun getWordsForPackId(id: Int): WordsForPack
}