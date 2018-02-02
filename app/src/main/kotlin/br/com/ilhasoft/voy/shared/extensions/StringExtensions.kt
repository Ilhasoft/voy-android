package br.com.ilhasoft.voy.shared.extensions

import java.util.regex.Pattern

/**
 * Created by felipe on 02/02/18.
 */
fun String.extractNumbers(): String {
    val pattern = Pattern.compile("\\d+")
    val matcher = pattern.matcher(this)

    return if (matcher.find()) {
        matcher.group()
    } else {
        this
    }
}