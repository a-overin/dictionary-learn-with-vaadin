package com.aoverin.dictionarylearning

import com.vaadin.flow.component.dependency.NpmPackage
import com.vaadin.flow.component.page.AppShellConfigurator
import com.vaadin.flow.server.PWA
import com.vaadin.flow.theme.Theme
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer

@SpringBootApplication
@Theme(value = "myapp")
@PWA(name = "Dictionary learning", shortName = "Dictionary learning", offlineResources = ["images/logo.png"])
@NpmPackage(value = "line-awesome", version = "1.3.0")
class DictionaryLearningApplication : SpringBootServletInitializer(), AppShellConfigurator

fun main(args: Array<String>) {
	runApplication<DictionaryLearningApplication>(*args)
}
