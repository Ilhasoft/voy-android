package br.com.ilhasoft.voy.ui.addreport

import br.com.ilhasoft.support.core.mvp.Presenter
import br.com.ilhasoft.voy.models.AddReportFragmentType

/**
 * Created by lucasbarros on 23/11/17.
 */
class AddReportPresenter(private val reportViewModel: ReportViewModel) : Presenter<AddReportContract>(AddReportContract::class.java) {

    override fun attachView(view: AddReportContract) {
        super.attachView(view)
        view.navigateToNext(AddReportFragmentType.MEDIAS)
    }

    fun onClickNavigateNext() {
        when (view.getVisibleFragmentType()) {
            AddReportFragmentType.MEDIAS -> view.navigateToNext(AddReportFragmentType.TITLE)
            AddReportFragmentType.TITLE -> view.navigateToNext(AddReportFragmentType.TAG)
            else -> saveReport()
        }
    }

    fun onClickNavigateBack() {
        view.navigateBack()
    }

    private fun saveReport() {
        view.navigateToThanks()
    }

}
