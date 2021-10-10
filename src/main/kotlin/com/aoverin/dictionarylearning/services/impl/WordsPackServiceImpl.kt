package com.aoverin.dictionarylearning.services.impl

import com.aoverin.dictionarylearning.dao.WordsPackDao
import com.aoverin.dictionarylearning.models.Word
import com.aoverin.dictionarylearning.models.WordsForPack
import com.aoverin.dictionarylearning.models.WordsPack
import com.aoverin.dictionarylearning.services.WordsPackService
import org.springframework.stereotype.Service

@Service
class WordsPackServiceImpl(
    private val wordsPackDao: WordsPackDao
) : WordsPackService {
    override fun getAllPack(): List<WordsPack> {
        return wordsPackDao.getAllPack()
    }

    override fun getWordsForPackId(id: Int): WordsForPack {
        return wordsPackDao.getWordsForPackId(id)
    }

    override fun createNewPack(pack: WordsPack) {
        wordsPackDao.createPack(pack.name, pack.langOne, pack.langTwo)
    }

    override fun addWordsToPack(pack: WordsPack, vararg words: Word?) {
        words
            .filterNotNull()
            .forEach {
                wordsPackDao.addWordToPack(pack.id, it.id)
            }
    }

    override fun removeWordsFromPack(pack: WordsPack, word1: Word, words2: Word) {
        wordsPackDao.removeWordsFromPack(pack.id, word1.id)
        wordsPackDao.removeWordsFromPack(pack.id, words2.id)
    }
}