package br.com.ilhasoft.voy.ui.home

import br.com.ilhasoft.voy.connectivity.ConnectivityManager
import br.com.ilhasoft.voy.db.project.ProjectDbHelper
import br.com.ilhasoft.voy.models.Notification
import br.com.ilhasoft.voy.models.Project
import br.com.ilhasoft.voy.models.Theme
import br.com.ilhasoft.voy.network.notification.NotificationRepository
import br.com.ilhasoft.voy.network.projects.ProjectRepository
import br.com.ilhasoft.voy.network.themes.ThemeRepository
import br.com.ilhasoft.voy.shared.extensions.fromIoToMainThread
import br.com.ilhasoft.voy.shared.extensions.onMainThread
import br.com.ilhasoft.voy.shared.schedulers.BaseScheduler
import io.reactivex.Completable
import io.reactivex.Flowable

/**
 * Created by lucas on 07/02/18.
 */
class HomeInteractorImpl(
    val themeRepository: ThemeRepository,
    val projectRepository: ProjectRepository,
    val notificationRepository: NotificationRepository,
    private val scheduler: BaseScheduler
) : HomeInteractor {

    private val projectsDbHelper by lazy { ProjectDbHelper() }

    override fun getProjects(userId: Int): Flowable<MutableList<Project>> {
        return if (ConnectivityManager.isConnected()) {
            var projects = mutableListOf<Project>()
            projectRepository.getProjects()
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
        return themeRepository.getThemes(projectId, userId)
            .flatMap { themeRepository.saveThemes(it) }
            .fromIoToMainThread(scheduler)
    }

    override fun getNotifications(): Flowable<List<Notification>> =
        notificationRepository.getNotifications()
            .fromIoToMainThread(scheduler)

    override fun markAsRead(notificationId: Int): Completable =
        notificationRepository.markAsRead(notificationId)
            .fromIoToMainThread(scheduler)
}