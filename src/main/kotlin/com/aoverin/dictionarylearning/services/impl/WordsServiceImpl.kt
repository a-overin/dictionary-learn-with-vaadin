package com.aoverin.dictionarylearning.services.impl

import com.aoverin.dictionarylearning.dao.WordsDao
import com.aoverin.dictionarylearning.models.Word
import com.aoverin.dictionarylearning.services.WordsService
import org.springframework.dao.DataAccessException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class WordsServiceImpl(
    private val wordsDao: WordsDao
) : WordsService {

    override fun add(value: String, lang: String): Word {
        return Word(
            wordsDao.add(value, lang),
            value,
            lang
        )
    }

    override fun getById(id: Int): Word? {
        return wordsDao.getById(id)
    }

    override fun getByLang(lang: String): List<Word> {
        return wordsDao.getByLang(lang)
    }

    override fun connectWords(wordsIdOne: Int, wordsIdTwo: Int) {
        wordsDao.connectWords(wordsIdOne, wordsIdTwo)
    }

    override fun getLanguages(): List<String> {
        return wordsDao.getLanguages()
    }

    @Transactional(rollbackFor = [DataAccessException::class])
    override fun addAndConnectWords(langOne: String, valueOne: String, langTwo: String, valueTwo: String) {
        connectWords(add(valueOne, langOne).id, add(valueTwo, langTwo).id)
    }
}