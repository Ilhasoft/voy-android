package br.com.ilhasoft.voy.ui.home

import br.com.ilhasoft.voy.models.Project
import io.reactivex.Flowable

/**
 * Created by lucas on 07/02/18.
 */
interface HomeInteractor {
    fun getProjects(): Flowable<MutableList<Project>>
}