package br.com.ilhasoft.voy.ui.home

import br.com.ilhasoft.support.core.mvp.Presenter
import br.com.ilhasoft.voy.models.*
import br.com.ilhasoft.voy.network.projects.ProjectService
import br.com.ilhasoft.voy.network.themes.ThemeService
import br.com.ilhasoft.voy.shared.helpers.RxHelper
import timber.log.Timber

class HomePresenter(preferences: Preferences) : Presenter<HomeContract>(HomeContract::class.java) {

    private val projectService: ProjectService by lazy { ProjectService() }
    private val themeService: ThemeService by lazy { ThemeService() }

    private var selectedProject: Project? = null
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
                    if (it.isNotEmpty()) {
                        selectedProject = it.first()
                        loadThemesData(selectedProject!!.id)
                    }
                }, { Timber.e(it) })
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

    fun onClickChooseProject() {
        view.selectProject()
    }

    fun onClickNotifications() {
        view.showNotifications()
    }

    fun onClickHeaderNavView() {
        view.dismissNotifications()
    }

    fun onClickProject(project: Project) {
        selectedProject = project
        view.swapProject(selectedProject!!)
        loadThemesData(selectedProject!!.id)
    }

    fun onClickItemNotification(notification: Notification?) {
        view.navigateToNotificationDetail()
    }

    fun onClickTheme(theme: Theme) {
        view.navigateToThemeReports(theme)
    }

    fun isSelectedProject(project: Project): Boolean = selectedProject?.id == project.id

}