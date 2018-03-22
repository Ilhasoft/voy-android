package br.com.ilhasoft.voy.ui.home

import br.com.ilhasoft.voy.models.Notification
import br.com.ilhasoft.voy.models.Project
import br.com.ilhasoft.voy.models.Theme
import br.com.ilhasoft.voy.network.notification.NotificationRepository
import br.com.ilhasoft.voy.network.projects.ProjectRepository
import br.com.ilhasoft.voy.network.themes.ThemeRepository
import br.com.ilhasoft.voy.shared.extensions.fromIoToMainThread
import br.com.ilhasoft.voy.shared.schedulers.BaseScheduler
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe

/**
 * Created by lucas on 07/02/18.
 */
class HomeInteractorImpl(
    private val themeRepository: ThemeRepository,
    private val projectRepository: ProjectRepository,
    private val notificationRepository: NotificationRepository,
    private val scheduler: BaseScheduler
) : HomeInteractor {

    override fun getProjects(userId: Int): Flowable<MutableList<Project>> {
        return projectRepository.getProjects()
            .fromIoToMainThread(scheduler)
            .flatMap { projectRepository.saveProjects(it) }
    }

    override fun getThemes(projectId: Int, userId: Int): Flowable<MutableList<Theme>> {
        return themeRepository.getThemes(projectId, userId)
            .flatMap { themeRepository.saveThemes(it) }
            .fromIoToMainThread(scheduler)
    }

    override fun getTheme(themeId: Int): Maybe<Theme> {
        return themeRepository.getTheme(themeId = themeId).toMaybe()
    }

    override fun getNotifications(): Flowable<List<Notification>> =
        notificationRepository.getNotifications()
            .fromIoToMainThread(scheduler)

    override fun markAsRead(notificationId: Int): Completable =
        notificationRepository.markAsRead(notificationId)
            .fromIoToMainThread(scheduler)
}