package br.com.ilhasoft.voy.ui.report.holder

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.ThumbnailUtils
import android.provider.MediaStore
import android.support.v4.content.ContextCompat
import android.util.Log
import android.webkit.URLUtil
import android.widget.PopupMenu
import br.com.ilhasoft.support.recyclerview.adapters.ViewHolder
import br.com.ilhasoft.voy.R
import br.com.ilhasoft.voy.databinding.ItemReportBinding
import br.com.ilhasoft.voy.models.Report
import br.com.ilhasoft.voy.models.ReportFile
import br.com.ilhasoft.voy.models.ThemeData
import br.com.ilhasoft.voy.shared.binding.ImageViewBindings
import br.com.ilhasoft.voy.ui.report.ReportStatus
import br.com.ilhasoft.voy.ui.report.fragment.ReportPresenter
import java.io.File

/**
 * Created by developer on 01/12/17.
 */
class ReportViewHolder(
    val binding: ItemReportBinding,
    val presenter: ReportPresenter
) : ViewHolder<Report>(binding.root) {

    private lateinit var popupMenu: PopupMenu

    init {
        binding.presenter = presenter
    }

    override fun onBind(report: Report) {
        binding.let {
            it.themeIndicator.setBackgroundColor(ThemeData.themeColor)
            it.pendingToSend = (report.status == ReportStatus.PENDING.value) && report.shouldSend
            it.report = report
            it.executePendingBindings()

            val lastMedia = report.files.last()
            if (!lastMedia.file.contains("http")) {
                ImageViewBindings.loadFromBitmap(
                        it.image,
                        getOfflineReportThumbnail(lastMedia),
                        R.drawable.shape_rounded_grey
                )
                it.executePendingBindings()
            }
        }
    }

    private fun getOfflineReportThumbnail(reportFile: ReportFile): Bitmap? {
        val file = File(reportFile.file)
        var thumbnail: Bitmap? = null

        if (file.exists()) {
            when {
                reportFile.mediaType == ReportFile.TYPE_IMAGE -> thumbnail = BitmapFactory.decodeFile(file.absolutePath)
                reportFile.mediaType == ReportFile.TYPE_VIDEO -> thumbnail = ThumbnailUtils.createVideoThumbnail(file.absolutePath, MediaStore.Images.Thumbnails.MICRO_KIND)
            }
        }

        return thumbnail
    }
}