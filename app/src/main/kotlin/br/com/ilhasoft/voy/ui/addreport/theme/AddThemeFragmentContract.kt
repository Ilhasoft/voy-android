package br.com.ilhasoft.voy.ui.addreport.theme

import br.com.ilhasoft.support.core.mvp.BasicView
import br.com.ilhasoft.voy.models.Theme

interface AddThemeFragmentContract : BasicView {
    fun showThemesDialog()
    fun swapTheme(theme: Theme?)
    fun changeActionButtonStatus(status: Boolean)
    fun notifyTagsListChange()
}