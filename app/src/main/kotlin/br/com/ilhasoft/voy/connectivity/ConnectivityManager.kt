package br.com.ilhasoft.voy.connectivity

import android.content.Context
import android.content.IntentFilter
import android.net.ConnectivityManager
import io.reactivex.subjects.PublishSubject

/**
 * Created by lucas on 07/02/18.
 */
class ConnectivityManager : ConnectivityListener {

    companion object {
        private val connectivityReceiver by lazy { ConnectivityReceiver() }

        fun isConnected(): Boolean = connectivityReceiver.isConnected()
    }

    val status = PublishSubject.create<Boolean>().toSerialized()

    fun registerReceive(context: Context) {
        context.registerReceiver(connectivityReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }

    fun unregisterReceiver(context: Context) {
        context.unregisterReceiver(connectivityReceiver)
    }

    override fun onNetworkStatusChange(isConnect: Boolean) {
        status.onNext(isConnect)
    }

}