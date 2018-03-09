package br.com.ilhasoft.voy.ui.comment

import br.com.ilhasoft.voy.models.ReportComment
import br.com.ilhasoft.voy.models.User
import br.com.ilhasoft.voy.shared.extensions.format
import io.reactivex.functions.Function

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
                it.createdOn.format("MMM dd, yyyy"),
                it.createdBy.avatar
            )
        }
    }

    private fun userName(createdBy: User): String {
        val name = "${createdBy.firstName} ${createdBy.lastName}"
        return if (name.isNotEmpty() && name.isNotBlank()) name else createdBy.username
    }
}
