package br.com.ilhasoft.voy.comments

import android.accounts.NetworkErrorException
import br.com.ilhasoft.voy.models.Preferences
import br.com.ilhasoft.voy.models.ReportComment
import br.com.ilhasoft.voy.models.User
import br.com.ilhasoft.voy.network.comments.CommentDataSource
import br.com.ilhasoft.voy.network.comments.CommentRepository
import br.com.ilhasoft.voy.ui.comment.CommentUIModel
import br.com.ilhasoft.voy.ui.comment.CommentsContract
import br.com.ilhasoft.voy.ui.comment.CommentsPresenter
import br.com.ilhasoft.voy.ui.comment.CommentsUIMapper
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import java.net.UnknownHostException
import java.util.*

/**
 * Created by developer on 01/03/18.
 */
class CommentPresenterTest {

    @Mock
    private lateinit var commentDataSource: CommentDataSource

    @Mock
    private lateinit var preferences: Preferences

    @Mock
    private lateinit var commentsContract: CommentsContract
    private lateinit var commentsPresenter: CommentsPresenter

    private val mockedCommentId = 101
    private val mockedCommentText: String = "This is a test comment"
    private val mockedUser = User(1, "2", "mockeduser", "mockuser@test.test")
    private val mockedValidReportId = 100

    private val mockedComment = ReportComment(mockedCommentId, mockedCommentText, mockedUser,
            Date(), Date(), mockedValidReportId)

    private val mockedCommentUiModel = CommentUIModel(mockedCommentId, mockedValidReportId,
            1, "", mockedCommentText, "", "")

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        commentsPresenter = CommentsPresenter(CommentRepository(commentDataSource),
                CommentsUIMapper(), preferences)
    }

    @Test
    fun loadEmptyCommentsList() {
        `when`(commentDataSource.getComments(mockedValidReportId))
                .thenReturn(Flowable.just(listOf()))

        commentsPresenter.loadComments(mockedValidReportId)

        verify(commentsContract, atLeastOnce()).showEmptyView()
    }

    @Test
    fun submitComment() {
        `when`(commentDataSource.saveComment(mockedCommentText, mockedValidReportId))
                .thenReturn(Maybe.just(mockedComment))

        commentsPresenter.onClickSendComment(mockedCommentText, mockedValidReportId)

        verify(commentsContract, atLeastOnce()).isValidCommentBodyState()
        verify(commentsContract, atLeastOnce()).commentCreated()
    }

    @Test
    fun deleteComment() {
        `when`(commentDataSource.deleteComment(mockedCommentId))
                .thenReturn(Completable.complete())

        commentsPresenter.onClickRemoveComment(mockedCommentUiModel)

        verify(commentsContract, atLeastOnce()).navigateToRemoveComment(mockedCommentUiModel)
    }

    @Test
    fun deleteCommentUnknownHost() {
        `when`(commentDataSource.deleteComment(mockedCommentId))
                .thenReturn(emitCompletableError(UnknownHostException()))

        commentsPresenter.onClickRemoveComment(mockedCommentUiModel)

        verify(commentsContract, atLeastOnce()).navigateToRemoveComment(mockedCommentUiModel)
    }

    @Test
    fun deleteCommentNetworkError() {
        `when`(commentDataSource.deleteComment(mockedCommentId))
                .thenReturn(emitCompletableError(NetworkErrorException()))

        commentsPresenter.onClickRemoveComment(mockedCommentUiModel)

        verify(commentsContract, atLeastOnce()).navigateToRemoveComment(mockedCommentUiModel)
    }

    private fun emitCompletableError(throwable: Throwable): Completable =
            Completable.error(throwable)

}