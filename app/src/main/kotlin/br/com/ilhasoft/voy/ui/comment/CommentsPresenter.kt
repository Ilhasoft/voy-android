package br.com.ilhasoft.voy.ui.comment

import br.com.ilhasoft.support.core.mvp.Presenter
import br.com.ilhasoft.voy.R
import br.com.ilhasoft.voy.models.Preferences
import br.com.ilhasoft.voy.models.User
import br.com.ilhasoft.voy.network.comments.CommentsService
import br.com.ilhasoft.voy.shared.extensions.fromIoToMainThread
import br.com.ilhasoft.voy.shared.extensions.loadControl
import br.com.ilhasoft.voy.shared.helpers.ErrorHandlerHelper
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class CommentsPresenter(
        private val commentsService: CommentsService,
        private val commentsUIMapper: CommentsUIMapper,
        private val preferences: Preferences
) : Presenter<CommentsContract>(CommentsContract::class.java) {

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    override fun detachView() {
        compositeDisposable.clear()
        super.detachView()
    }

    fun loadComments(reportId: Int) {
        compositeDisposable.add(
                commentsService.getComments(reportId)
                        .fromIoToMainThread()
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
                commentsService.deleteComment(comment.commentId, comment.reportId)
                        .fromIoToMainThread()
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
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .filter { view.isValidCommentBodyState() }
                        .observeOn(Schedulers.io())
                        .flatMap { (text, id) -> commentsService.saveComment(text, id) }
                        .observeOn(AndroidSchedulers.mainThread())
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