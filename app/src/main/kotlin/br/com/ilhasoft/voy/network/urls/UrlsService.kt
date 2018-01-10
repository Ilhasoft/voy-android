package br.com.ilhasoft.voy.network.urls

import br.com.ilhasoft.voy.models.ReportUrl
import br.com.ilhasoft.voy.network.ServiceFactory
import io.reactivex.Flowable

/**
 * Created by lucasbarros on 10/01/18.
 */
class UrlsService : ServiceFactory<UrlsApi>(UrlsApi::class.java) {

    fun getUrls(reportId: Int): Flowable<List<ReportUrl>> = api.getUrls(reportId)

}