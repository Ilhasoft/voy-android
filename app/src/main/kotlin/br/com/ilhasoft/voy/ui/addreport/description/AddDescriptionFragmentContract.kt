package br.com.ilhasoft.voy.ui.addreport.description

import br.com.ilhasoft.support.core.mvp.BasicView

interface AddDescriptionFragmentContract : BasicView {
    fun changeActionButtonStatus(status: Boolean)
    fun updateReportExternalLinksList(externalLinks: MutableList<String>)
    fun updateAdapterList(externalLinks: MutableList<String>)
    fun displaySameListElementFeedback()
}