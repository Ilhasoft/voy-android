package br.com.ilhasoft.voy.ui.comment

import br.com.ilhasoft.support.core.mvp.Presenter
import br.com.ilhasoft.voy.R
import br.com.ilhasoft.voy.models.Preferences
import br.com.ilhasoft.voy.models.User
import br.com.ilhasoft.voy.network.comments.CommentRepository
import br.com.ilhasoft.voy.shared.extensions.fromIoToMainThread
import br.com.ilhasoft.voy.shared.extensions.loadControl
import br.com.ilhasoft.voy.shared.helpers.ErrorHandlerHelper
import br.com.ilhasoft.voy.shared.schedulers.BaseScheduler
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable

class CommentsPresenter(
        private val commentRepository: CommentRepository,
        private val commentsUIMapper: CommentsUIMapper,
        private val preferences: Preferences,
        private val scheduler: BaseScheduler
) : Presenter<CommentsContract>(CommentsContract::class.java) {

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    override fun detachView() {
        compositeDisposable.clear()
        super.detachView()
    }

    fun loadComments(reportId: Int) {
        compositeDisposable.add(
                commentRepository.getComments(reportId)
                        .fromIoToMainThread(scheduler)
                        .loadControl(view)
                        .map(commentsUIMapper)
                        .doOnNext { if (it.isEmpty()) view.showEmptyView() }
                        .subscribe(
                                { view.populateComments(it) },
                                { ErrorHandlerHelper.showError(it) { msg -> view.showMessage(msg) } }
                        )
        )
    }

    fun onClickNavigateBack() {
        view.navigateBack()
    }

    fun onClickRemoveComment(comment: CommentUIModel) {
        compositeDisposable.add(
                commentRepository.deleteComment(comment.commentId, comment.reportId)
                        .fromIoToMainThread(scheduler)
                        .loadControl(view)
                        .subscribe(
                                { view.navigateToRemoveComment(comment) },
                                {
                                    ErrorHandlerHelper.showError(it, R.string.comment_delete_error) { msg ->
                                        view.showMessage(msg)
                                    }
                                }
                        )
        )
    }

    fun onClickSendComment(body: String, reportId: Int) {
        compositeDisposable.add(
                Single.just(body to reportId)
                        .subscribeOn(scheduler.ui())
                        .filter { view.isValidCommentBodyState() }
                        .observeOn(scheduler.io())
                        .flatMap { (text, id) -> commentRepository.saveComment(text, id) }
                        .observeOn(scheduler.ui())
                        .loadControl(view)
                        .subscribe(
                                { view.commentCreated() },
                                {
                                    ErrorHandlerHelper.showError(it, R.string.comment_create_error) { msg ->
                                        view.showMessage(msg)
                                    }
                                }
                        )
        )
    }

    fun isMyID(id: Int): Boolean {
        return if (preferences.contains(User.ID).not()) {
            false
        } else {
            id == preferences.getInt(User.ID)
        }
    }

}