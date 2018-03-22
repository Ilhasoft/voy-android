package br.com.ilhasoft.voy

import com.crashlytics.android.Crashlytics
import com.squareup.picasso.Picasso
import io.fabric.sdk.android.Fabric

/**
 * Created by daniel on 11/07/17.
 */
fun Application.configureLogs() {
    Fabric.with(context, Crashlytics())
}
