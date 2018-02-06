package br.com.ilhasoft.voy

import android.app.Application

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
    }

}