package br.com.ilhasoft.voy.models

/**
 * Created by geral on 05/12/17.
 */
data class Notification(val title: String, val comments: MutableList<Any>?) {
    constructor() : this("", null)
}