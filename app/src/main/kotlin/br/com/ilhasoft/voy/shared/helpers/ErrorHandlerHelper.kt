package br.com.ilhasoft.voy.shared.helpers

import android.accounts.NetworkErrorException
import br.com.ilhasoft.voy.R
import retrofit2.HttpException
import timber.log.Timber
import java.net.UnknownHostException

/**
 * Created by felipe on 01/02/18.
 */
object ErrorHandlerHelper {

    fun showError(throwable: Throwable, defaultError: Int = 0, block: (Int) -> Unit) {
        Timber.e(throwable)
        block.invoke(
                when (throwable) {
                    is HttpException -> R.string.http_request_error
                    is UnknownHostException,
                    is NetworkErrorException -> R.string.login_network_error
                    else -> defaultError
                }
        )
    }
}