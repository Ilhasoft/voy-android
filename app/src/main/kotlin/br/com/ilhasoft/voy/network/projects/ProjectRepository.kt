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

    override fun getProjects(): Flowable<MutableList<Project>> {
        return if (connectionProvider.hasConnection()) {
            remoteProjectDataSource.getProjects()
        } else {
            localProjectDataSource.getProjects()
        }
    }

    override fun getProject(projectId: Int): Single<Project> = remoteProjectDataSource.getProject(projectId)

    override fun saveProjects(projects: MutableList<Project>): Flowable<MutableList<Project>> {
        return if (connectionProvider.hasConnection()) {
            localProjectDataSource.saveProjects(projects)
        } else {
            Flowable.just(projects)
        }
    }
}