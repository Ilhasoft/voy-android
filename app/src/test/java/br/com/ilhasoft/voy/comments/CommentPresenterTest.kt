package br.com.ilhasoft.voy.comments

import android.accounts.NetworkErrorException
import br.com.ilhasoft.voy.R
import br.com.ilhasoft.voy.extensions.emitCompletableError
import br.com.ilhasoft.voy.extensions.emitFlowableError
import br.com.ilhasoft.voy.extensions.emitMaybeError
import br.com.ilhasoft.voy.models.Preferences
import br.com.ilhasoft.voy.models.ReportComment
import br.com.ilhasoft.voy.models.User
import br.com.ilhasoft.voy.network.comments.CommentDataSource
import br.com.ilhasoft.voy.network.comments.CommentRepository
import br.com.ilhasoft.voy.shared.schedulers.ImmediateScheduler
import br.com.ilhasoft.voy.ui.comment.CommentUIModel
import br.com.ilhasoft.voy.ui.comment.CommentsContract
import br.com.ilhasoft.voy.ui.comment.CommentsPresenter
import br.com.ilhasoft.voy.ui.comment.CommentsUIMapper
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import junit.framework.Assert.assertEquals
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import java.net.UnknownHostException
import java.util.*
import java.util.concurrent.TimeoutException

/**
 * Created by developer on 01/03/18.
 */
class CommentPresenterTest {

    @Mock
    private lateinit var dataSource: CommentDataSource

    @Mock
    private lateinit var preferences: Preferences

    @Mock
    private lateinit var view: CommentsContract
    private lateinit var presenter: CommentsPresenter

    private val mockedCommentId = 101
    private val mockedCommentText: String = "This is a test comment"
    private val mockedUser = User(1, "2", "mockeduser", "mockuser@test.test")
    private val mockedValidReportId = 205

    private val mockedComment = ReportComment(mockedCommentId, mockedCommentText, mockedUser,
            Date(), Date(), mockedValidReportId)

    private val mockedCommentUiModel = CommentUIModel(mockedCommentId, mockedValidReportId,
            1, "mockeduser", mockedCommentText, "", "")

