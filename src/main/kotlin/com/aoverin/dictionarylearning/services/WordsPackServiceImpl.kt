package com.aoverin.dictionarylearning.services

import com.aoverin.dictionarylearning.models.Word
import com.aoverin.dictionarylearning.models.WordsForPack
import com.aoverin.dictionarylearning.models.WordsPack
import io.swagger.v3.oas.annotations.servers.Server
import org.springframework.stereotype.Service

@Service
class WordsPackServiceImpl : WordsPackService {
    override fun getAllPack(): List<WordsPack> {
        return listOf(
            WordsPack(1, "First", "eng", "rus"),
            WordsPack(2, "Second","eng", "rus")
        )
    }

    override fun getWordsForPackId(id: Int): WordsForPack {
        if (id == 1) {
            return WordsForPack(id, listOf(
                Pair(Word(1, "hello", "eng"), Word(2, "привет", "rus")),
                Pair(Word(1, "class", "eng"), Word(2, "класс", "rus")),
            ))
        }
        return WordsForPack(
            id, listOf(
                Pair(Word(1, "hello", "eng"), Word(2, "привет", "rus")),
                Pair(Word(1, "house", "eng"), Word(2, "дом", "rus")),
            )
        )
    }
}