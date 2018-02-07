package br.com.ilhasoft.voy

import android.app.Application
import io.realm.Realm
import io.realm.RealmConfiguration


/**
 * Created by lucasbarros on 06/02/18.
 */

class VoyApplication : Application() {

    companion object {
        lateinit var instance: VoyApplication
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        Realm.init(this)
        val config = RealmConfiguration.Builder().name("voy.realm").build()
        Realm.setDefaultConfiguration(config)
    }

}