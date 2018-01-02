package br.com.ilhasoft.voy.ui.addreport.medias

import br.com.ilhasoft.support.core.mvp.BasicView
import br.com.ilhasoft.voy.models.Media

interface AddMediasFragmentContract : BasicView {
    fun getMedia()
    fun changeActionButtonStatus(status: Boolean)
    fun updateReportMedias(mediaList: MutableList<Media>)
}