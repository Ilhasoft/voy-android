package br.com.ilhasoft.voy.shared.extensions

import br.com.ilhasoft.voy.shared.schedulers.BaseScheduler
import br.com.ilhasoft.voy.ui.base.LoadView
import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by felipe on 31/01/18.
 */
fun <T> Flowable<T>.fromIoToMainThread(): Flowable<T> {
    return compose { upstream ->
        upstream
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }
}

fun <T> Flowable<T>.fromIoToMainThread(baseScheduler: BaseScheduler): Flowable<T> {
    return compose { upstream ->
        upstream
                .subscribeOn(baseScheduler.io())
                .observeOn(baseScheduler.ui())
    }
}

fun <T> Flowable<T>.onMainThread(): Flowable<T> {
    return compose { upstream ->
        upstream
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
    }
}

fun <T> Flowable<T>.onMainThread(scheduler: BaseScheduler): Flowable<T> {
    return compose { upstream ->
        upstream
            .subscribeOn(scheduler.io())
            .observeOn(scheduler.ui())
    }
}

fun <T> Flowable<T>.loadControl(loadView: LoadView): Flowable<T> {
    return compose { upstream ->
        upstream
                .doOnSubscribe { loadView.showLoading() }
                .doAfterTerminate { loadView.dismissLoading() }
    }
}


fun Completable.fromIoToMainThread(): Completable {
    return compose { upstream ->
        upstream
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }
}

fun Completable.fromIoToMainThread(baseScheduler: BaseScheduler): Completable {
    return compose { upstream ->
        upstream
                .subscribeOn(baseScheduler.io())
                .observeOn(baseScheduler.ui())
    }
}

fun Completable.loadControl(loadView: LoadView): Completable {
    return compose { upstream ->
        upstream
                .doOnSubscribe { loadView.showLoading() }
                .doAfterTerminate { loadView.dismissLoading() }
    }
}

fun <T> Single<T>.fromIoToMainThread(): Single<T> {
    return compose { upstream ->
        upstream
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }
}

fun <T> Single<T>.fromIoToMainThread(baseScheduler: BaseScheduler): Single<T> {
    return compose { upstream ->
        upstream
                .subscribeOn(baseScheduler.io())
                .observeOn(baseScheduler.ui())
    }
}

fun <T> Single<T>.onMainThread(): Single<T> {
    return compose { upstream ->
        upstream
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
    }
}

fun <T> Single<T>.onMainThread(baseScheduler: BaseScheduler): Single<T> {
    return compose { upstream ->
        upstream
                .subscribeOn(baseScheduler.ui())
                .observeOn(baseScheduler.ui())
    }
}

fun <T> Single<T>.loadControl(loadView: LoadView): Single<T> {
    return compose { upstream ->
        upstream
                .doOnSubscribe { loadView.showLoading() }
                .doAfterTerminate { loadView.dismissLoading() }
    }
}

fun <T> Maybe<T>.fromIoToMainThread(): Maybe<T> {
    return compose { upstream ->
        upstream
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }
}

fun <T> Maybe<T>.loadControl(loadView: LoadView): Maybe<T> {
    return compose { upstream ->
        upstream
                .doOnSubscribe { loadView.showLoading() }
                .doAfterTerminate { loadView.dismissLoading() }
    }
}

fun <T> Observable<T>.fromIoToMainThread(): Observable<T> {
    return compose { upstream ->
        upstream
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }
}

fun <T> Observable<T>.loadControl(loadView: LoadView): Observable<T> {
    return compose { upstream ->
        upstream
                .doOnSubscribe { loadView.showLoading() }
                .doAfterTerminate { loadView.dismissLoading() }
    }
}