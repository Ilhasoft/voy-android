package br.com.ilhasoft.voy.network.medias

import br.com.ilhasoft.voy.models.ReportMedia
import br.com.ilhasoft.voy.network.ServiceFactory
import io.reactivex.Flowable

/**
 * Created by lucasbarros on 11/01/18.
 */
class MediasService : ServiceFactory<MediasApi>(MediasApi::class.java) {

    fun getMedias(reportId: Int): Flowable<List<ReportMedia>> = api.getMedias(reportId)

}