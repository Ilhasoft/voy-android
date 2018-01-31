package br.com.ilhasoft.voy.shared.extensions

import br.com.ilhasoft.voy.ui.base.LoadView
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by felipe on 31/01/18.
 */
fun <T> Flowable<T>.fromIoToMainThread(): Flowable<T> {
    return compose{ upstream ->
        upstream
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }
}

fun <T> Flowable<T>.loadControl(loadView: LoadView): Flowable<T> {
    return compose{ upstream ->
        upstream
                .doOnSubscribe { loadView.showLoad() }
                .doAfterTerminate { loadView.dismissLoad() }
    }
}

fun Completable.fromIoToMainThread(): Completable {
    return compose{ upstream ->
        upstream
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }
}

fun Completable.loadControl(loadView: LoadView): Completable {
    return compose{ upstream ->
        upstream
                .doOnSubscribe { loadView.showLoad() }
                .doAfterTerminate { loadView.dismissLoad() }
    }
}