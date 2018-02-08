package br.com.ilhasoft.voy.db

import io.realm.RealmList
import io.realm.RealmObject

/**
 * Created by lucasbarros on 08/02/18.
 */
open class BoundListDbModel : RealmObject() {
    var list: RealmList<BoundDbModel> = RealmList()
}