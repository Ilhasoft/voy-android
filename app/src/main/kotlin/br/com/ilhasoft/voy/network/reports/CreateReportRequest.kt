package br.com.ilhasoft.voy.network.reports

import br.com.ilhasoft.voy.models.Location

/**
 * Created by lucasbarros on 09/01/18.
 */
data class CreateReportRequest(
        var theme: Int,
        var location: Location,
        var description: String?,
        var name: String,
        var status: Int?
)