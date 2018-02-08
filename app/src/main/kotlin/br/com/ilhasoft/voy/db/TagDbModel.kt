package br.com.ilhasoft.voy.db

import io.realm.RealmObject

/**
 * Created by lucasbarros on 08/02/18.
 */
open class TagDbModel: RealmObject() {
    var tag: String = ""
}