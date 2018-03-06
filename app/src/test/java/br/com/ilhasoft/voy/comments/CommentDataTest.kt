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
    private lateinit var dataSource: CommentDataSource
    private lateinit var repository: CommentRepository

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
        repository = CommentRepository(dataSource)
    }

    @Test
    fun shouldLoadCommentsList() {
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
    fun shouldLoadEmptyCommentsList() {
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
    fun shouldThrowExceptionWhenLoadCommentsListWithTimeout() {
        `when`(getCommentsRef(mockedValidReportId))
                .thenReturn(emitFlowableError(TimeoutException()))

        setupCommentsImpl(mockedValidReportId)
                .test()
                .assertSubscribed()
                .assertError { it is TimeoutException }
    }

    @Test
    fun shouldThrowExceptionWhenLoadCommentsListWithNetworkException() {
        `when`(getCommentsRef(mockedValidReportId))
                .thenReturn(emitFlowableError(NetworkErrorException()))

        setupCommentsImpl(mockedValidReportId)
                .test()
                .assertSubscribed()
                .assertError { it is NetworkErrorException }
    }

    @Test
    fun shouldThrowExceptionWhenLoadCommentsListWithUnknownHostException() {
        `when`(getCommentsRef(mockedValidReportId))
                .thenReturn(emitFlowableError(UnknownHostException()))

        setupCommentsImpl(mockedValidReportId)
                .test()
                .assertSubscribed()
                .assertError { it is UnknownHostException }
    }

    @Test
    fun shouldThrowExceptionWhenSubmitComment() {
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
    fun shouldThrowExceptionWhenSubmitCommentWithTimeout() {
        `when`(getSaveCommentRef())
                .thenReturn(emitMaybeError(TimeoutException()))

        setupSaveCommentImpl()
                .test()
                .assertSubscribed()
                .assertError { it is TimeoutException }
    }

    @Test
    fun shouldThrowExceptionWhenSubmitCommentWithNetworkException() {
        `when`(getSaveCommentRef())
                .thenReturn(emitMaybeError(NetworkErrorException()))

        setupSaveCommentImpl()
                .test()
                .assertSubscribed()
                .assertError { it is NetworkErrorException }
    }

    @Test
    fun shouldThrowExceptionWhenSubmitCommentWithUnknownHostException() {
        `when`(getSaveCommentRef())
                .thenReturn(emitMaybeError(UnknownHostException()))

        setupSaveCommentImpl()
                .test()
                .assertSubscribed()
                .assertError { it is UnknownHostException }
    }

    @Test
    fun shouldThrowExceptionWhenDeleteComment() {
        `when`(getDeleteCommentRef())
                .thenReturn(Completable.complete())

        setupDeleteCommentImpl()
                .test()
                .assertSubscribed()
                .assertComplete()
    }

    @Test
    fun shouldThrowExceptionWhenDeleteCommentWithTimeout() {
        `when`(getDeleteCommentRef())
                .thenReturn(emitCompletableError(TimeoutException()))

        setupDeleteCommentImpl()
                .test()
                .assertSubscribed()
                .assertError { it is TimeoutException }
    }

    @Test
    fun shouldThrowExceptionWhenDeleteCommentWithNetworkException() {
        `when`(getDeleteCommentRef())
                .thenReturn(emitCompletableError(NetworkErrorException()))

        setupDeleteCommentImpl()
                .test()
                .assertSubscribed()
                .assertError { it is NetworkErrorException }
    }

    @Test
    fun shouldThrowExceptionWhenDeleteCommentWithUnknownHostException() {
        `when`(getDeleteCommentRef())
                .thenReturn(emitCompletableError(UnknownHostException()))

        setupDeleteCommentImpl()
                .test()
                .assertSubscribed()
                .assertError { it is UnknownHostException }
    }

    private fun getCommentsRef(reportId: Int) = dataSource.getComments(reportId)

    private fun getSaveCommentRef() = dataSource.saveComment(mockedCommentText, mockedValidReportId)

    private fun getDeleteCommentRef() = dataSource.deleteComment(mockedCommentId)

    private fun setupCommentsImpl(reportId: Int) = repository.getComments(reportId)

    private fun setupSaveCommentImpl() = repository.saveComment(mockedCommentText, mockedValidReportId)

    private fun setupDeleteCommentImpl() = repository.deleteComment(mockedCommentId)

}