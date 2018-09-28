package br.com.ilhasoft.voy.network.projects

import br.com.ilhasoft.voy.connectivity.CheckConnectionProvider
import br.com.ilhasoft.voy.models.Project
import io.reactivex.Flowable
import io.reactivex.Single

/**
 * Created by erickjones on 02/03/18.
 */
class ProjectRepository(
    private val remoteProjectDataSource: ProjectDataSource,
    private val localProjectDataSource: ProjectDataSource,
    private val connectionProvider: CheckConnectionProvider
): ProjectDataSource {

    override fun getProjects(lang: String): Flowable<MutableList<Project>> {
        return if (connectionProvider.hasConnection()) {
            remoteProjectDataSource.getProjects(lang)
        } else {
            localProjectDataSource.getProjects(lang)
        }
    }

    override fun getProject(projectId: Int, lang: String): Single<Project> = remoteProjectDataSource.getProject(projectId, lang)

    override fun saveProjects(projects: MutableList<Project>): Flowable<MutableList<Project>> {
        return if (connectionProvider.hasConnection()) {
            localProjectDataSource.saveProjects(projects)
        } else {
            Flowable.just(projects)
        }
    }
}