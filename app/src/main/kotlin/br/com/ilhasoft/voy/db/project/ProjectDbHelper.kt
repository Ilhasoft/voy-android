package br.com.ilhasoft.voy.db.project

import br.com.ilhasoft.voy.models.Project
import io.realm.Realm
import timber.log.Timber

/**
 * Created by lucas on 07/02/18.
 */
class ProjectDbHelper {

    private val realm by lazy { Realm.getDefaultInstance() }

    fun getProjects() : MutableList<Project> {

        realm.beginTransaction()
        val p = realm.createObject(ProjectLocal::class.java)
        p.id = 1
        p.name = "Local"
        realm.commitTransaction()

        val proj = realm.where(ProjectLocal::class.java).findAll()

        val response = proj.map { Project(id = it.id, name = it.name) }.toMutableList()
        realm.close()
        return response
    }

}