package br.com.ilhasoft.voy

import android.app.Application
import android.content.Context
import android.support.v4.app.FragmentManager
import com.facebook.stetho.Stetho
import com.uphyca.stetho_realm.RealmInspectorModulesProvider
import timber.log.Timber
import java.util.regex.Pattern

/**
 * Created by daniel on 11/07/17.
 */
fun Application.configureLogs() {
    FragmentManager.enableDebugLogging(true)
    Timber.plant(object : Timber.DebugTree() {
        override fun createStackElementTag(element: StackTraceElement): String {
            return String.format("%s:%s", super.createStackElementTag(element), element.lineNumber)
        }
    })
    configStetho(this)
}

private fun configStetho(context: Context) {
    Stetho.initialize(
        Stetho.newInitializerBuilder(context)
            .enableDumpapp(Stetho.defaultDumperPluginsProvider(context))
            .enableWebKitInspector(
                RealmInspectorModulesProvider.builder(context).databaseNamePattern(
                    Pattern.compile(".+\\.realm")).build())
            .build()
    )
}