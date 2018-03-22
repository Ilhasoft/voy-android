package br.com.ilhasoft.voy

import android.content.Context
import android.support.multidex.MultiDex
import android.support.multidex.MultiDexApplication
import com.facebook.stetho.Stetho
import com.uphyca.stetho_realm.RealmInspectorModulesProvider
import io.realm.Realm
import io.realm.RealmConfiguration
import timber.log.Timber
import java.util.regex.Pattern


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

        if (BuildConfig.DEBUG) {
            configStetho()
            Timber.plant(Timber.DebugTree())
        }
    }

    private fun configStetho() {
        Stetho.initialize(
            Stetho.newInitializerBuilder(this)
                .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                .enableWebKitInspector(RealmInspectorModulesProvider.builder(this).databaseNamePattern(
                    Pattern.compile(".+\\.realm")).build())
                .build()
        )
    }

}