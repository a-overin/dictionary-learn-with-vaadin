package com.aoverin.dictionarylearning.views.game.write

import com.aoverin.dictionarylearning.services.WordsPackService
import com.aoverin.dictionarylearning.views.MainLayout
import com.aoverin.dictionarylearning.views.game.AbstractGame
import com.vaadin.flow.component.Text
import com.vaadin.flow.component.icon.Icon
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route
import kotlin.properties.Delegates

@PageTitle("Match game")
@Route(value = "write", layout = MainLayout::class)
class WriteGame(
    wordsPackService: WordsPackService
) : AbstractGame(wordsPackService) {

    private var wordsCount by Delegates.notNull<Int>()
    private lateinit var wordsLayout: VerticalLayout

    private fun createTarget(text: String):Text {
        return Text(text)
            .apply {
                addClassName("target-words-list")
            }
    }

    private fun createEdit(): TextField {
        return TextField()
    }

    override fun initComponents(wordsTarget: List<String>, wordsSource: List<String>) {
        wordsCount = wordsTarget.count()
        wordsLayout = createWordLayout(
            wordsTarget,
            wordsCount)
            .also {
                this.add(it)
            }

    }

    private fun createWordLayout(targetWords: List<String>, wordCount: Int): VerticalLayout {
        val layout = VerticalLayout()
        for (i in 0 until wordCount) {
            layout.add(
                HorizontalLayout()
                    .apply {
                        defaultVerticalComponentAlignment = FlexComponent.Alignment.BASELINE
                        add(createTarget(targetWords[i]))
                        add(createEdit())
                    }
            )
        }
        return layout
    }

    override fun hideComponents() {
        if (this::wordsLayout.isInitialized) {
            remove(wordsLayout)
        }
    }

    override fun disableComponents() {
        wordsLayout.isEnabled = false
    }

    override fun showResult() {
        val errorElements = checkResultAndReturnErrorElementNumber(
            getListStringFromElements()
        )
        drawResult(errorElements)
    }

    private fun getListStringFromElements(): List<Pair<String, String>> {
        val stringList = mutableListOf<Pair<String, String>>()
        for (i in 0 until wordsLayout.componentCount) {
            val horizontalLayout = wordsLayout.getComponentAt(i) as HorizontalLayout
            val target = horizontalLayout.getComponentAt(0) as Text
            val source = horizontalLayout.getComponentAt(1) as TextField
            stringList.add(Pair(target.text, source.value))
        }
        return stringList
    }

    private fun drawResult(list: List<Pair<Int, String>>) {
        val results = list.toMap()
        for (i in 0 until wordsLayout.componentCount) {
            val component = wordsLayout.getComponentAt(i) as HorizontalLayout
            if (results.containsKey(i)) {
                val icon = Icon(VaadinIcon.CLOSE_SMALL).apply { color = "red" }
                component.setVerticalComponentAlignment(FlexComponent.Alignment.CENTER, icon)
                component.add(icon)
                component.add(Text("(${results[i]})"))
            } else {
                val icon = Icon(VaadinIcon.CHECK).apply { color = "green" }
                component.setVerticalComponentAlignment(FlexComponent.Alignment.CENTER, icon)
                component.add(icon)
            }
        }
    }
}