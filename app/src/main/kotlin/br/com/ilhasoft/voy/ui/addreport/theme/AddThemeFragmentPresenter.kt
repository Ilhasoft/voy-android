package br.com.ilhasoft.voy.ui.addreport.theme

import br.com.ilhasoft.support.core.mvp.Presenter
import br.com.ilhasoft.voy.models.Report
import br.com.ilhasoft.voy.models.Tag
import br.com.ilhasoft.voy.models.Theme

class AddThemeFragmentPresenter : Presenter<AddThemeFragmentContract>(AddThemeFragmentContract::class.java) {

    var report: Report? = null
        private set

    private var theme: Theme? = null

    fun onClickThemes() {
        view.showThemesDialog()
    }

    fun onClickTheme(theme: Theme?) {
        view.swapTheme(theme)
    }

    fun setSelectedTheme(theme: Theme?) {
        this.theme = theme
        view.changeActionButtonStatus(true)
    }

    fun getSelectedTheme(): Theme? = theme


    fun setReportReference(report: Report) {
        this.report = report
    }

    fun setSelectedTag(tag: Tag?) {
        tag?.let {
            this.report?.tagsList?.apply {
                val empty = filter { it -> it == tag }.isEmpty()
                if (empty) {
                    add(it)
                } else {
                    remove(it)
                }
            }
        }
    }

    fun verifyTagSelected(tag: Tag?): Boolean? = report?.tagsList?.contains(tag)


}