package br.com.ilhasoft.voy.ui.home

import br.com.ilhasoft.support.core.mvp.Presenter
import br.com.ilhasoft.voy.models.Notification
import br.com.ilhasoft.voy.models.Project
import br.com.ilhasoft.voy.network.projects.ProjectService
import br.com.ilhasoft.voy.shared.rx.RxHelper

class HomePresenter : Presenter<HomeContract>(HomeContract::class.java) {

    private var selectedProject: Project? = null
    private val projectService: ProjectService by lazy { ProjectService() }

    override fun attachView(view: HomeContract?) {
        super.attachView(view)
        loadProjectsData()
    }

    private fun loadProjectsData() {
        projectService.getProjects()
                .compose(RxHelper.defaultFlowableSchedulers())
                .doOnSubscribe { view?.showLoading() }
                .doOnTerminate { view?.dismissLoading() }
                .subscribe({ fillProjectsAdapter(it) }, {})
    }

    private fun fillProjectsAdapter(projects: MutableList<Project>) {
        view?.fillProjectsAdapter(projects)
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

    fun setSelectedProject(project: Project?) {
        selectedProject = project
    }

    fun getSelectedProject(): Project? = selectedProject

}