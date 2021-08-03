package com.aoverin.dictionarylearning.views.game.write

import com.aoverin.dictionarylearning.views.game.AbstractGame
import com.aoverin.dictionarylearning.views.game.GameLayout
import com.google.common.collect.ImmutableList
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.dnd.DragSource
import com.vaadin.flow.component.dnd.DropEffect
import com.vaadin.flow.component.dnd.DropTarget
import com.vaadin.flow.component.dnd.EffectAllowed
import com.vaadin.flow.component.html.Span
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.router.Route

@Route(value = "write", layout = GameLayout::class)
class WriteGame : AbstractGame() {

    private val targetsWords: List<DropTarget<Component>>

    private val sourceWords: List<DragSource<Component>>

    init {
        targetsWords = mutableListOf()
        sourceWords = mutableListOf()
        add(createTargetWordsLayout())
        add(createEmptyLayout(getTargetWords().count()))
        add(createSourceWordsLayout())
    }

    private fun createEmptyLayout(wordsCount: Int): HorizontalLayout {
        val layout = HorizontalLayout()
        for (i in 1..wordsCount) {
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

    private fun createTargetWordsLayout() : HorizontalLayout {
        val layout = HorizontalLayout()
        getTargetWords()
            .forEach{ layout.add(Span(it)) }
        return layout
    }

    private fun createSourceWordsLayout() : HorizontalLayout {
        val layout = HorizontalLayout()
        getSourceWords()
            .map { it ->
                val span = Span(it)
                val dragSource = DragSource.create(span)
                dragSource.effectAllowed = EffectAllowed.MOVE
                dragSource.addDragEndListener{
                    if (it.isSuccessful) {
                        dragSource.isDraggable = false
//                        dragSource.dragSourceComponent.isVisible = false
                        println("good $dragSource")
                    }
                }
                span
            }
            .forEach{ layout.add(it) }
        return layout
    }

    private fun getTargetWords(): List<String> {
        return ImmutableList.of("раз", "два", "три")
    }

    private fun getSourceWords(): List<String> {
        return ImmutableList.of("one", "two", "three")
    }
}