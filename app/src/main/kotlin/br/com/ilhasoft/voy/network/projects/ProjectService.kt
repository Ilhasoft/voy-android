package br.com.ilhasoft.voy.network.projects

import br.com.ilhasoft.voy.models.Project
import br.com.ilhasoft.voy.network.ServiceFactory
import io.reactivex.Flowable
import io.reactivex.Single

/**
 * Created by developer on 09/01/18.
 */
class ProjectService : ServiceFactory<ProjectApi>(ProjectApi::class.java), ProjectDataSource {

    override fun getProjects(): Flowable<MutableList<Project>> = api.getProjects()

    override fun getProject(projectId: Int): Single<Project> = api.getProject(projectId)

    override fun saveProjects(projects: MutableList<Project>): Flowable<MutableList<Project>> {
        return Flowable.just(projects)
    }
}