    private val mockedCommentsUiModel = listOf(mockedCommentUiModel, mockedCommentUiModel)

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        presenter = CommentsPresenter(
                CommentRepository(dataSource),
                CommentsUIMapper(),
                preferences,
                ImmediateScheduler()
        )
        presenter.attachView(view)
    }

    @After
    fun finishing() {
        presenter.detachView()
    }


    @Test
    fun shouldBackWhenOnBackPressed() {
        presenter.onClickNavigateBack()

        verify(view).navigateBack()
    }


    @Test
    fun shouldShowEmptyListMessageWhenLoadCommentsList() {
        `when`(getCommentsRef(mockedValidReportId))
                .thenReturn(Flowable.just(listOf()))

        presenter.loadComments(mockedValidReportId)

        verify(view, times(1)).showLoading()
        verify(view, times(1)).showEmptyView()
        verify(view, times(1)).dismissLoading()
    }


    @Test
    fun shouldShowErrorMessageWhenLoadCommentsTimeout() {
        `when`(getCommentsRef(mockedValidReportId))
                .thenReturn(emitFlowableError(TimeoutException()))

        presenter.loadComments(mockedValidReportId)

        verify(view, times(1)).showLoading()
        verify(view, times(1)).showMessage(0)
        verify(view, times(1)).dismissLoading()
    }

    @Test
    fun shouldShowErrorMessageWhenLoadCommentsUnknownHost() {
        `when`(getCommentsRef(mockedValidReportId))
                .thenReturn(emitFlowableError(UnknownHostException()))

        presenter.loadComments(mockedValidReportId)

        verify(view, times(1)).showLoading()
        verify(view, times(1)).showMessage(R.string.login_network_error)
        verify(view, times(1)).dismissLoading()
    }


    @Test
    fun shouldShowErrorMessageWhenLoadCommentsNetworkError() {
        `when`(getCommentsRef(mockedValidReportId))
                .thenReturn(emitFlowableError(NetworkErrorException()))

        presenter.loadComments(mockedValidReportId)

        verify(view, times(1)).showLoading()
        verify(view, times(1)).showMessage(R.string.login_network_error)
        verify(view, times(1)).dismissLoading()
    }

    @Test
    fun shouldSubmitCommentWhenValidBody() {
        `when`(getSaveCommentRef(mockedCommentText, mockedValidReportId))
                .thenReturn(Maybe.just(mockedComment))

        `when`(view.isValidCommentBodyState()).thenReturn(true)

        presenter.onClickSendComment(mockedCommentText, mockedValidReportId)

        verify(view, times(1)).showLoading()
        verify(view, times(1)).isValidCommentBodyState()
        verify(view, times(1)).commentCreated()
        verify(view, times(1)).dismissLoading()
    }

    @Test
    fun shouldShowErrorMessageWhenSubmitCommentTimeout() {
        `when`(getSaveCommentRef(mockedCommentText, mockedValidReportId))
                .thenReturn(emitMaybeError(TimeoutException()))

        `when`(view.isValidCommentBodyState()).thenReturn(true)

        presenter.onClickSendComment(mockedCommentText, mockedValidReportId)

        verify(view, times(1)).showLoading()
        verify(view, times(1)).isValidCommentBodyState()
        verify(view, times(1)).showMessage(R.string.comment_create_error)
        verify(view, times(1)).dismissLoading()
    }

    @Test
    fun shouldShowErrorMessageWhenSubmitCommentUnknownHost() {
        `when`(getSaveCommentRef(mockedCommentText, mockedValidReportId))
                .thenReturn(emitMaybeError(UnknownHostException()))

        `when`(view.isValidCommentBodyState()).thenReturn(true)

        presenter.onClickSendComment(mockedCommentText, mockedValidReportId)

        verify(view, times(1)).showLoading()
        verify(view, times(1)).isValidCommentBodyState()
        verify(view, times(1)).showMessage(R.string.login_network_error)
        verify(view, times(1)).dismissLoading()
    }

    @Test
    fun shouldShowErrorMessageWhenSubmitCommentNetworkError() {
        `when`(getSaveCommentRef(mockedCommentText, mockedValidReportId))
                .thenReturn(emitMaybeError(NetworkErrorException()))

        `when`(view.isValidCommentBodyState()).thenReturn(true)

        presenter.onClickSendComment(mockedCommentText, mockedValidReportId)

        verify(view, times(1)).showLoading()
        verify(view, times(1)).isValidCommentBodyState()
        verify(view, times(1)).showMessage(R.string.login_network_error)
        verify(view, times(1)).dismissLoading()
    }

    @Test
    fun shouldDeleteCommentWhenSuccess() {
        `when`(getDeleteCommentRef(mockedCommentId, mockedValidReportId))
                .thenReturn(Completable.complete())

        presenter.onClickRemoveComment(mockedCommentUiModel)

        verify(view, times(1)).showLoading()
        verify(view, times(1)).navigateToRemoveComment(mockedCommentUiModel)
        verify(view, times(1)).dismissLoading()
    }

    @Test
    fun shouldShowErrorMessageWhenDeleteCommentTimeout() {
        `when`(getDeleteCommentRef(mockedCommentId, mockedValidReportId))
                .thenReturn(emitCompletableError(TimeoutException()))

        presenter.onClickRemoveComment(mockedCommentUiModel)

        verify(view, times(1)).showLoading()
        verify(view, times(1)).showMessage(R.string.comment_delete_error)
        verify(view, times(1)).dismissLoading()
    }

    @Test
    fun shouldShowErrorMessageWhenDeleteCommentUnknownHost() {
        `when`(getDeleteCommentRef(mockedCommentId, mockedValidReportId))
                .thenReturn(emitCompletableError(UnknownHostException()))

        presenter.onClickRemoveComment(mockedCommentUiModel)

        verify(view, times(1)).showLoading()
        verify(view, times(1)).showMessage(R.string.login_network_error)
        verify(view, times(1)).dismissLoading()
    }

    @Test
    fun shouldShowErrorMessageWhenDeleteCommentNetworkError() {
        `when`(getDeleteCommentRef(mockedCommentId, mockedValidReportId))
                .thenReturn(emitCompletableError(NetworkErrorException()))

        presenter.onClickRemoveComment(mockedCommentUiModel)

        verify(view, times(1)).showLoading()
        verify(view, times(1)).showMessage(R.string.login_network_error)
        verify(view, times(1)).dismissLoading()
    }

    @Test
    fun shouldAssertThatIdIsMine() {
        `when`(preferences.contains(User.ID)).thenReturn(true)
        `when`(preferences.getInt(User.ID)).thenReturn(1)

        val response = presenter.isMyID(1)

        assertEquals(response, true)
    }

    @Test
    fun shouldAssertThatIdIsNotMine() {
        `when`(preferences.contains(User.ID)).thenReturn(false)

        val response = presenter.isMyID(2)

        assertEquals(response, false)
    }

    private fun getCommentsRef(reportId: Int) = dataSource.getComments(reportId)

    private fun getSaveCommentRef(comment: String, reportId: Int) =
            dataSource.saveComment(comment, reportId)

    private fun getDeleteCommentRef(commentId: Int, reportId: Int?) =
            dataSource.deleteComment(commentId, reportId)

}