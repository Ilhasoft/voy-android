package br.com.ilhasoft.voy.network.reports

import br.com.ilhasoft.voy.network.ServiceFactory
import br.com.ilhasoft.voy.shared.extensions.putIfNotNull
import io.reactivex.Flowable

/**
 * Created by lucasbarros on 08/01/18.
 */
class ReportsService : ServiceFactory<ReportsApi>(ReportsApi::class.java) {

    fun getReports(page: Int? = null,
                   page_size: Int? = null,
                   theme: Int? = null,
                   project: Int? = null,
                   mapper: Int? = null,
                   status: Int? = null): Flowable<ReportsListResponse> {

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
}
