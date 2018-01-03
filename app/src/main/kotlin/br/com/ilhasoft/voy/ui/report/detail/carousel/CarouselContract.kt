package br.com.ilhasoft.voy.ui.report.detail.carousel

import android.net.Uri
import br.com.ilhasoft.support.core.mvp.BasicView
import br.com.ilhasoft.voy.models.Media

interface CarouselContract : BasicView {
    fun displayMedia(media: Media?)
}