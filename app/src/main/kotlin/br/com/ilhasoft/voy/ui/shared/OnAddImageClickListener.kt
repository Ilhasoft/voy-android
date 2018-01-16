package br.com.ilhasoft.voy.ui.shared

import android.net.Uri
import android.widget.ImageView
import br.com.ilhasoft.voy.shared.widget.AddImageView

/**
 * Created by lucasbarros on 23/11/17.
 */
interface OnAddImageClickListener {

    fun onClickAddImage(addImageView: AddImageView)
    fun onClickRemove(uri: Uri)

}