package br.com.ilhasoft.voy.models

import java.util.Date

/**
 * Created by lucasbarros on 01/02/18.
 */
object ThemeData {
    var themeId: Int = 0
    var themeColor: Int = 0
    var themeBounds: List<List<Double>> = arrayListOf()
    var allowLinks: Boolean = false
    var startAt: Date = Date(0L)
    var endAt: Date = Date(0L)
}