package br.com.ilhasoft.voy.ui.addreport

import android.os.Bundle
import android.support.v4.app.Fragment
import br.com.ilhasoft.support.core.mvp.Presenter
import br.com.ilhasoft.voy.models.AddReportFragmentType
import br.com.ilhasoft.voy.models.Media
import br.com.ilhasoft.voy.models.Report
import br.com.ilhasoft.voy.ui.addreport.description.AddDescriptionFragment
import br.com.ilhasoft.voy.ui.addreport.medias.AddMediasFragment
import br.com.ilhasoft.voy.ui.addreport.tag.AddThemeFragment

/**
 * Created by lucasbarros on 23/11/17.
 */
class AddReportPresenter : Presenter<AddReportContract>(AddReportContract::class.java) {

    var report = Report()
    private var nextFragmentReference: AddReportFragmentType? = null


    override fun attachView(view: AddReportContract) {
        super.attachView(view)
        startFragmentByReference(AddReportFragmentType.MEDIAS)
    }

    fun updateReportMedias(mediaList: MutableList<Media>) {
        /*report.mediaList = mediaList*/
    }

    fun updateNextFragmentReference(nextFragment: AddReportFragmentType) {
        nextFragmentReference = nextFragment
    }

    fun updateExternalLinksList(externalLinks: MutableList<String>) {
        /*report.links = links*/
    }

    fun onClickNavigateNext() {
        startFragmentByReference(nextFragmentReference)
    }

    fun onClickNavigateBack() {
        view.navigateBack()
    }

    private fun startFragmentByReference(actualFragment: AddReportFragmentType?) {
        actualFragment?.let {
            when (it) {
                AddReportFragmentType.MEDIAS -> startFragment(AddMediasFragment(), AddMediasFragment.TAG, AddReportFragmentType.DESCRIPTION)
                AddReportFragmentType.DESCRIPTION -> startFragment(AddDescriptionFragment(), AddDescriptionFragment.TAG, AddReportFragmentType.THEME)
                AddReportFragmentType.THEME -> startFragment(AddThemeFragment(), AddThemeFragment.TAG, AddReportFragmentType.SEND)
                else -> sendReport()
            }
        }
    }

    private fun sendReport() {
        println(report)
        view.displayThanks()
    }

    private fun startFragment(fragment: Fragment, tag: String, nextFragment: AddReportFragmentType?) {
        val bundle = Bundle()
        bundle.putParcelable(Report.TAG, report)
        fragment.arguments = bundle
        nextFragmentReference = nextFragment
        view.displayFragment(fragment, tag)
    }

}
