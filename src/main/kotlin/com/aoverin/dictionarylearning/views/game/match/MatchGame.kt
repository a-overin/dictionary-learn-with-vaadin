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

    private lateinit var sourceWords: List<String>
    private lateinit var targetWords: List<String>
    private lateinit var horizontalLayout: HorizontalLayout
    private var wordsCount by Delegates.notNull<Int>()

    private fun createEmptyLayout(): VerticalLayout {
        val layout = VerticalLayout()
        for (i in 1..targetWords.count()) {
            val span = Span("empty")
            val dropTarget: DropTarget<Span> = DropTarget.create(span)
            dropTarget.dropEffect = DropEffect.MOVE
            dropTarget.addDropListener { it.dragSourceComponent.ifPresent {
                    component ->
                run {
                    it.component.add(component)
                    span.text = component.element.text
                    span.addClassName("finished-dropped")
                    span.removeClassName("possible-drop-zone")
                    dropTarget.isActive = false
                }

            }}
            span.addClassName("possible-drop-zone")
            layout.add(span)
        }
        return layout
    }

    private fun createTargetWordsLayout() : VerticalLayout {
        val layout = VerticalLayout()
        layout.addClassName("target-words-list")
        targetWords
            .forEach{ layout.add(Span(it)) }
        return layout
    }

    private fun createSourceWordsLayout() : VerticalLayout {
        val layout = VerticalLayout()
        sourceWords
            .map { it ->
                val span = Span(it)
                val dragSource = DragSource.create(span)
                dragSource.effectAllowed = EffectAllowed.MOVE
                dragSource.addDragEndListener{
                    if (it.isSuccessful) {
                        dragSource.isDraggable = false
                    }
                }
                span
            }
            .forEach{ layout.add(it) }
        return layout
    }

    override fun hideComponents() {
        if (this::horizontalLayout.isInitialized) {
            remove(horizontalLayout)
        }
    }

    override fun disableComponents() {
        horizontalLayout.isEnabled = false
    }

    override fun initComponents(words: List<Pair<String, String>>) {
        wordsCount = words.count()
        targetWords = words
            .stream()
            .map { e -> e.first }
            .toList()
        sourceWords = words
            .stream()
            .map { e -> e.second }
            .toList()
        val targetWordsLayout = createTargetWordsLayout()
        val emptyWordsLayout = createEmptyLayout()
        val sourceWordsLayout = createSourceWordsLayout()
        horizontalLayout = HorizontalLayout().apply {
            add(targetWordsLayout)
            add(emptyWordsLayout)
            add(sourceWordsLayout)
            isEnabled = true
        }
        add(horizontalLayout)
    }

    override fun showResult() {
        val zeroComp = horizontalLayout.getComponentAt(0) as VerticalLayout
        val oneComp = horizontalLayout.getComponentAt(1) as VerticalLayout
        val errorElements = checkResultAndReturnErrorElementNumber(
            getListStringFromElements(zeroComp) zip getListStringFromElements(oneComp)
        )
        drawResult(errorElements)
    }

    private fun getListStringFromElements(verticalLayout: VerticalLayout): List<String> {
        val stringList = mutableListOf<String>()
        for (i in 0 until verticalLayout.componentCount) {
            val comp = verticalLayout.getComponentAt(i)
            if (comp is Span) {
                stringList.add(comp.text)
            }
        }
        return stringList
    }

    private fun drawResult(list: List<Int>) {
        val layout = VerticalLayout().apply {
            for (i in 0 until wordsCount) {
                if (i in list) {
                    add(Icon(VaadinIcon.CLOSE_SMALL).apply { color = "red" })
                } else {
                    add(Icon(VaadinIcon.CHECK).apply { color = "green" })
                }
            }
        }
        horizontalLayout.add(layout)
    }
}