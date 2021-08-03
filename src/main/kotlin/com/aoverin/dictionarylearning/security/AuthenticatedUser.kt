package com.aoverin.dictionarylearning.security

import com.aoverin.dictionarylearning.configurations.SecurityConfiguration
import com.aoverin.dictionarylearning.models.UserModel
import com.aoverin.dictionarylearning.services.UserService
import com.vaadin.flow.component.UI
import com.vaadin.flow.server.VaadinServletRequest
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler
import org.springframework.stereotype.Component
import java.util.*

@Component
class AuthenticatedUser(
    val userService: UserService
) {
    private fun getAuthenticatedUser(): UserDetails? {
        val context = SecurityContextHolder.getContext()
        val principal = context.authentication.principal
        return if (principal is UserDetails) {
            context.authentication.principal as UserDetails
        } else null
    }

    fun get(): Optional<UserModel> {
        val details = getAuthenticatedUser() ?: return Optional.empty()
        return Optional.ofNullable(userService.findByUsername(details.username))
    }

    fun logout() {
        UI.getCurrent().page.setLocation(SecurityConfiguration.LOGOUT_URL)
        val logoutHandler = SecurityContextLogoutHandler()
        logoutHandler.logout(VaadinServletRequest.getCurrent().httpServletRequest, null, null)
    }
}