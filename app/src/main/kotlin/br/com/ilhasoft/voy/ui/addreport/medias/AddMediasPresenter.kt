package br.com.ilhasoft.voy.ui.addreport.medias


import android.widget.ImageView
import br.com.ilhasoft.support.core.mvp.Presenter
import br.com.ilhasoft.voy.ui.shared.OnAddImageClickListener

/**
 * Created by lucasbarros on 23/11/17.
 */
class AddMediasPresenter : Presenter<AddMediasContract>(AddMediasContract::class.java),
        OnAddImageClickListener {
    override fun onClickAddImage(imageView: ImageView) {
    }

    override fun onClickRemove(imageView: ImageView) {
    }


}