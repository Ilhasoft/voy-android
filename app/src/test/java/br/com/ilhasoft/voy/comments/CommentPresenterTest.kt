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
        presenter = CommentsPresenter(CommentRepository(dataSource),
                CommentsUIMapper(), preferences, ImmediateScheduler())
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
    fun loadEmptyCommentsList() {
        `when`(dataSource.getComments(mockedValidReportId))
                .thenReturn(Flowable.just(listOf()))

        presenter.loadComments(mockedValidReportId)

        verify(view, atLeastOnce()).showLoading()
        verify(view, atLeastOnce()).showEmptyView()
        verify(view, atLeastOnce()).dismissLoading()
    }

    @Test
    fun loadCommentsListTimeout() {
        `when`(dataSource.getComments(mockedValidReportId))
                .thenReturn(emitFlowableError(TimeoutException()))

        presenter.loadComments(mockedValidReportId)

        verify(view, atLeastOnce()).showLoading()
        verify(view, atLeastOnce()).showMessage(0)
        verify(view, atLeastOnce()).dismissLoading()
    }

    @Test
    fun loadCommentsListUnknownHost() {
        `when`(dataSource.getComments(mockedValidReportId))
                .thenReturn(emitFlowableError(UnknownHostException()))

        presenter.loadComments(mockedValidReportId)

        verify(view, atLeastOnce()).showLoading()
        verify(view, atLeastOnce()).showMessage(R.string.login_network_error)
        verify(view, atLeastOnce()).dismissLoading()
    }

    @Test
    fun loadCommentsListNetworkError() {
        `when`(dataSource.getComments(mockedValidReportId))
                .thenReturn(emitFlowableError(NetworkErrorException()))

        presenter.loadComments(mockedValidReportId)

        verify(view, atLeastOnce()).showLoading()
        verify(view, atLeastOnce()).showMessage(R.string.login_network_error)
        verify(view, atLeastOnce()).dismissLoading()
    }

    @Test
    fun submitComment() {
        `when`(dataSource.saveComment(mockedCommentText, mockedValidReportId))
                .thenReturn(Maybe.just(mockedComment))

        presenter.onClickSendComment(mockedCommentText, mockedValidReportId)

        verify(view, atLeastOnce()).showLoading()
        verify(view, atLeastOnce()).isValidCommentBodyState()
        verify(view, atLeastOnce()).commentCreated()
        verify(view, atLeastOnce()).dismissLoading()
    }

    @Test
    fun submitCommentTimeout() {
        `when`(dataSource.saveComment(mockedCommentText, mockedValidReportId))
                .thenReturn(emitMaybeError(TimeoutException()))

        presenter.onClickSendComment(mockedCommentText, mockedValidReportId)

        verify(view, atLeastOnce()).showLoading()
        verify(view, atLeastOnce()).isValidCommentBodyState()
        verify(view, atLeastOnce()).showMessage(R.string.login_network_error)
        verify(view, atLeastOnce()).dismissLoading()
    }

    @Test
    fun submitCommentUnknownHost() {
        `when`(dataSource.saveComment(mockedCommentText, mockedValidReportId))
                .thenReturn(emitMaybeError(UnknownHostException()))

        presenter.onClickSendComment(mockedCommentText, mockedValidReportId)

        verify(view, atLeastOnce()).showLoading()
        verify(view, atLeastOnce()).isValidCommentBodyState()
        verify(view, atLeastOnce()).showMessage(R.string.login_network_error)
        verify(view, atLeastOnce()).dismissLoading()
    }

    @Test
    fun submitCommentNetworkError() {
        `when`(dataSource.saveComment(mockedCommentText, mockedValidReportId))
                .thenReturn(emitMaybeError(NetworkErrorException()))

        presenter.onClickSendComment(mockedCommentText, mockedValidReportId)

        verify(view, atLeastOnce()).showLoading()
        verify(view, atLeastOnce()).isValidCommentBodyState()
        verify(view, atLeastOnce()).showMessage(R.string.login_network_error)
        verify(view, atLeastOnce()).dismissLoading()
    }

    @Test
    fun deleteComment() {
        `when`(dataSource.deleteComment(mockedCommentId))
                .thenReturn(Completable.complete())

        presenter.onClickRemoveComment(mockedCommentUiModel)

        verify(view, atLeastOnce()).showLoading()
        verify(view, atLeastOnce()).navigateToRemoveComment(mockedCommentUiModel)
        verify(view, atLeastOnce()).dismissLoading()
    }

    @Test
    fun deleteCommentTimeout() {
        `when`(dataSource.deleteComment(mockedCommentId))
                .thenReturn(emitCompletableError(TimeoutException()))

        presenter.onClickRemoveComment(mockedCommentUiModel)

        verify(view, atLeastOnce()).showLoading()
        verify(view, atLeastOnce()).showMessage(R.string.login_network_error)
        verify(view, atLeastOnce()).dismissLoading()
    }

    @Test
    fun deleteCommentUnknownHost() {
        `when`(dataSource.deleteComment(mockedCommentId))
                .thenReturn(emitCompletableError(UnknownHostException()))

        presenter.onClickRemoveComment(mockedCommentUiModel)

        verify(view, atLeastOnce()).showLoading()
        verify(view, atLeastOnce()).showMessage(R.string.login_network_error)
        verify(view, atLeastOnce()).dismissLoading()
    }

    @Test
    fun deleteCommentNetworkError() {
        `when`(dataSource.deleteComment(mockedCommentId))
                .thenReturn(emitCompletableError(NetworkErrorException()))

        presenter.onClickRemoveComment(mockedCommentUiModel)

        verify(view, atLeastOnce()).showLoading()
        verify(view, atLeastOnce()).showMessage(R.string.login_network_error)
        verify(view, atLeastOnce()).dismissLoading()
    }

}