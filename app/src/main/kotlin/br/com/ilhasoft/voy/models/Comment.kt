package br.com.ilhasoft.voy.models

/**
 * Created by developer on 14/12/17.
 */
data class Comment(var user: User, var createdAt: String, var description: String) {
    constructor() : this(User(), "", "")
}