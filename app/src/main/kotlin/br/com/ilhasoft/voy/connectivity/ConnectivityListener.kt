package br.com.ilhasoft.voy.connectivity

/**
 * Created by lucasbarros on 06/02/18.
 */
interface ConnectivityListener {
    fun onNetworkStatusChange(isConnect: Boolean)
}