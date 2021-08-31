package com.aoverin.dictionarylearning.dao.impl

import com.aoverin.dictionarylearning.dao.WordsPackDao
import com.aoverin.dictionarylearning.models.Word
import com.aoverin.dictionarylearning.models.WordsForPack
import com.aoverin.dictionarylearning.models.WordsPack
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.core.simple.SimpleJdbcInsert
import org.springframework.stereotype.Repository

@Repository
class WordsPackDaoImpl(
    private val jdbcTemplate: JdbcTemplate
) : WordsPackDao {

    companion object {
        private const val SQL_DELETE_WORD_FROM_PACK =
            "delete from words2group where group_id = :group_id and word_id = :word_id"

        private const val SQL_SELECT_ALL_PACKS = "select * from word_group_main"

        private const val SQL_GET_WORDS_FOR_PACK =
            """with group_words as(
                    select word_id
                        from words2group
                    where group_id = :group_id
                )
                select w1.id w1_id, w1.word word1, w1.lang lang1, w2.id w2_id, w2.word word2, w2.lang lang2
                    from word2word ww
                    join words w1 on w1.id in (ww.word, ww.link_word)
                    join words w2 on w2.id in (ww.word, ww.link_word)
                where w1.lang = :lang1
                  and w2.lang = :lang2
                  and ww.word in (select * from group_words)
                  and ww.link_word in (select * from group_words)"""
    }

    override fun getAllPack(): List<WordsPack> {
        return jdbcTemplate.query(
            SQL_SELECT_ALL_PACKS
        ) { rs, _ ->
            WordsPack(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("lang1"),
                rs.getString("lang2")
            )
        }
    }

    override fun getWordsForPackId(id: Int): WordsForPack {
        val wordsPack = getAllPack()
            .stream()
            .filter { it.id == id }
            .findFirst()
            .get()

        val result = NamedParameterJdbcTemplate(jdbcTemplate).query(
            SQL_GET_WORDS_FOR_PACK,
            mapOf(
                "group_id" to id,
                "lang1" to wordsPack.langOne,
                "lang2" to wordsPack.langTwo
            )
        ) { rs, _ ->
            Pair(
                Word(
                    rs.getInt("w1_id"),
                    rs.getString("word1"),
                    rs.getString("lang1")
                ),
                Word(
                    rs.getInt("w2_id"),
                    rs.getString("word2"),
                    rs.getString("lang2")
                )
            )
        }
        return WordsForPack(id, result)
    }

    override fun addWordsToPack(packID: Int, wordId: Int) {
        SimpleJdbcInsert(jdbcTemplate)
            .withTableName("words2group")
            .execute(mapOf(
                "group_id" to packID,
                "word_id" to wordId
            ))
    }

    override fun removeWordsFromPack(packID: Int, wordId: Int) {
        NamedParameterJdbcTemplate(jdbcTemplate).update(
            SQL_DELETE_WORD_FROM_PACK,
            mutableMapOf(
                "group_id" to packID,
                "word_id" to wordId
            )
        )
    }
}