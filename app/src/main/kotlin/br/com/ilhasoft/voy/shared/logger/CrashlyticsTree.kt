package br.com.ilhasoft.voy.shared.logger

import android.text.TextUtils
import android.util.Log
import com.crashlytics.android.Crashlytics
import timber.log.Timber

/**
 * Created by daniel on 11/10/17.
 */
class CrashlyticsTree : Timber.Tree() {

    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        if (priority == Log.ERROR) {
            if (t != null) {
                Crashlytics.logException(t)
            } else if (!TextUtils.isEmpty(tag) && !TextUtils.isEmpty(message)) {
                Crashlytics.log(priority, tag, message)
            } else if (!TextUtils.isEmpty(message)) {
                Crashlytics.log(message)
            }
        }
    }

}
