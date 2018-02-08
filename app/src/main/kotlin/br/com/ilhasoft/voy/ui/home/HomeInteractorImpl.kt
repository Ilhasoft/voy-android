package br.com.ilhasoft.voy.ui.home

import br.com.ilhasoft.voy.connectivity.ConnectivityManager
import br.com.ilhasoft.voy.db.project.ProjectDbHelper
import br.com.ilhasoft.voy.db.theme.ThemeDbHelper
import br.com.ilhasoft.voy.models.Project
import br.com.ilhasoft.voy.models.Theme
import br.com.ilhasoft.voy.network.projects.ProjectService
import br.com.ilhasoft.voy.network.themes.ThemeService
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by lucas on 07/02/18.
 */
class HomeInteractorImpl : HomeInteractor {

    private val projectsService by lazy { ProjectService() }
    private val projectsDbHelper by lazy { ProjectDbHelper() }

    private val themeService by lazy { ThemeService() }
    private val themeDbHelper by lazy { ThemeDbHelper() }

    override fun getProjects(): Flowable<MutableList<Project>> {
        return if (ConnectivityManager.isConnected()) {
            projectsService.getProjects()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .flatMap { projectsDbHelper.saveProjects(it) }

        } else {
            projectsDbHelper.getProjects()
        }
    }

    override fun getThemes(projectId: Int, userId: Int): Flowable<MutableList<Theme>> {
        return if (ConnectivityManager.isConnected()) {
            themeService.getThemes(projectId, userId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .flatMap { themeDbHelper.saveThemes(it) }
        } else {
            themeDbHelper.getThemes(projectId).subscribeOn(AndroidSchedulers.mainThread())
        }
    }
}