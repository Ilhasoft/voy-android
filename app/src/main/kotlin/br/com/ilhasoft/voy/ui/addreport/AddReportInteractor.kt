package br.com.ilhasoft.voy.ui.addreport

import io.reactivex.Flowable

/**
 * Created by lucasbarros on 09/02/18.
 */
interface AddReportInteractor {
    fun getTags(themeId: Int): Flowable<MutableList<String>>
}