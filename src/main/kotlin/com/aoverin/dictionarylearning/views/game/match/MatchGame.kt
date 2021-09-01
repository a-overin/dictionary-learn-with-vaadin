package com.aoverin.dictionarylearning.views.game.match

import com.aoverin.dictionarylearning.services.WordsPackService
import com.aoverin.dictionarylearning.views.MainLayout
import com.aoverin.dictionarylearning.views.game.AbstractGame
import com.vaadin.flow.component.dnd.DragSource
import com.vaadin.flow.component.dnd.DropEffect
import com.vaadin.flow.component.dnd.DropTarget
import com.vaadin.flow.component.dnd.EffectAllowed
import com.vaadin.flow.component.html.Span
import com.vaadin.flow.component.icon.Icon
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route
import kotlin.properties.Delegates
import kotlin.streams.toList

@PageTitle("Match game")
@Route(value = "match", layout = MainLayout::class)
class MatchGame(
    wordsPackService: WordsPackService
) : AbstractGame(wordsPackService) {

    private var wordsCount by Delegates.notNull<Int>()
    private lateinit var wordsLayout: VerticalLayout

    private fun createDropZone(): Span {
        val span = Span("empty")
        DropTarget.create(span)
            .apply {
                dropEffect = DropEffect.MOVE
                addDropListener {
                    it.dragSourceComponent.ifPresent { component ->
                        run {
                            it.component.add(component)
                            span.text = component.element.text
                            span.addClassName("finished-dropped")
                            span.removeClassName("possible-drop-zone")
                            this.isActive = false
                        }
                    }
                }
            }
        span.addClassName("possible-drop-zone")
        return span
    }

    private fun createTarget(text: String):Span {
        return Span(text)
            .apply {
                addClassName("target-words-list")
            }
    }

    private fun createDragSource(text: String): Span {
        val span = Span(text)
        DragSource.create(span)
            .apply {
                effectAllowed = EffectAllowed.MOVE
                addDragEndListener {
                    if (it.isSuccessful) {
                        this.isDraggable = false
                    }
                }
            }
        return span
    }

    override fun initComponents(words: List<Pair<String, String>>) {
        wordsCount = words.count()
        wordsLayout = createWordLayout(
            words.stream()
                .map { e -> e.first }
                .toList()
                .shuffled(),
            words.stream()
                .map { e -> e.second }
                .toList()
                .shuffled(),
            wordsCount)
            .also {
                this.add(it)
            }

    }

    private fun createWordLayout(targetWords: List<String>, sourceWords: List<String>, wordCount: Int): VerticalLayout {
        val layout = VerticalLayout()
        for (i in 0 until wordCount) {
            layout.add(
                HorizontalLayout()
                    .apply {
                        defaultVerticalComponentAlignment = FlexComponent.Alignment.BASELINE
                        add(createTarget(targetWords[i]))
                        add(createDropZone())
                        add(createDragSource(sourceWords[i]))
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
            val target = horizontalLayout.getComponentAt(0) as Span
            val source = horizontalLayout.getComponentAt(1) as Span
            stringList.add(Pair(target.text, source.text))
        }
        return stringList
    }

    private fun drawResult(list: List<Int>) {
        for (i in 0 until wordsLayout.componentCount) {
            val component = wordsLayout.getComponentAt(i) as HorizontalLayout
            if (i in list) {
                val icon = Icon(VaadinIcon.CLOSE_SMALL).apply { color = "red" }
                component.setVerticalComponentAlignment(FlexComponent.Alignment.END, icon)
                component.add(icon)
            } else {
                val icon = Icon(VaadinIcon.CHECK).apply { color = "green" }
                component.setVerticalComponentAlignment(FlexComponent.Alignment.END, icon)
                component.add(icon)
            }
        }
    }
}