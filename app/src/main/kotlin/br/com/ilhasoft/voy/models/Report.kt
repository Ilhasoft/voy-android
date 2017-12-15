package br.com.ilhasoft.voy.models

/**
 * Created by developer on 01/12/17.
 */
data class Report(var title: String, var createdAt: String, var description: String) {
    constructor() : this("", "", "")
}