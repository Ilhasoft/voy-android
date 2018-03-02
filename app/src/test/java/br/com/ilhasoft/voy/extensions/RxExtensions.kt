package br.com.ilhasoft.voy.extensions

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe

/**
 * Created by developer on 02/03/18.
 */
fun <T> emitFlowableError(throwable: Throwable): Flowable<T> = Flowable.error(throwable)

fun <T> emitMaybeError(throwable: Throwable): Maybe<T> = Maybe.error(throwable)

fun emitCompletableError(throwable: Throwable): Completable = Completable.error(throwable)