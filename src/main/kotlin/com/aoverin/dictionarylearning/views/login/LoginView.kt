package com.aoverin.dictionarylearning.views.login

import com.vaadin.flow.component.login.LoginForm
import com.vaadin.flow.component.login.LoginI18n
import com.vaadin.flow.component.login.LoginOverlay
import com.vaadin.flow.router.*
import com.vaadin.flow.server.auth.AnonymousAllowed
import org.springframework.core.annotation.AliasFor

@PageTitle("Login")
@Route(value = "login")
class LoginView : LoginOverlay(), BeforeEnterObserver  {

    var login = LoginForm()

    init {
        action = "login"
        val i18n = LoginI18n.createDefault()
        i18n.header = LoginI18n.Header()
        i18n.header.title = "Dictionary learning"
        i18n.header.description = "Login using user/user or admin/admin"
        i18n.additionalInformation = null
        setI18n(i18n)
        isForgotPasswordButtonVisible = false
        isOpened = true
    }

    override fun beforeEnter(event: BeforeEnterEvent) {
        if (event.location
                .queryParameters
                .parameters
                .containsKey("error")
        ) {
            login.isError = true
        }
    }
}