package br.com.ilhasoft.voy.ui.addreport.medias

import android.net.Uri
import br.com.ilhasoft.support.core.mvp.BasicView
import br.com.ilhasoft.support.core.mvp.Presenter
import br.com.ilhasoft.voy.ui.addreport.ReportViewModel

class AddMediasFragmentPresenter(private val reportViewModel: ReportViewModel) :
        Presenter<BasicView>(BasicView::class.java) {

    fun addMedia(uri: Uri) {
        reportViewModel.addUri(uri)
    }

    fun removeMedia(uri: Uri?) {
        uri?.let {
            reportViewModel.removeUri(it)
        }
    }
}