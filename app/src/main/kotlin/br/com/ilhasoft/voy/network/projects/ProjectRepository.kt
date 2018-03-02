package br.com.ilhasoft.voy.network.projects

import br.com.ilhasoft.voy.models.Project
import io.reactivex.Flowable
import io.reactivex.Single

/**
 * Created by erickjones on 02/03/18.
 */
class ProjectRepository(val remoteProjectDataSource: ProjectDataSource): ProjectDataSource {

    override fun getProjects(): Flowable<MutableList<Project>> =
            remoteProjectDataSource.getProjects()

    override fun getProject(projectId: Int): Single<Project> =
            remoteProjectDataSource.getProject(projectId)

}