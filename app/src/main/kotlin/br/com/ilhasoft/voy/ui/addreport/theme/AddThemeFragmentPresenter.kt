package br.com.ilhasoft.voy.ui.addreport.theme

import br.com.ilhasoft.support.core.mvp.Presenter
import br.com.ilhasoft.voy.models.Theme

class AddThemeFragmentPresenter : Presenter<AddThemeFragmentContract>(AddThemeFragmentContract::class.java) {

    private var theme: Theme? = null

    fun onClickThemes() {
        view.showThemesDialog()
    }

    fun onClickTheme(theme: Theme?){
        view.swapTheme(theme)
    }

    fun setSelectedTheme(theme: Theme?){
        this.theme = theme
    }

    fun getSelectedTheme(): Theme? = theme
}