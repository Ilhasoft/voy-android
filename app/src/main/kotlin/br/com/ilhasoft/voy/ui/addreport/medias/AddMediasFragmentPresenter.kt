package br.com.ilhasoft.voy.ui.addreport.medias

import android.net.Uri
import br.com.ilhasoft.support.core.mvp.Presenter
import br.com.ilhasoft.voy.models.Media
import br.com.ilhasoft.voy.models.Report

class AddMediasFragmentPresenter :
        Presenter<AddMediasFragmentContract>(AddMediasFragmentContract::class.java) {

    var report: Report? = null

    fun addMedia(uri: Uri) {
        /*report?.mediaList?.apply {
            add(Media(uri))
            verifyMediaListSize()
        }*/
    }

    fun removeMedia(uri: Uri?) {
        /*report?.mediaList?.apply {
            remove(single { it.uri == uri })
            verifyMediaListSize()
        }*/
    }

    fun verifyMediaListSize() {
        /*report?.mediaList?.let {
            view.changeActionButtonStatus(it.size > 0)
            view.updateReportMedias(it)
        }*/
    }

    fun setReportReference(report: Report) {
        this.report = report
    }

}