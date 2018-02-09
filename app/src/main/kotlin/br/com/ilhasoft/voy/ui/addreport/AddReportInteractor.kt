package br.com.ilhasoft.voy.ui.addreport

import br.com.ilhasoft.voy.models.Location
import br.com.ilhasoft.voy.models.Report
import io.reactivex.Flowable
import io.reactivex.Single

/**
 * Created by lucasbarros on 09/02/18.
 */
interface AddReportInteractor {

    fun saveReport(theme: Int, location: Location,
                   description: String?,
                   name: String,
                   tags: List<String>,
                   urls: List<String>?): Single<Report>

    fun getTags(themeId: Int): Flowable<MutableList<String>>
}