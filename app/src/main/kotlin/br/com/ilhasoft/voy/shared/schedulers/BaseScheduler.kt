package br.com.ilhasoft.voy.shared.schedulers

import io.reactivex.Scheduler

/**
 * Created by developer on 02/03/18.
 */
interface BaseScheduler {

    fun io(): Scheduler

    fun ui(): Scheduler

}