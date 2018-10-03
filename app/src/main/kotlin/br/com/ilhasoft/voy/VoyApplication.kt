package br.com.ilhasoft.voy

import android.content.Context
import android.support.multidex.MultiDex
import android.support.multidex.MultiDexApplication
import io.realm.Realm
import io.realm.RealmConfiguration
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesUtil
import com.google.android.gms.common.GooglePlayServicesRepairableException
import com.google.android.gms.security.ProviderInstaller


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
        installServiceProviderIfNeeded(this)
        instance = this
        Realm.init(this)
        val config = RealmConfiguration.Builder().name("voy.realm").build()
        Realm.setDefaultConfiguration(config)
        configureLogs()
    }

    private fun installServiceProviderIfNeeded(context: Context) {
        try {
            ProviderInstaller.installIfNeeded(context)
        } catch (e: GooglePlayServicesRepairableException) {
            e.printStackTrace()

            // Prompt the user to install/update/enable Google Play services.
            GooglePlayServicesUtil.showErrorNotification(e.connectionStatusCode, context)

        } catch (e: GooglePlayServicesNotAvailableException) {
            e.printStackTrace()
        }

    }
}