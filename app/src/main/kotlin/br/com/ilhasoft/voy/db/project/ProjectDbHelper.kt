package br.com.ilhasoft.voy.db.project

import br.com.ilhasoft.voy.models.Project
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.realm.Realm

/**
 * Created by lucas on 07/02/18.
 */
class ProjectDbHelper {

    private val realm by lazy { Realm.getDefaultInstance() }

    fun getProjects(): Flowable<MutableList<Project>> {
        //TODO: Refactor to include realm with RxJava
        return Flowable.fromCallable {
            val localProjects = realm.where(ProjectDbModel::class.java).findAll()
            localProjects.map { Project(id = it.id, name = it.name) }.toMutableList()
        }
    }

    fun saveProjects(projects: MutableList<Project>): Flowable<MutableList<Project>> {
        return Flowable.fromCallable {
            var projectsDb = projects.map { project ->
                ProjectDbModel().apply {
                    id = project.id
                    name = project.name
                }
            }
            realm.beginTransaction()
            projectsDb = realm.copyToRealmOrUpdate(projectsDb)
            realm.commitTransaction()
            projectsDb.map { Project(id = it.id, name = it.name) }.toMutableList()
        }
    }

}