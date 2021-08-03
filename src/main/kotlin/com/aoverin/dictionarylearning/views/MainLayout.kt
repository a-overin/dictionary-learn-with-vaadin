package com.aoverin.dictionarylearning.views

import com.aoverin.dictionarylearning.models.UserModel
import com.aoverin.dictionarylearning.security.AuthenticatedUser
import com.aoverin.dictionarylearning.views.game.write.WriteGame
import com.aoverin.dictionarylearning.views.main.MainView
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.ComponentUtil
import com.vaadin.flow.component.Text
import com.vaadin.flow.component.applayout.AppLayout
import com.vaadin.flow.component.applayout.DrawerToggle
import com.vaadin.flow.component.avatar.Avatar
import com.vaadin.flow.component.contextmenu.ContextMenu
import com.vaadin.flow.component.html.Anchor
import com.vaadin.flow.component.html.H1
import com.vaadin.flow.component.html.Image
import com.vaadin.flow.component.html.Span
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.tabs.Tab
import com.vaadin.flow.component.tabs.Tabs
import com.vaadin.flow.component.tabs.TabsVariant
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.RouterLink
import com.vaadin.flow.server.auth.AccessAnnotationChecker
import java.util.*

@PageTitle("Main")
open class MainLayout(authenticatedUser: AuthenticatedUser, accessChecker: AccessAnnotationChecker) :
    AppLayout() {
    class MenuItemInfo(val text: String, val iconClass: String, val view: Class<out Component>)

    private val menu: Tabs
    private var viewTitle: H1? = null
    private val authenticatedUser: AuthenticatedUser
    private val accessChecker: AccessAnnotationChecker
    private fun createHeaderContent(): Component {
        val layout = HorizontalLayout()
        layout.className = "sidemenu-header"
        layout.themeList["dark"] = true
        layout.setWidthFull()
        layout.isSpacing = false
        layout.alignItems = FlexComponent.Alignment.CENTER
        layout.add(DrawerToggle())
        viewTitle = H1()
        layout.add(viewTitle)
        val maybeUser: Optional<UserModel> = authenticatedUser.get()
        if (maybeUser.isPresent) {
            val user: UserModel = maybeUser.get()
            val avatar = Avatar(user.userName, null)
            avatar.addClassNames("ms-auto", "me-m")
            val userMenu = ContextMenu(avatar)
            userMenu.isOpenOnClick = true
            userMenu.addItem(
                "Logout"
            ) { authenticatedUser.logout() }
            layout.add(avatar)
        } else {
            val loginLink = Anchor("login", "Sign in")
            loginLink.addClassNames("ms-auto", "me-m")
            layout.add(loginLink)
        }
        return layout
    }

    private fun createDrawerContent(menu: Tabs): Component {
        val layout = VerticalLayout()
        layout.className = "sidemenu-menu"
        layout.setSizeFull()
        layout.isPadding = false
        layout.isSpacing = false
        layout.themeList["spacing-s"] = true
        layout.alignItems = FlexComponent.Alignment.STRETCH
        val logoLayout = HorizontalLayout()
        logoLayout.setId("logo")
        logoLayout.alignItems = FlexComponent.Alignment.CENTER
        logoLayout.add(Image("images/logo.png", "My App logo"))
        logoLayout.add(H1("My App"))
        layout.add(logoLayout, menu)
        return layout
    }

    private fun createMenu(): Tabs {
        val tabs = Tabs()
        tabs.orientation = Tabs.Orientation.VERTICAL
        tabs.addThemeVariants(TabsVariant.LUMO_MINIMAL)
        tabs.setId("tabs")
        for (menuTab in createMenuItems()) {
            tabs.add(menuTab)
        }
        return tabs
    }

    private fun createMenuItems(): List<Tab> {
        val menuItems = arrayOf( //
            MenuItemInfo("Main", "la la-globe", MainView::class.java),
            MenuItemInfo("Write Game", "la la-globe", WriteGame::class.java),
//            MenuItemInfo("About", "la la-file", AboutView::class.java)
        )
        val tabs: MutableList<Tab> = ArrayList()
        for (menuItemInfo in menuItems) {
            if (accessChecker.hasAccess(menuItemInfo.view)) {
                tabs.add(createTab(menuItemInfo))
            }
        }
        return tabs
    }

    override fun afterNavigation() {
        super.afterNavigation()
        getTabForComponent(content).ifPresent { selectedTab: Tab? ->
            menu.selectedTab = selectedTab
        }
        viewTitle!!.text = currentPageTitle
    }

    private fun getTabForComponent(component: Component): Optional<Tab> {
        return menu.children.filter { tab: Component? ->
            ComponentUtil.getData(
                tab,
                Class::class.java
            ) == component.javaClass
        }
            .findFirst().map { `object`: Component? ->
                Tab::class.java.cast(
                    `object`
                )
            }
    }

    private val currentPageTitle: String
        get() {
            val title = content.javaClass.getAnnotation(PageTitle::class.java)
            return title?.value ?: ""
        }

    companion object {
        private fun createTab(menuItemInfo: MenuItemInfo): Tab {
            val tab = Tab()
            val link = RouterLink()
            link.setRoute(menuItemInfo.view)
            val iconElement = Span()
            iconElement.addClassNames("text-l", "pr-s")
            if (menuItemInfo.iconClass.isNotEmpty()) {
                iconElement.addClassNames(menuItemInfo.iconClass)
            }
            link.add(iconElement, Text(menuItemInfo.text))
            tab.add(link)
            ComponentUtil.setData(tab, Class::class.java, menuItemInfo.view)
            return tab
        }
    }

    init {
        this.authenticatedUser = authenticatedUser
        this.accessChecker = accessChecker
        primarySection = Section.DRAWER
        addToNavbar(true, createHeaderContent())
        menu = createMenu()
        addToDrawer(createDrawerContent(menu))
    }
}