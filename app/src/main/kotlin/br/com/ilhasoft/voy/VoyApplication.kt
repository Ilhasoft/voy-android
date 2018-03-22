package br.com.ilhasoft.voy

import android.content.Context
import android.support.multidex.MultiDex
import android.support.multidex.MultiDexApplication
import io.realm.Realm
import io.realm.RealmConfiguration


/**
 * Created by lucasbarros on 06/02/18.
 */

class VoyApplication : MultiDexApplication() {

    companion object {
        lateinit var instance: VoyApplication
            private set
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        Realm.init(this)
        val config = RealmConfiguration.Builder().name("voy.realm").build()
        Realm.setDefaultConfiguration(config)
        configureLogs()
    }
}