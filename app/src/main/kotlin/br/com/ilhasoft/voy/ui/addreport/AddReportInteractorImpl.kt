package br.com.ilhasoft.voy.ui.addreport

import br.com.ilhasoft.voy.db.theme.ThemeDbHelper
import br.com.ilhasoft.voy.shared.extensions.onMainThread
import io.reactivex.Flowable

/**
 * Created by lucasbarros on 09/02/18.
 */
class AddReportInteractorImpl : AddReportInteractor {

    private val themeDbHelper by lazy { ThemeDbHelper() }

    override fun getTags(themeId: Int): Flowable<MutableList<String>> {
        return themeDbHelper.getThemeTags(themeId).onMainThread()
    }
}