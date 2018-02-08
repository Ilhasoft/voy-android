package br.com.ilhasoft.voy.db

import io.realm.RealmObject

/**
 * Created by lucasbarros on 08/02/18.
 */
open class BoundDbModel: RealmObject() {
    var lat: Double = 0.0
    var lng: Double = 0.0
}