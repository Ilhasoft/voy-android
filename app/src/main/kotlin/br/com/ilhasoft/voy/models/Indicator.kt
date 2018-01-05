package br.com.ilhasoft.voy.models

import android.net.Uri

/**
 * Created by jones on 12/27/17.
 */
data class Indicator(var mediaUri: Uri? = Uri.EMPTY,
                     var selected: Boolean = false,
                     var position: Int = INITIAL_POSITION) {

    companion object {
        val INITIAL_POSITION = 0
    }
}