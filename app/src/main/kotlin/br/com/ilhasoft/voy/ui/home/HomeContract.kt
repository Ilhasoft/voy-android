package br.com.ilhasoft.voy.ui.home

import br.com.ilhasoft.support.core.mvp.BasicView
import br.com.ilhasoft.voy.models.Project

interface HomeContract : BasicView {

    fun fillProjectsAdapter(projects: MutableList<Project>)

    fun navigateToMyAccount()

    fun selectProject()

    fun swapProject(project: Project?)

    fun showNotifications()

    fun dismissNotifications()

    fun navigateToNotificationDetail()

}