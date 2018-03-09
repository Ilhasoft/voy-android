package br.com.ilhasoft.voy.ui.comment

import br.com.ilhasoft.voy.models.ReportComment
import br.com.ilhasoft.voy.models.User
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.util.*

/**
 * Created by felipe on 08/03/18.
 */
class CommentsUIMapperTest {

    private lateinit var mapper: CommentsUIMapper

    @Before
    fun setUp() {
        mapper = CommentsUIMapper()
    }

    @Test
    fun shouldMapperDataCommentToUICommentUsingUsername() {
        val commentsUI = mapper.apply(createCommentsData(true))
        Assert.assertEquals(commentsUI, createCommentsUI(true))
    }

    @Test
    fun shouldMapperDataCommentToUICommentUsingFullName() {
        val commentsUI = mapper.apply(createCommentsData(false))
        Assert.assertEquals(commentsUI, createCommentsUI(false))
    }

    private fun createCommentsData(isUsername: Boolean): List<ReportComment> = listOf(
        if (isUsername) ReportComment(1, "body", createFakeUser(), createFakeDate(), createFakeDate(), 1)
        else ReportComment(1, "body", createFakeFullUser(), createFakeDate(), createFakeDate(), 1)
    )

    private fun createCommentsUI(isUsername: Boolean): List<CommentUIModel> = listOf(
        if (isUsername) CommentUIModel(1, 1, 1, "username", "body", "Jan 01, 2018", "avatar")
        else CommentUIModel(1, 1, 1, "Jake Wharton", "body", "Jan 01, 2018", "avatar")
    )

    private fun createFakeUser(): User {
        return User(1, "avatar", "username", "email")
    }

    private fun createFakeFullUser(): User {
        return User(1, "Jake", "Wharton", "portuguese", "avatar", "username", "email", true, true, "password")
    }

    private fun createFakeDate(): Date {
        val calendar = Calendar.getInstance()
        calendar.set(2018, 0, 1)
        return calendar.time
    }
}