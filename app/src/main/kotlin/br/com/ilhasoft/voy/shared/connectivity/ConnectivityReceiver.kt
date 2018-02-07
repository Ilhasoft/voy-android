package br.com.ilhasoft.voy.shared.connectivity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import br.com.ilhasoft.voy.VoyApplication


/**
 * Created by lucasbarros on 06/02/18.
 */
class ConnectivityReceiver : BroadcastReceiver() {

    var connectivityListener: ConnectivityListener? = null

    override fun onReceive(context: Context, intent: Intent) {
        connectivityListener?.onNetworkStatusChange(isConnected())
    }

    fun isConnected(): Boolean {
        val connectivityManager = VoyApplication.instance.applicationContext
                .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetworkInfo

        return activeNetwork != null && activeNetwork.isConnectedOrConnecting
    }
}