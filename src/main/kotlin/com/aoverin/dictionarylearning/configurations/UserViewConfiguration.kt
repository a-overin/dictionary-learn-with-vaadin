package com.aoverin.dictionarylearning.configurations

import com.aoverin.dictionarylearning.views.MainLayout
import com.aoverin.dictionarylearning.views.game.match.MatchGame
import com.aoverin.dictionarylearning.views.main.MainView
import com.aoverin.dictionarylearning.views.pack.EditPacksView
import com.aoverin.dictionarylearning.views.words.EditWordsView
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class UserViewConfiguration {

    @Bean
    fun userViews(): Array<MainLayout.MenuItemInfo> {
        return arrayOf(
            MainLayout.MenuItemInfo("Main", "la la-globe", MainView::class.java),
            MainLayout.MenuItemInfo("Match Game", "la la-globe", MatchGame::class.java),
            MainLayout.MenuItemInfo("Edit Words", "la la-globe", EditWordsView::class.java),
            MainLayout.MenuItemInfo("Edit Packs", "la la-globe", EditPacksView::class.java)
        )
    }
}