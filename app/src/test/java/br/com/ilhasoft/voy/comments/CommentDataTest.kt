package br.com.ilhasoft.voy.comments

import android.accounts.NetworkErrorException
import br.com.ilhasoft.voy.extensions.emitCompletableError
import br.com.ilhasoft.voy.extensions.emitFlowableError
import br.com.ilhasoft.voy.extensions.emitMaybeError
import br.com.ilhasoft.voy.models.ReportComment
import br.com.ilhasoft.voy.models.User
import br.com.ilhasoft.voy.network.comments.CommentDataSource
import br.com.ilhasoft.voy.network.comments.CommentRepository
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import java.net.UnknownHostException
import java.util.*
import java.util.concurrent.TimeoutException

/**
 * Created by developer on 27/02/18.
 */
class CommentDataTest {

    @Mock
    private lateinit var commentDataSource: CommentDataSource
    private lateinit var commentRepository: CommentRepository

    private val mockedValidReportId = 100
    private val mockedInvalidReportId = -1

    private val mockedCommentId = 101
    private val mockedCommentText: String = "This is a test comment"
    private val mockedUser = User(1, "2", "mockeduser", "mockuser@test.test")

    private val mockedComment = ReportComment(mockedCommentId, mockedCommentText, mockedUser,
            Date(), Date(), mockedValidReportId)
    private val mockedComments = listOf(mockedComment, mockedComment)

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        commentRepository = CommentRepository(commentDataSource)
    }

    @Test
    fun loadCommentsList() {
        `when`(getCommentsRef(mockedValidReportId))
                .thenReturn(Flowable.just(mockedComments))

        setupCommentsImpl(mockedValidReportId)
                .test()
                .assertSubscribed()
                .assertNoErrors()
                .assertComplete()
                .assertValue { comments: List<ReportComment> -> comments.isNotEmpty() }
    }

    @Test
    fun loadEmptyCommentsList() {
        `when`(getCommentsRef((mockedInvalidReportId)))
                .thenReturn(Flowable.empty())

        setupCommentsImpl(mockedInvalidReportId)
                .test()
                .assertSubscribed()
                .assertNoErrors()
                .assertComplete()
                .assertValueCount(0)
    }

    @Test
    fun loadCommentsListWithTimeout() {
        `when`(getCommentsRef(mockedValidReportId))
                .thenReturn(emitFlowableError(TimeoutException()))

        setupCommentsImpl(mockedValidReportId)
                .test()
                .assertSubscribed()
                .assertError { it is TimeoutException }
    }

    @Test
    fun loadCommentsListWithNetworkException() {
        `when`(getCommentsRef(mockedValidReportId))
                .thenReturn(emitFlowableError(NetworkErrorException()))

        setupCommentsImpl(mockedValidReportId)
                .test()
                .assertSubscribed()
                .assertError { it is NetworkErrorException }
    }

    @Test
    fun loadCommentsListWithUnknownHostException() {
        `when`(getCommentsRef(mockedValidReportId))
                .thenReturn(emitFlowableError(UnknownHostException()))

        setupCommentsImpl(mockedValidReportId)
                .test()
                .assertSubscribed()
                .assertError { it is UnknownHostException }
    }

    @Test
    fun submitComment() {
        `when`(getSaveCommentRef())
                .thenReturn(Maybe.just(mockedComment))

        setupSaveCommentImpl()
                .test()
                .assertSubscribed()
                .assertNoErrors()
                .assertComplete()
                .assertValueCount(1)
    }

    @Test
    fun submitCommentWithTimeout() {
        `when`(getSaveCommentRef())
                .thenReturn(emitMaybeError(TimeoutException()))

        setupSaveCommentImpl()
                .test()
                .assertSubscribed()
                .assertError { it is TimeoutException }
    }

    @Test
    fun submitCommentWithNetworkException() {
        `when`(getSaveCommentRef())
                .thenReturn(emitMaybeError(NetworkErrorException()))

        setupSaveCommentImpl()
                .test()
                .assertSubscribed()
                .assertError { it is NetworkErrorException }
    }

    @Test
    fun submitCommentWithUnknownHostException() {
        `when`(getSaveCommentRef())
                .thenReturn(emitMaybeError(UnknownHostException()))

        setupSaveCommentImpl()
                .test()
                .assertSubscribed()
                .assertError { it is UnknownHostException }
    }

    @Test
    fun deleteComment() {
        `when`(getDeleteCommentRef())
                .thenReturn(Completable.complete())

        setupDeleteCommentImpl()
                .test()
                .assertSubscribed()
                .assertComplete()
    }

    @Test
    fun deleteCommentWithTimeout() {
        `when`(getDeleteCommentRef())
                .thenReturn(emitCompletableError(TimeoutException()))

        setupDeleteCommentImpl()
                .test()
                .assertSubscribed()
                .assertError { it is TimeoutException }
    }

    @Test
    fun deleteCommentWithNetworkException() {
        `when`(getDeleteCommentRef())
                .thenReturn(emitCompletableError(NetworkErrorException()))

        setupDeleteCommentImpl()
                .test()
                .assertSubscribed()
                .assertError { it is NetworkErrorException }
    }

    @Test
    fun deleteCommentWithUnknownHostException() {
        `when`(getDeleteCommentRef())
                .thenReturn(emitCompletableError(UnknownHostException()))

        setupDeleteCommentImpl()
                .test()
                .assertSubscribed()
                .assertError { it is UnknownHostException }
    }

    private fun getCommentsRef(reportId: Int) = commentDataSource.getComments(reportId)

    private fun getSaveCommentRef() = commentDataSource.saveComment(mockedCommentText, mockedValidReportId)

    private fun getDeleteCommentRef() = commentDataSource.deleteComment(mockedCommentId)

    private fun setupCommentsImpl(reportId: Int) = commentRepository.getComments(reportId)

    private fun setupSaveCommentImpl() = commentRepository.saveComment(mockedCommentText, mockedValidReportId)

    private fun setupDeleteCommentImpl() = commentRepository.deleteComment(mockedCommentId)

}