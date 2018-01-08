package br.com.ilhasoft.voy.shared.extensions

/**
 * Created by lucasbarros on 08/01/18.
 */
fun <K, V> MutableMap<K, V>.putIfNotNull(key: K, value: V?) {
    value?.let { put(key, it) }
}