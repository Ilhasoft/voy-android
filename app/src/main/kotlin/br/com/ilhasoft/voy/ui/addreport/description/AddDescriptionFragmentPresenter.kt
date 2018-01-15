package br.com.ilhasoft.voy.ui.addreport.description

import br.com.ilhasoft.support.core.mvp.Presenter
import br.com.ilhasoft.voy.models.Report
import br.com.ilhasoft.voy.ui.addreport.ReportViewModel

class AddDescriptionFragmentPresenter(val reportViewModel: ReportViewModel) :
        Presenter<AddDescriptionFragmentContract>(AddDescriptionFragmentContract::class.java) {

    private val LIST_MAX_SIZE = 4

    fun addLink(link: String) {
        /*report?.externalLinks?.let {
            if (!it.contains(link)) {
                it.add(link)
                view.updateAdapterList(it)
            } else {
                view.displaySameListElementFeedback()
            }
        }*/
    }

    fun removeLink(link: String) {
        /*report?.externalLinks?.apply {
            remove(single { it == link })
            view.updateAdapterList(this)
        }*/
    }

    fun verifyListSize(): Boolean =
            /*report!!.externalLinks.size < LIST_MAX_SIZE*/false

}