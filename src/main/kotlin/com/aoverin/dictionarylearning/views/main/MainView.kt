package com.aoverin.dictionarylearning.views.main

import com.aoverin.dictionarylearning.views.MainLayout
import com.vaadin.flow.component.Text
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.dialog.Dialog
import com.vaadin.flow.component.notification.Notification
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route
import javax.annotation.security.RolesAllowed

@PageTitle("Main")
@Route(value = "main", layout = MainLayout::class)
@RolesAllowed("admin")
class MainView : HorizontalLayout() {

    private val name: TextField
    private val buttons: List<Button>

    init {
        buttons = mutableListOf()
        addClassName("main-view")
        name = TextField("Your name")
        val helloButton = Button("Say hello")
        helloButton.addClickListener {
            Notification.show(
                "Hello " + name.value
            )
            getButton("Another")
        }
        buttons.add(helloButton)
        add(name, *buttons.toTypedArray())
    }

    private fun getButton(name: String) {

        val dialog = Dialog()
        val verticalLayout = VerticalLayout()
        verticalLayout.add(Text("Your next meeting starts in 5 minutes"))
        verticalLayout.add(Button("OK"){
            dialog.close()
            setStatus()
        })
        dialog.add(verticalLayout)
        val button = Button(name)
        button.addClickListener { dialog.open() }
        add(button, dialog)
    }
}

fun setStatus() {
    Notification.show("Pressed OK")
}
