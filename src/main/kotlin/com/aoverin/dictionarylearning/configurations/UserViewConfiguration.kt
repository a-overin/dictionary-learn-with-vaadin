package com.aoverin.dictionarylearning.configurations

import com.aoverin.dictionarylearning.views.MainLayout
import com.aoverin.dictionarylearning.views.game.match.MatchGame
import com.aoverin.dictionarylearning.views.main.MainView
import com.aoverin.dictionarylearning.views.words.AddWords
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class UserViewConfiguration {

    @Bean
    fun userViews(): Array<MainLayout.MenuItemInfo> {
        return arrayOf(
            MainLayout.MenuItemInfo("Main", "la la-globe", MainView::class.java),
            MainLayout.MenuItemInfo("Match Game", "la la-globe", MatchGame::class.java),
            MainLayout.MenuItemInfo("Add Words", "la la-globe", AddWords::class.java)
        )
    }
}