package br.com.ilhasoft.voy.shared.helpers

import android.content.Context
import br.com.ilhasoft.voy.R

/**
 * Created by erickjones on 23/02/18.
 */
object ResourcesHelper {

    fun getAvatarsResources(context: Context): List<Int> {
        val avatars = context.resources.obtainTypedArray(R.array.avatars)
        val resources = (0 until avatars.length()).map { avatars.getResourceId(it, 0) }
        avatars.recycle()
        return resources
    }

}