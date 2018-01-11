package br.com.ilhasoft.voy.network.reports

/**
 * Created by lucasbarros on 08/01/18.
 */
data class Response<Type>(var count: Int,
                          var next: String?,
                          var previous: String?,
                          var results: List<Type>)