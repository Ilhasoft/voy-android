package br.com.ilhasoft.voy.ui.addreport.tag

import br.com.ilhasoft.support.core.mvp.BasicView

interface AddThemeFragmentContract : BasicView {

    fun changeActionButtonStatus(status: Boolean)

    fun notifyTagsChange()

}