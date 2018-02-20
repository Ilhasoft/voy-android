package br.com.ilhasoft.voy.models

import android.annotation.SuppressLint
import android.content.Context
import android.preference.PreferenceManager


/**
 * Created by lucasbarros on 05/01/18.
 */
@SuppressLint("CommitPrefEdits")
class SharedPreferences(context: Context) : Preferences {

    private val client = PreferenceManager.getDefaultSharedPreferences(context.applicationContext)
    private val editor by lazy { client.edit() }

    override fun contains(key: String): Boolean {
        return client.contains(key)
    }

    override fun getString(key: String, defaultValue: String): String {
        return client.getString(key, defaultValue)
    }

    override fun getInt(key: String, defaultValue: Int): Int {
        return client.getInt(key, defaultValue)
    }

    override fun getLong(key: String, defaultValue: Long): Long {
        return client.getLong(key, defaultValue)
    }

    override fun getFloat(key: String, defaultValue: Float): Float {
        return client.getFloat(key, defaultValue)
    }

    override fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return client.getBoolean(key, defaultValue)
    }

    override fun put(key: String, value: Any?) {
        when (value) {
            null -> editor.remove(key)
            is Boolean -> editor.putBoolean(key, value)
            is Float -> editor.putFloat(key, value)
            is Int -> editor.putInt(key, value)
            is Long -> editor.putLong(key, value)
            is String -> editor.putString(key, value)
            is Set<*> -> try {
                editor.putStringSet(key, value as Set<String>)
            } catch (e: ClassCastException) {
                e.printStackTrace()
            }
        }
        editor.apply()
    }

    override fun remove(key: String) {
        editor.remove(key).apply()
    }

    override fun clear() {
        editor.clear().apply()
    }

}