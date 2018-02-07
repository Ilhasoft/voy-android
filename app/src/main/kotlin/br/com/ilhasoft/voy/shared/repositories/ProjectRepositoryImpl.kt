package br.com.ilhasoft.voy.shared.repositories

import br.com.ilhasoft.voy.db.project.ProjectDbHelper
import br.com.ilhasoft.voy.models.Project
import br.com.ilhasoft.voy.network.projects.ProjectService
import br.com.ilhasoft.voy.shared.connectivity.ConnectivityManager
import io.reactivex.Flowable

/**
 * Created by lucas on 07/02/18.
 */
class ProjectRepositoryImpl : ProjectRepository {

    private val projectsService = ProjectService()
    private val projectsDb = ProjectDbHelper()

    override fun getProjects(): Flowable<MutableList<Project>> {
        return if (ConnectivityManager.isConnected()) {
            projectsService.getProjects()
        } else {
            Flowable.fromCallable { projectsDb.getProjects() }
        }
    }

    override fun saveProjects(projects: MutableList<Project>) {
        if(!ConnectivityManager.isConnected()) {
            //TODO: save projects locale
        }
    }

}