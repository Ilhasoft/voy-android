package br.com.ilhasoft.voy.ui.addreport

import br.com.ilhasoft.support.core.mvp.BasicView
import br.com.ilhasoft.voy.models.AddReportFragmentType

/**
 * Created by lucasbarros on 23/11/17.
 */
interface AddReportContract : BasicView {
    fun navigateBack()
    fun navigateToThanks()
    fun navigateToNext(type: AddReportFragmentType)
    fun getVisibleFragmentType(): AddReportFragmentType
}