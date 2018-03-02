package br.com.ilhasoft.voy.network.projects

import br.com.ilhasoft.voy.models.Project
import io.reactivex.Flowable
import io.reactivex.Single

/**
 * Created by erickjones on 02/03/18.
 */

interface ProjectDataSource {
    fun getProjects(): Flowable<MutableList<Project>>
    fun getProject(projectId: Int): Single<Project>
}