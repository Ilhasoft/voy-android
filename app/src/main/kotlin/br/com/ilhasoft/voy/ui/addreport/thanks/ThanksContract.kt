package br.com.ilhasoft.voy.ui.addreport.thanks

import br.com.ilhasoft.support.core.mvp.BasicView

interface ThanksContract : BasicView {
    fun navigateToAddReport()
    fun onClickClose()
}