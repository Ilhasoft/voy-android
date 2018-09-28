package br.com.ilhasoft.voy.network.projects

import br.com.ilhasoft.voy.models.Project
import br.com.ilhasoft.voy.network.ServiceFactory
import io.reactivex.Flowable
import io.reactivex.Single

/**
 * Created by developer on 09/01/18.
 */
class ProjectService : ServiceFactory<ProjectApi>(ProjectApi::class.java), ProjectDataSource {

    override fun getProjects(lang: String): Flowable<MutableList<Project>> = api.getProjects(lang)

    override fun getProject(projectId: Int, lang: String): Single<Project> = api.getProject(projectId, lang)

    override fun saveProjects(projects: MutableList<Project>): Flowable<MutableList<Project>> {
        return Flowable.just(projects)
    }
}