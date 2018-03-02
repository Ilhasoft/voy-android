package br.com.ilhasoft.voy.shared.schedulers

import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers

/**
 * Created by developer on 02/03/18.
 */
class ImmediateScheduler : BaseScheduler {

    override fun io(): Scheduler = Schedulers.trampoline()

    override fun ui(): Scheduler = Schedulers.trampoline()

}