package br.com.ilhasoft.voy.ui.addreport.theme

import br.com.ilhasoft.support.core.mvp.Presenter
import br.com.ilhasoft.voy.models.Report
import br.com.ilhasoft.voy.models.Tag
import br.com.ilhasoft.voy.models.Theme

class AddThemeFragmentPresenter : Presenter<AddThemeFragmentContract>(AddThemeFragmentContract::class.java) {

    var report: Report? = null
        private set

    fun onClickThemes() {
        view.showThemesDialog()
    }

    fun onClickTheme(theme: Theme?) {
        view.swapTheme(theme)
    }

    fun getSelectedTheme(): Theme? = report?.theme

    fun setSelectedTheme(theme: Theme?) {
        report?.theme = theme
        view.changeActionButtonStatus(true)
    }

    fun setSelectedTag(tag: Tag) {
        report?.tagsList?.apply {
            if (!contains(tag)) {
                add(tag)
            } else {
                remove(tag)
            }
        }
    }

    fun verifyTagSelected(tag: Tag): Boolean? = report?.tagsList?.contains(tag)

    fun setReportReference(report: Report) {
        this.report = report
    }

}