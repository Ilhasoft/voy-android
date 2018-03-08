package br.com.ilhasoft.voy.ui.comment

/**
 * Created by felipe on 31/01/18.
 */
data class CommentUIModel(
    val commentId: Int,
    val reportId: Int,
    val userId: Int,
    val createdBy: String,
    val body: String,
    val createdOn: String,
    val avatar: String
)