package br.com.ilhasoft.voy.db.project

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

/**
 * Created by lucas on 07/02/18.
 */
open class ProjectDbModel : RealmObject() {
    @PrimaryKey
    var id: Int = 0
    var name: String = "ProjectName"
}