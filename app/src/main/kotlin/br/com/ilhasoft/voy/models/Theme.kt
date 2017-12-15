package br.com.ilhasoft.voy.models

/**
 * Created by geral on 13/12/17.
 */
data class Theme(val project: String,
                 val name: String,
                 val description: String,
                 val tags: MutableList<String>,
                 val color: String,
                 val pin: String) {

    constructor() : this( "", "", "", mutableListOf(), "", "")

}