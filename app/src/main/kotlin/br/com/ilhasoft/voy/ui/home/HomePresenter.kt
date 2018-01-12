package br.com.ilhasoft.voy.ui.home

import br.com.ilhasoft.support.core.mvp.Presenter
import br.com.ilhasoft.voy.models.Notification
import br.com.ilhasoft.voy.models.Project
import br.com.ilhasoft.voy.models.Theme
import br.com.ilhasoft.voy.network.projects.ProjectService
import br.com.ilhasoft.voy.network.themes.ThemeService
import br.com.ilhasoft.voy.shared.helpers.RxHelper

class HomePresenter : Presenter<HomeContract>(HomeContract::class.java) {

    private var selectedProject: Project? = null
    private val projectService: ProjectService by lazy { ProjectService() }
    private val themeService: ThemeService by lazy { ThemeService() }

    override fun attachView(view: HomeContract?) {
        super.attachView(view)
        view?.showLoading()
        loadProjectsData()
        loadThemesData()
        view?.dismissLoading()
    }

    private fun loadProjectsData() {
        projectService.getProjects()
                .compose(RxHelper.defaultFlowableSchedulers())
                .subscribe({ fillProjectsAdapter(it) }, {})
    }

    private fun loadThemesData() {
        themeService.getThemes()
                .compose(RxHelper.defaultFlowableSchedulers())
                .subscribe({ fillThemesAdapter(it) }, {})
    }

    private fun fillProjectsAdapter(projects: MutableList<Project>) {
        view?.fillProjectsAdapter(projects)
    }

    private fun fillThemesAdapter(themes: MutableList<Theme>) {
        view?.fillThemesAdapter(themes)
    }

    fun onClickMyAccount() {
        view?.navigateToMyAccount()
    }

    fun onClickSelectProject() {
        view?.selectProject()
    }

    fun onClickNotifications() {
        view?.showNotifications()
    }

    fun onClickHeaderNavView() {
        view?.dismissNotifications()
    }

    fun onClickProject(project: Project?) {
        view?.swapProject(project)
    }

    fun onClickItemNotification(notification: Notification?) {
        view?.navigateToNotificationDetail()
    }

    fun onClickTheme(theme: Theme?) {
        view?.navigateToThemeReports(theme)
    }

    fun setSelectedProject(project: Project?) {
        selectedProject = project
    }

    fun getSelectedProject(): Project? = selectedProject

}