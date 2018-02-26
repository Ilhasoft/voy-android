package br.com.ilhasoft.voy.ui.home

import br.com.ilhasoft.voy.models.Project
import br.com.ilhasoft.voy.models.Theme
import io.reactivex.Flowable

/**
 * Created by lucas on 07/02/18.
 */
interface HomeInteractor {
    fun getProjects(userId: Int): Flowable<MutableList<Project>>

    fun getThemes(projectId: Int, userId: Int): Flowable<MutableList<Theme>>
}