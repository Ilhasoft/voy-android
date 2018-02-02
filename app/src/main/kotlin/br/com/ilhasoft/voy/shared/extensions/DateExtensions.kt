@file:JvmName("DateHelper")

package br.com.ilhasoft.voy.shared.extensions

import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by felipe on 02/02/18.
 */
fun Date?.format(pattern: String): String {
    val formatter = SimpleDateFormat(pattern, Locale.getDefault())
    return this?.let { formatter.format(it) } ?: ""
}