package br.com.ilhasoft.voy

import android.app.Application
import com.crashlytics.android.Crashlytics
import io.fabric.sdk.android.Fabric

/**
 * Created by daniel on 11/07/17.
 */
fun Application.configureLogs() {
    Fabric.with(this, Crashlytics())
}
