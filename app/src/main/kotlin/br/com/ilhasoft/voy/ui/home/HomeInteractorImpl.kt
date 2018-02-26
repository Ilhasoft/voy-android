package br.com.ilhasoft.voy.ui.home

import br.com.ilhasoft.voy.connectivity.ConnectivityManager
import br.com.ilhasoft.voy.db.project.ProjectDbHelper
import br.com.ilhasoft.voy.db.theme.ThemeDbHelper
import br.com.ilhasoft.voy.models.Notification
import br.com.ilhasoft.voy.models.Project
import br.com.ilhasoft.voy.models.Theme
import br.com.ilhasoft.voy.network.notification.NotificationService
import br.com.ilhasoft.voy.network.projects.ProjectService
import br.com.ilhasoft.voy.network.themes.ThemeService
import br.com.ilhasoft.voy.shared.extensions.fromIoToMainThread
import br.com.ilhasoft.voy.shared.extensions.onMainThread
import io.reactivex.Completable
import io.reactivex.Flowable

/**
 * Created by lucas on 07/02/18.
 */
class HomeInteractorImpl : HomeInteractor {

    private val projectsService by lazy { ProjectService() }
    private val projectsDbHelper by lazy { ProjectDbHelper() }

    private val themeService by lazy { ThemeService() }
    private val themeDbHelper by lazy { ThemeDbHelper() }

    private val notificationService by lazy { NotificationService() }

    override fun getProjects(userId: Int): Flowable<MutableList<Project>> {
        return if (ConnectivityManager.isConnected()) {
            var projects = mutableListOf<Project>()
            projectsService.getProjects()
                .fromIoToMainThread()
                .flatMap { projectsDbHelper.saveProjects(it) }
                .flatMap {
                    projects = it
                    Flowable.fromIterable(it)
                }
                .flatMap { getThemes(it.id, userId) }
                .map { projects }

        } else {
            projectsDbHelper.getProjects().onMainThread()
        }
    }

    override fun getThemes(projectId: Int, userId: Int): Flowable<MutableList<Theme>> {
        return if (ConnectivityManager.isConnected()) {
            themeService.getThemes(projectId, userId)
                .fromIoToMainThread()
                .flatMap { themeDbHelper.saveThemes(it) }
        } else {
            themeDbHelper.getThemes(projectId).onMainThread()
        }
    }

    override fun getNotifications(): Flowable<List<Notification>> =
        notificationService.getNotifications().fromIoToMainThread()

    override fun markAsRead(notificationId: Int): Completable =
        notificationService.markAsRead(notificationId).fromIoToMainThread()
}