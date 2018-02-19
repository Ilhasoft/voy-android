package br.com.ilhasoft.voy.models

/**
 * Created by lucasbarros on 05/01/18.
 */
interface Preferences {

    fun contains(key: String): Boolean

    fun getString(key: String, defaultValue: String = ""): String

    fun getInt(key: String, defaultValue: Int = 0): Int

    fun getLong(key: String, defaultValue: Long = 0): Long

    fun getFloat(key: String, defaultValue: Float = 0.0F): Float

    fun getBoolean(key: String, defaultValue: Boolean = false): Boolean

    fun put(key: String, value: Any?)

    fun remove(key: String)

    fun clear()

}