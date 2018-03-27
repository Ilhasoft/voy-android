package br.com.ilhasoft.voy.ui.report.holder

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.ThumbnailUtils
import android.provider.MediaStore
import br.com.ilhasoft.support.recyclerview.adapters.ViewHolder
import br.com.ilhasoft.voy.GlideApp
import br.com.ilhasoft.voy.databinding.ItemReportBinding
import br.com.ilhasoft.voy.models.Report
import br.com.ilhasoft.voy.models.ReportFile
import br.com.ilhasoft.voy.models.ThemeData
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

    init {
        binding.presenter = presenter
    }

    override fun onBind(report: Report) {
        binding.let {
            it.themeIndicator.setBackgroundColor(ThemeData.themeColor)
            it.pendingToSend = (report.status == ReportStatus.PENDING.value) && report.shouldSend
            it.report = report
            it.executePendingBindings()

            val lastMedia: ReportFile? = try { report.files.last() } catch (e: NoSuchElementException) { null }
            if (lastMedia != null && !lastMedia.file.contains(Regex("^http"))) {
                // TODO: improve setting thumbnail image
                GlideApp.with(it.image)
                        .load(getOfflineReportThumbnail(lastMedia))
                        .centerCrop()
                        .into(it.image)
            }
        }
    }

    private fun getOfflineReportThumbnail(reportFile: ReportFile): Bitmap? {
        val file = File(reportFile.file)
        return when {
            !file.exists() -> null
            reportFile.mediaType == ReportFile.TYPE_IMAGE -> BitmapFactory.decodeFile(file.absolutePath)
            else -> ThumbnailUtils.createVideoThumbnail(file.absolutePath, MediaStore.Images.Thumbnails.MINI_KIND)
        }
    }
}