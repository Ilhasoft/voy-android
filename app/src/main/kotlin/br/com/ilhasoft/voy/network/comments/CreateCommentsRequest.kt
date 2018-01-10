package br.com.ilhasoft.voy.network.comments

import br.com.ilhasoft.voy.models.User

/**
 * Created by lucasbarros on 10/01/18.
 */
data class CreateCommentsRequest(var text: String,
                                 var create_by: User?,
                                 var report: Int)