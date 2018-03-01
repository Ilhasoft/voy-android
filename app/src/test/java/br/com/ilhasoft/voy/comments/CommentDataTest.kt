package br.com.ilhasoft.voy.comments

import android.accounts.NetworkErrorException
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
    fun loadCommentsListFromReport() {
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
    fun loadEmptyCommentsListFromReport() {
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
    fun loadCommentsListFromReportWithTimeout() {
        `when`(getCommentsRef(mockedValidReportId))
                .thenReturn(Flowable.error(TimeoutException()))

        setupCommentsImpl(mockedValidReportId)
                .test()
                .assertSubscribed()
                .assertError(TimeoutException::class.java)
    }

    @Test
    fun loadCommentsListFromReportWithNetworkException() {
        `when`(getCommentsRef(mockedValidReportId))
                .thenReturn(Flowable.error(NetworkErrorException()))

        setupCommentsImpl(mockedValidReportId)
                .test()
                .assertSubscribed()
                .assertError(NetworkErrorException::class.java)
    }

    @Test
    fun submitReportComment() {
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
    fun submitReportCommentWithTimeout() {
        `when`(getSaveCommentRef())
                .thenReturn(Maybe.error(TimeoutException()))

        setupSaveCommentImpl()
                .test()
                .assertSubscribed()
                .assertError(TimeoutException::class.java)
    }

    @Test
    fun submitReportCommentWithNetworkException() {
        `when`(getSaveCommentRef())
                .thenReturn(Maybe.error(NetworkErrorException()))

        setupSaveCommentImpl()
                .test()
                .assertSubscribed()
                .assertError(NetworkErrorException::class.java)
    }

    @Test
    fun deleteReportComment() {
        `when`(getDeleteCommentRef())
                .thenReturn(Completable.complete())

        setupDeleteCommentImpl()
                .test()
                .assertSubscribed()
                .assertComplete()
    }

    @Test
    fun deleteReportCommentWithTimeout() {
        `when`(getDeleteCommentRef())
                .thenReturn(Completable.error(TimeoutException()))

        setupDeleteCommentImpl()
                .test()
                .assertSubscribed()
                .assertError(TimeoutException::class.java)
    }

    @Test
    fun deleteReportCommentWithNetworkException() {
        `when`(getDeleteCommentRef())
                .thenReturn(Completable.error(NetworkErrorException()))

        setupDeleteCommentImpl()
                .test()
                .assertSubscribed()
                .assertError(NetworkErrorException::class.java)
    }

    private fun getDataSource() = commentDataSource

    private fun getCommentsRef(reportId: Int) = getDataSource().getComments(reportId)

    private fun getSaveCommentRef() = getDataSource().saveComment(mockedCommentText, mockedValidReportId)

    private fun getDeleteCommentRef() = getDataSource().deleteComment(mockedCommentId)

    private fun getRepository() = commentRepository

    private fun setupCommentsImpl(reportId: Int) = getRepository().getComments(reportId)

    private fun setupSaveCommentImpl() = getRepository().saveComment(mockedCommentText, mockedValidReportId)

    private fun setupDeleteCommentImpl() = getRepository().deleteComment(mockedCommentId)

}