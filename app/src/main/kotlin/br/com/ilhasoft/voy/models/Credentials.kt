package br.com.ilhasoft.voy.models

/**
 * Created by geral on 24/11/17.
 */
data class Credentials(var username: String, var password: String) {
    constructor() : this("", "")
}
