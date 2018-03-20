package br.com.ilhasoft.voy.ui.report


import br.com.ilhasoft.support.core.mvp.Presenter
import br.com.ilhasoft.voy.R
import br.com.ilhasoft.voy.models.ThemeData
import java.util.*
/**
 * Created by developer on 11/01/18.
 */
class ReportsPresenter : Presenter<ReportsContract>(ReportsContract::class.java) {

    fun onClickNavigateBack() {
        view.navigateBack()
    }

    fun onClickAddReport(currentTime: Date) {
        when  {
            currentTime in ThemeData.startAt..ThemeData.endAt -> view.navigateToAddReport()
            ThemeData.startAt > currentTime -> view.showMessage(R.string.period_future_text)
            else -> view.showMessage(R.string.period_ended_text)
        }
    }
}