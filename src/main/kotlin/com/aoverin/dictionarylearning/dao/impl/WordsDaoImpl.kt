package com.aoverin.dictionarylearning.dao.impl

import com.aoverin.dictionarylearning.dao.WordsDao
import com.aoverin.dictionarylearning.models.Word
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.jdbc.core.simple.SimpleJdbcInsert
import org.springframework.stereotype.Repository

@Repository
class WordsDaoImpl(
    private val jdbcTemplate: JdbcTemplate
) : WordsDao {

    companion object {
        private const val SQL_GET_LANG = "select * from languages"
        private const val SQL_GET_WORDS_BY_LANG = "select * from words where lang = :lang"
        private const val SQL_GET_WORDS_BY_ID = "select * from words where id = :id"
    }

    override fun add(value: String, lang: String): Int {
        return SimpleJdbcInsert(jdbcTemplate)
            .withTableName("words")
            .usingGeneratedKeyColumns("id")
            .run {
                executeAndReturnKey(mapOf(
                    "word" to value.lowercase(),
                    "lang" to lang.lowercase()
                )) as Int
        }

    }

    override fun getById(id: Int): Word? {
        return NamedParameterJdbcTemplate(jdbcTemplate).queryForObject(
            SQL_GET_WORDS_BY_ID,
            mapOf(
                "id" to id
            )
        ) { rs, _ -> Word(
            rs.getInt("id"),
            rs.getString("word"),
            rs.getString("lng"))
        }
    }

    override fun getByLang(lang: String): List<Word> {
        return NamedParameterJdbcTemplate(jdbcTemplate).query(
            SQL_GET_WORDS_BY_LANG,
            mapOf(
                "lang" to lang.lowercase()
            )
        ) { rs, _ -> Word(
            rs.getInt("id"),
            rs.getString("word"),
            rs.getString("lang"))
        }
    }

    override fun connectWords(wordsIdOne: Int, wordsIdTwo: Int) {
        runCatching {
            SimpleJdbcInsert(jdbcTemplate)
                .withTableName("word2word")
                .run {
                    execute(
                        mapOf(
                            "word" to minOf(wordsIdOne, wordsIdTwo),
                            "link_word" to maxOf(wordsIdOne, wordsIdTwo),
                        )
                    )
                }
        }
            .exceptionOrNull()
    }

    override fun getLanguages(): List<String> {
        return jdbcTemplate.queryForList(SQL_GET_LANG, String::class.java)
    }
}