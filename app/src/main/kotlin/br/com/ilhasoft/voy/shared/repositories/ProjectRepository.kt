package br.com.ilhasoft.voy.shared.repositories

import br.com.ilhasoft.voy.models.Project
import io.reactivex.Flowable

/**
 * Created by lucas on 07/02/18.
 */
interface ProjectRepository {
    fun getProjects(): Flowable<MutableList<Project>>

    fun saveProjects(projects: MutableList<Project>)
}