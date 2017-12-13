package br.com.ilhasoft.voy.models

/**
 * Created by geral on 13/12/17.
 */
data class Theme(val id: Int,
                 val project: String,
                 val name: String,
                 val description: String,
                 val tags: MutableList<String>,
                 val color: String,
                 val pin: String) {

    constructor() : this(0, "", "", "", mutableListOf(), "", "")

}