package br.com.ilhasoft.voy.connectivity

import android.content.Context
import android.content.IntentFilter
import android.net.ConnectivityManager
import br.com.ilhasoft.voy.ui.base.AutoSendInteractor
import io.reactivex.subjects.PublishSubject

/**
 * Created by lucas on 07/02/18.
 */
class ConnectivityManager(private val sendPendingInteractor: AutoSendInteractor) : ConnectivityListener {

    companion object {
        private val connectivityReceiver by lazy { ConnectivityReceiver() }

        fun isConnected(): Boolean = connectivityReceiver.isConnected()
    }

    fun registerReceive(context: Context) {
        connectivityReceiver.connectivityListener = this
        context.registerReceiver(connectivityReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }

    fun unregisterReceiver(context: Context) {
        context.unregisterReceiver(connectivityReceiver)
    }

    override fun onNetworkStatusChange(isConnect: Boolean) {
        if (isConnect) {
            sendPendingInteractor.sendPendingReports()
        }
    }

}