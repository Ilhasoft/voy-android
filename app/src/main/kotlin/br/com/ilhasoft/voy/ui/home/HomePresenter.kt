package br.com.ilhasoft.voy.ui.home

import br.com.ilhasoft.support.core.mvp.Presenter
import br.com.ilhasoft.voy.models.*
import br.com.ilhasoft.voy.network.projects.ProjectService
import br.com.ilhasoft.voy.network.themes.ThemeService
import br.com.ilhasoft.voy.shared.helpers.RxHelper
import timber.log.Timber

class HomePresenter(private val preferences: Preferences) : Presenter<HomeContract>(HomeContract::class.java) {

    private var selectedProject: Project? = null
    private val projectService: ProjectService by lazy { ProjectService() }
    private val themeService: ThemeService by lazy { ThemeService() }
    private val userId = preferences.getInt(User.ID)

    override fun attachView(view: HomeContract) {
        super.attachView(view)
        view.showLoading()
        loadData()
        view.dismissLoading()
    }

    private fun loadData() {
        projectService.getProjects()
                .compose(RxHelper.defaultFlowableSchedulers())
                .doOnSubscribe { view.showLoading() }
                .doOnTerminate { view.dismissLoading() }
                .subscribe({
                    fillProjectsAdapter(it)
                    if (it.isNotEmpty())
                        loadThemesData(it.first().id)
                }, { Timber.e(it) })
    }

    private fun loadProjectsData() {
        projectService.getProjects()
                .compose(RxHelper.defaultFlowableSchedulers())
                .subscribe({ fillProjectsAdapter(it) }, {})
    }

    private fun loadThemesData(projectId: Int) {
        themeService.getThemes(projectId, user = userId)
                .compose(RxHelper.defaultFlowableSchedulers())
                .doOnSubscribe { view.showLoading() }
                .doOnTerminate { view.dismissLoading() }
                .subscribe({ fillThemesAdapter(it) }, { Timber.e(it) })
    }

    private fun fillProjectsAdapter(projects: MutableList<Project>) {
        view.fillProjectsAdapter(projects)
    }

    private fun fillThemesAdapter(themes: MutableList<Theme>) {
        view.fillThemesAdapter(themes)
    }

    fun onClickMyAccount() {
        view.navigateToMyAccount()
    }

    fun onClickSelectProject() {
        view.selectProject()
    }

    fun onClickNotifications() {
        view.showNotifications()
    }

    fun onClickHeaderNavView() {
        view.dismissNotifications()
    }

    fun onClickProject(project: Project) {
        view.swapProject(project)
        loadThemesData(project.id)
    }

    fun onClickItemNotification(notification: Notification?) {
        view.navigateToNotificationDetail()
    }

    fun onClickTheme(theme: Theme) {
        view.navigateToThemeReports(theme)
    }

    fun setSelectedProject(project: Project?) {
        selectedProject = project
    }

    fun getSelectedProject(): Project? = selectedProject

}