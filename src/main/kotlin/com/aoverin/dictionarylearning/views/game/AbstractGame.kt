package com.aoverin.dictionarylearning.views.game

import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.router.RoutePrefix
import com.vaadin.flow.server.auth.AnonymousAllowed


@RoutePrefix(value = "game")
@AnonymousAllowed
open class AbstractGame : VerticalLayout() {
}