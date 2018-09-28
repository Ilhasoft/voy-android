package br.com.ilhasoft.voy.db.project

import br.com.ilhasoft.voy.models.Project
import br.com.ilhasoft.voy.network.projects.ProjectDataSource
import br.com.ilhasoft.voy.shared.extensions.onMainThread
import br.com.ilhasoft.voy.shared.schedulers.BaseScheduler
import io.reactivex.Flowable
import io.reactivex.Single
import io.realm.Realm

/**
 * Created by lucas on 07/02/18.
 */
class ProjectDbHelper(private val realm: Realm, private val scheduler: BaseScheduler) :
    ProjectDataSource {

    override fun getProjects(lang: String): Flowable<MutableList<Project>> {
        return Flowable.just(realm)
            .onMainThread(scheduler)
            .flatMap {
                Flowable.just(it.where(ProjectDbModel::class.java).findAll())
            }.map { it.map { Project(id = it.id, name = it.name) }.toMutableList() }
    }

    override fun getProject(projectId: Int, lang:String): Single<Project> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun saveProjects(projects: MutableList<Project>): Flowable<MutableList<Project>> {
        return Flowable.fromIterable(projects)
            .onMainThread(scheduler)
            .doOnNext { project ->
                realm.executeTransaction {
                    it.copyToRealmOrUpdate(createDbModel(project))
                }
            }
            .toList()
            .flatMapPublisher { Flowable.just(it) }
    }

    private fun createDbModel(project: Project): ProjectDbModel {
        return ProjectDbModel().apply {
            id = project.id
            name = project.name
        }
    }
}