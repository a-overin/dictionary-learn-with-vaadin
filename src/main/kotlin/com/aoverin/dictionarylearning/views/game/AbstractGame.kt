package com.aoverin.dictionarylearning.views.game

import com.aoverin.dictionarylearning.models.WordsPack
import com.aoverin.dictionarylearning.services.WordsPackService
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.combobox.ComboBox
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.router.RoutePrefix
import com.vaadin.flow.server.auth.AnonymousAllowed

@RoutePrefix(value = "game")
@AnonymousAllowed
abstract class AbstractGame(
    private val wordsPackService: WordsPackService
) : VerticalLayout() {

    companion object {
        private const val checkButtonClass = "bordered-button"
    }

    private var gameStarted: Boolean = false
    private val comboBox = ComboBox<WordsPack>("Pack")
    private val button = Button("Start")
    private val buttonLangFirst = Button("lang type one")
    private val buttonLangSecond = Button("lang type second")
    private lateinit var wordsList: List<Pair<String, String>>
    private var langType = false

    init {
        this.add(createLayout(wordsPackService))
    }

    protected fun checkResultAndReturnErrorElementNumber(resultSet: List<Pair<String, String>>): List<Int> {
        val resultErrors = mutableListOf<Int>()
        resultSet.forEachIndexed { index, pair ->
            if (!wordsList.any { p -> p.first == pair.first && p.second == pair.second}) {
                resultErrors.add(index)
            }
        }
        return resultErrors
    }

    private fun makeAction() {
        if (!gameStarted) {
            hideComponents()
            wordsList = wordsPackService.getWordsForPackId(comboBox.value.id).words.map { e ->
                if (langType) {
                    Pair(
                        e.first.value,
                        e.second.value
                    )
                } else {
                    Pair(
                        e.second.value,
                        e.first.value
                    )
                }
            }
            initComponents(wordsList)
        } else {
            disableComponents()
            showResult()
        }
        switchElements(!gameStarted)
        button.isEnabled = true
    }

    private fun createLayout(wordsPackService: WordsPackService): HorizontalLayout {
        val layout = HorizontalLayout()
        val list = wordsPackService.getAllPack()
        comboBox.apply {
            helperText = "Select pack with words"
            setItems(list)
            setItemLabelGenerator { it.name }
            addValueChangeListener {
                buttonLangFirst.isVisible = true
                buttonLangSecond.isVisible = true
                buttonLangFirst.text = "${it.value.langOne} -> ${it.value.langTwo}"
                buttonLangSecond.text = "${it.value.langTwo} -> ${it.value.langOne}"
            }
        }
        button.apply {
            isDisableOnClick = true
            addClickListener { makeAction()}
        }
        buttonLangFirst.apply {
            addClickListener {
                addClassName(checkButtonClass)
                buttonLangSecond.removeClassName(checkButtonClass)
                langType = true
            }
            isVisible = false
        }
        buttonLangSecond.apply {
            addClickListener {
                addClassName(checkButtonClass)
                buttonLangFirst.removeClassName(checkButtonClass)
                langType = false
            }
            isVisible = false
            addClassName(checkButtonClass)
        }
        layout.add(comboBox, button, buttonLangFirst, buttonLangSecond)
        for (i in 0 until layout.componentCount) {
            layout.setVerticalComponentAlignment(FlexComponent.Alignment.BASELINE, layout.getComponentAt(i))
        }
        return layout
    }

    private fun switchElements(flag: Boolean) {
        comboBox.isEnabled = !flag
        buttonLangFirst.isEnabled = !flag
        buttonLangSecond.isEnabled = !flag
        button.text = if (flag) "Check" else "Start"
        gameStarted = flag
    }

    abstract fun disableComponents()

    abstract fun hideComponents()

    abstract fun initComponents(words: List<Pair<String, String>>)

    abstract fun showResult()
}