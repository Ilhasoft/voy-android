package br.com.ilhasoft.voy.ui.report

import br.com.ilhasoft.support.core.mvp.BasicView

/**
 * Created by developer on 11/01/18.
 */
interface ReportsContract : BasicView {

    fun navigateBack()

    fun navigateToAddReport()
}