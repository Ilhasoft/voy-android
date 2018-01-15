package br.com.ilhasoft.voy.ui.addreport

import android.support.v4.app.Fragment
import br.com.ilhasoft.support.core.mvp.BasicView
/**
 * Created by lucasbarros on 23/11/17.
 */
interface AddReportContract : BasicView {
    fun displayFragment(fragment: Fragment, tag: String)
    fun navigateBack()
    fun displayThanks()
}