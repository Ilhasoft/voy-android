package br.com.ilhasoft.voy.network.reports

import br.com.ilhasoft.voy.models.Report

/**
 * Created by lucasbarros on 08/01/18.
 */
data class ReportsListResponse(var count: Int,
                               var next: String?,
                               var previous: String?,
                               var results: List<Report>)