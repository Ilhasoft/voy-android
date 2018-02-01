package br.com.ilhasoft.voy.ui.comment

import br.com.ilhasoft.voy.models.ReportComment
import br.com.ilhasoft.voy.models.User
import io.reactivex.functions.Function
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by felipe on 31/01/18.
 */
class CommentsUIMapper : Function<List<ReportComment>, List<CommentUIModel>> {

    override fun apply(comments: List<ReportComment>): List<CommentUIModel> {
        if (comments.isEmpty()) return listOf()

        return comments.map {
            CommentUIModel(
                    it.id,
                    it.report,
                    it.createdBy.id,
                    userName(it.createdBy),
                    it.text,
                    formatCreatedOnDate(it.createdOn),
                    it.createdBy.avatar
            )
        }
    }

    private fun formatCreatedOnDate(createdOn: Date): String {
        val formatter = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
        return formatter.format(createdOn)
    }

    private fun userName(createdBy: User): String {
        val name = "${createdBy.firstName} ${createdBy.lastName}"
        return if (name.isNotEmpty() && name.isNotBlank()) name else createdBy.username
    }
}
