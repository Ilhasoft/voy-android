package br.com.ilhasoft.voy.ui.addreport

import android.os.Bundle
import android.support.v4.app.Fragment
import br.com.ilhasoft.support.core.mvp.Presenter
import br.com.ilhasoft.voy.models.Fragments
import br.com.ilhasoft.voy.models.Media
import br.com.ilhasoft.voy.models.Report
import br.com.ilhasoft.voy.ui.addreport.description.AddDescriptionFragment
import br.com.ilhasoft.voy.ui.addreport.medias.AddMediasFragment
import br.com.ilhasoft.voy.ui.addreport.theme.AddThemeFragment

/**
 * Created by lucasbarros on 23/11/17.
 */
class AddReportPresenter : Presenter<AddReportContract>(AddReportContract::class.java) {

    var report = Report()
    private var nextFragmentReference: Fragments? = null

    fun startFragmentByReference(actualFragment: Fragments?) {
        actualFragment?.let {
            when (it) {
                Fragments.MEDIAS -> startFragment(AddMediasFragment(), AddMediasFragment.TAG, Fragments.DESCRIPTION)
                Fragments.DESCRIPTION -> startFragment(AddDescriptionFragment(), AddDescriptionFragment.TAG, Fragments.THEME)
                Fragments.THEME -> startFragment(AddThemeFragment(), AddThemeFragment.TAG, Fragments.SEND)
                else -> sendReport()
            }
        }
    }

    private fun sendReport() {
        println("SEND REPORT!")
        println(report)
    }

    private fun startFragment(fragment: Fragment, tag: String, nextFragment: Fragments?) {
        val bundle = Bundle()
        bundle.putParcelable(Report.TAG, report)
        fragment.arguments = bundle
        nextFragmentReference = nextFragment
        view.displayFragment(fragment, tag)
    }

    fun updateReportMedias(mediaList: MutableList<Media>) {
        report.mediaList = mediaList
    }

    fun updateNextFragmentReference(nextFragment: Fragments) {
        nextFragmentReference = nextFragment
    }

    fun updateExternalLinksList(externalLinks: MutableList<String>) {
        report.externalLinks = externalLinks
    }

    fun onClickNavigateNext() {
        startFragmentByReference(nextFragmentReference)
    }

    fun onClickNavigateBack() {
        view.navigateBack()
    }
}