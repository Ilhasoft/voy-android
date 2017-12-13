package br.com.ilhasoft.voy.ui.addreport.theme

import br.com.ilhasoft.support.core.mvp.Presenter

class AddThemeFragmentPresenter : Presenter<AddThemeFragmentContract>(AddThemeFragmentContract::class.java) {

    fun onClickThemes() {
        view.showThemesDialog()
    }
}