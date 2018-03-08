package br.com.ilhasoft.voy.shared.helpers

import br.com.ilhasoft.voy.shared.schedulers.BaseScheduler
import io.reactivex.FlowableTransformer

/**
 * Created by lucasbarros on 09/01/18.
 */
object RxHelper {

    fun <Type> defaultFlowableSchedulers(baseScheduler: BaseScheduler) = FlowableTransformer<Type, Type> {
        it.subscribeOn(baseScheduler.io())
                .observeOn(baseScheduler.ui())
    }
}