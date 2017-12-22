package br.com.ilhasoft.voy.ui.shared

import br.com.ilhasoft.voy.models.Fragments
import br.com.ilhasoft.voy.models.Media
/**
 * Created by geral on 18/12/17.
 */
interface OnReportChangeListener {
    fun changeActionButtonStatus(status: Boolean)
    fun updateReportMedias(mediaList: MutableList<Media>)
    fun updateNextFragmentReference(nextFragment: Fragments)
    fun updateExternalLinksList(externalLinks: MutableList<String>)
    fun changeActionButtonName(resourceId: Int)
}