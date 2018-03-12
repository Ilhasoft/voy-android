package br.com.ilhasoft.voy.db.base

import io.realm.Realm

/**
 * Created by felipe on 12/03/18.
 */
class BaseDbHelperImpl(private val realm: Realm): BaseDbHelper {

    override fun deleteAllData() {
        realm.executeTransaction {
            it.deleteAll()
        }
    }
}