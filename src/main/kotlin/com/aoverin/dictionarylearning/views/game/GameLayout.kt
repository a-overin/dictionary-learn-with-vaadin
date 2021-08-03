package com.aoverin.dictionarylearning.views.game

import com.aoverin.dictionarylearning.views.MainLayout
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.ParentLayout
import com.vaadin.flow.router.RouterLayout

@PageTitle("Game")
@ParentLayout(value = MainLayout::class)
class GameLayout : VerticalLayout(), RouterLayout {
    init {
        addClassName("game-view")
        val layout = HorizontalLayout()
        layout.add(TextField("Base name"))
        add(layout)
    }
}