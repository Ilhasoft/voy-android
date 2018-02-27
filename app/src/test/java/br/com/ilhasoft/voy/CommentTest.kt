package br.com.ilhasoft.voy

import br.com.ilhasoft.voy.models.Credentials
import br.com.ilhasoft.voy.models.ReportComment
import br.com.ilhasoft.voy.network.BaseFactory
import br.com.ilhasoft.voy.network.authorization.AuthorizationResponse
import br.com.ilhasoft.voy.network.authorization.AuthorizationService
import br.com.ilhasoft.voy.network.comments.CommentsService
import io.reactivex.Flowable
import org.junit.Before
import org.junit.Test

/**
 * Created by developer on 27/02/18.
 */
class CommentTest {

    private lateinit var authService: AuthorizationService
    private lateinit var commentsService: CommentsService

    private val credentials: Credentials = Credentials("pirralho", "123456")
    private val reportId: Int = 205
    private val reportComment: String = "This is a test comment"
    private val commentId: Int = 916

    @Before
    fun setup() {
        authService = AuthorizationService()
        getToken(credentials).subscribe()
        commentsService = CommentsService()
    }

    @Test
    fun tryLoadCommentsWithValidReportId() {
        commentsService.getComments(reportId = reportId)
                .test()
                .assertSubscribed()
                .assertNoErrors()
                .assertComplete()
                .assertValue { comments: List<ReportComment> -> comments.isNotEmpty() }
    }

    @Test
    fun trySubmitNotEmptyComment() {
        commentsService.saveComment(text = reportComment, reportId = reportId)
                .test()
                .assertSubscribed()
                .assertNoErrors()
                .assertComplete()
                .assertValueCount(1)
    }

    @Test
    fun tryDeleteReportComment() {
        commentsService.deleteComment(commentId = commentId)
                .test()
                .assertSubscribed()
                .assertNoErrors()
                .assertComplete()
    }

    private fun getToken(credentials: Credentials): Flowable<AuthorizationResponse> =
            authService.loginWithCredentials(credentials)
                    .doOnNext { BaseFactory.accessToken = it.token }

}