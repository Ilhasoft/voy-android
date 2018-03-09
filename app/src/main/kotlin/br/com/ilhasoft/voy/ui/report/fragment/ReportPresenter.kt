package br.com.ilhasoft.voy.ui.report.fragment

import br.com.ilhasoft.support.core.mvp.Presenter
import br.com.ilhasoft.voy.models.Preferences
import br.com.ilhasoft.voy.models.Report
import br.com.ilhasoft.voy.models.User
import br.com.ilhasoft.voy.shared.extensions.extractNumbers

class ReportPresenter(private val preferences: Preferences) : Presenter<ReportContract>(ReportContract::class.java) {

    fun navigateToReportDetail(report: Report) {
        view.navigateToReportDetail(report)
    }

    fun getAvatarPositionFromPreferences(): Int =
            preferences.getString(User.AVATAR).extractNumbers().toInt().minus(1)
}