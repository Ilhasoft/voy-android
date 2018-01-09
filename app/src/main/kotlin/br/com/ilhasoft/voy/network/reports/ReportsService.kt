package br.com.ilhasoft.voy.network.reports

import br.com.ilhasoft.voy.models.Location
import br.com.ilhasoft.voy.models.Report
import br.com.ilhasoft.voy.network.ServiceFactory
import br.com.ilhasoft.voy.shared.extensions.putIfNotNull
import io.reactivex.Single

/**
 * Created by lucasbarros on 08/01/18.
 */
class ReportsService : ServiceFactory<ReportsApi>(ReportsApi::class.java) {

    fun getReports(page: Int? = null,
                   page_size: Int? = null,
                   theme: Int? = null,
                   project: Int? = null,
                   mapper: Int? = null,
                   status: Int? = null): Single<ReportsListResponse> {

        val reportsRequest = mutableMapOf<String, Int?>()
        reportsRequest.apply {
            putIfNotNull("page", page)
            putIfNotNull("page_size", page_size)
            putIfNotNull("theme", theme)
            putIfNotNull("project", project)
            putIfNotNull("mapper", mapper)
            putIfNotNull("status", status)
        }
        return api.getReports(reportsRequest)
    }

    fun createReport(theme: Int,
                     location: Location,
                     description: String?,
                     name: String,
                     status: Int?): Single<Report> {
        val request = CreateReportRequest(theme, location, description, name, status)
        return api.createReport(request)
    }
}
