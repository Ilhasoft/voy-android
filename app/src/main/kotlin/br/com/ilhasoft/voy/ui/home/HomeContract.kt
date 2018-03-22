package br.com.ilhasoft.voy.ui.home

import br.com.ilhasoft.voy.models.Notification
import br.com.ilhasoft.voy.models.Project
import br.com.ilhasoft.voy.models.Theme
import br.com.ilhasoft.voy.ui.base.BaseView

interface HomeContract : BaseView {

    fun fillProjectsAdapter(projects: MutableList<Project>)

    fun fillThemesAdapter(themes: MutableList<Theme>)

    fun navigateToMyAccount()

    fun selectProject()

    fun swapProject(project: Project)

    fun showNotifications()

    fun dismissNotifications()

    fun navigateToNotificationDetail(notification: Notification)

    fun navigateToThemeReports(theme: Theme)

    fun fillNotificationAdapter(notifications: List<Notification>)

    fun putThemeOnThemeData(theme: Theme)

}