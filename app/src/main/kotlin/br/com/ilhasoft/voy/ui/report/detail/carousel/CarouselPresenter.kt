package br.com.ilhasoft.voy.ui.report.detail.carousel

import br.com.ilhasoft.support.core.mvp.Presenter
import br.com.ilhasoft.voy.models.Media

class CarouselPresenter : Presenter<CarouselContract>(CarouselContract::class.java){

    fun displayMedia(media: Media?){
        view.displayMedia(media)
    }


}