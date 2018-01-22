package br.com.ilhasoft.voy.network.reports

import br.com.ilhasoft.voy.models.Location
import br.com.ilhasoft.voy.models.Report
import br.com.ilhasoft.voy.network.ServiceFactory
import br.com.ilhasoft.voy.shared.extensions.putIfNotNull
import io.reactivex.Flowable
import io.reactivex.Single

/**
 * Created by lucasbarros on 08/01/18.
 */
class ReportService : ServiceFactory<ReportsApi>(ReportsApi::class.java) {

    fun getReports(page: Int? = null,
                   page_size: Int? = null,
                   theme: Int? = null,
                   project: Int? = null,
                   mapper: Int? = null,
                   status: Int? = null): Single<Response<Report>> {

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

    fun getReport(id: Int,
                  theme: Int? = null,
                  project: Int? = null,
                  mapper: Int? = null,
                  status: Int? = null): Single<Report> {

        val reportsRequest = mutableMapOf<String, Int?>()
        reportsRequest.apply {
            putIfNotNull("theme", theme)
            putIfNotNull("project", project)
            putIfNotNull("mapper", mapper)
            putIfNotNull("status", status)
        }
        return api.getReport(id, reportsRequest)
    }

    fun saveReport(theme: Int,
                   location: Location,
                   description: String?,
                   name: String,
                   tags: List<String>,
                   urls: List<String>?): Single<Report> {
        val request = CreateReportRequest(theme, location, description, name, tags, urls)
        return api.saveReport(request)
    }

    //TODO: Refactor updateReport
    /*fun updateReport(id: Int,
                     theme: Int,
                     project: Int? = null,
                     mapper: Int? = null,
                     status: Int? = null,
                     location: Location,
                     newDescription: String?,
                     newName: String): Single<Report> {

        val parameters = mutableMapOf<String, Int?>()
        parameters.apply {
            putIfNotNull("theme", theme)
            putIfNotNull("project", project)
            putIfNotNull("mapper", mapper)
            putIfNotNull("status", status)
        }

        val requestBody = CreateReportRequest(theme, location, newDescription, newName, status)

        return api.updateReport(id, parameters, requestBody)
    }*/

    fun deleteReport(id: Int,
               theme: Int? = null,
               project: Int? = null,
               mapper: Int? = null,
               status: Int? = null): Single<Void> {

        val reportsRequest = mutableMapOf<String, Int?>()
        reportsRequest.apply {
            putIfNotNull("theme", theme)
            putIfNotNull("project", project)
            putIfNotNull("mapper", mapper)
            putIfNotNull("status", status)
        }
        return api.deleteReport(id, reportsRequest)
    }
}
