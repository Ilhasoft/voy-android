package br.com.ilhasoft.voy.network.files

import br.com.ilhasoft.voy.models.ReportFile
import br.com.ilhasoft.voy.network.ServiceFactory
import br.com.ilhasoft.voy.network.reports.Response
import br.com.ilhasoft.voy.shared.extensions.putIfNotNull
import io.reactivex.Single

/**
 * Created by lucasbarros on 11/01/18.
 */
class FilesService : ServiceFactory<FilesApi>(FilesApi::class.java) {

    fun getFiles(page: Int? = null,
                 page_size: Int? = null,
                 theme: Int? = null,
                 report: Int? = null,
                 project: Int? = null,
                 media_type: String? = null): Single<Response<ReportFile>> {

        val request = mutableMapOf<String, Any?>()
        request.apply {
            putIfNotNull("page", page)
            putIfNotNull("page_size", page_size)
            putIfNotNull("theme", theme)
            putIfNotNull("report", report)
            putIfNotNull("project", project)
            putIfNotNull("media_type", media_type)
        }

        return api.getFiles(request)
    }

}