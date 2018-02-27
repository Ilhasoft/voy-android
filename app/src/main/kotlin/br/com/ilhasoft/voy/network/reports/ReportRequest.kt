package br.com.ilhasoft.voy.network.reports

import br.com.ilhasoft.voy.models.Location

/**
 * Created by lucasbarros on 09/01/18.
 */
data class ReportRequest(
        var theme: Int,
        var location: Location,
        var description: String?,
        var name: String,
        var tags: List<String>,
        var urls: List<String>? = null
)