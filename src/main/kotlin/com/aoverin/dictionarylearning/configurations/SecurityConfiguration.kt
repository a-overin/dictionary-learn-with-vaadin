package com.aoverin.dictionarylearning.configurations

import com.aoverin.dictionarylearning.views.login.LoginView
import com.vaadin.flow.spring.security.VaadinWebSecurityConfigurerAdapter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder

@EnableWebSecurity
@Configuration
class SecurityConfiguration : VaadinWebSecurityConfigurerAdapter() {
    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        super.configure(http)
        setLoginView(http, LoginView::class.java, LOGOUT_URL)
    }

    @Throws(Exception::class)
    override fun configure(web: WebSecurity) {
        super.configure(web)
        web.ignoring().antMatchers("/images/logo.png")
    }

    companion object {
        const val LOGOUT_URL = "/"
    }
}