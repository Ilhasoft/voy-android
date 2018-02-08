package br.com.ilhasoft.voy.ui.home

import br.com.ilhasoft.voy.connectivity.ConnectivityManager
import br.com.ilhasoft.voy.db.project.ProjectDbHelper
import br.com.ilhasoft.voy.models.Project
import br.com.ilhasoft.voy.network.projects.ProjectService
import io.reactivex.Flowable

/**
 * Created by lucas on 07/02/18.
 */
class HomeInteractorImpl : HomeInteractor {

    private val projectsService = ProjectService()
    private val projectsDbHelper = ProjectDbHelper()

    override fun getProjects(): Flowable<MutableList<Project>> {
        return if (ConnectivityManager.isConnected()) {
            projectsService.getProjects()
                    .flatMap { projectsDbHelper.saveProjects(it) }

        } else {
            projectsDbHelper.getProjects()
        }
    }
}