package br.com.ilhasoft.voy.db.theme

import io.realm.RealmObject

/**
 * Created by lucasbarros on 08/02/18.
 */
open class StringDbModel : RealmObject() {
    var text: String = ""
}