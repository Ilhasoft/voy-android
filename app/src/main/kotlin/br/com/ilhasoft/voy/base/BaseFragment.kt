package br.com.ilhasoft.voy.base

import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.view.View
import br.com.ilhasoft.support.core.app.IndeterminateProgressDialog
import br.com.ilhasoft.support.core.mvp.BasicView
import br.com.ilhasoft.voy.R

/**
 * Created by lucasbarros on 22/11/17.
 */
abstract class BaseFragment: Fragment(), BasicView {

    private val progressDialog: IndeterminateProgressDialog by lazy {
        val dialog = IndeterminateProgressDialog(context)
        dialog.setMessage(getString(R.string.wait))
        dialog
    }

    override fun showLoading() {
        progressDialog.show()
    }

    override fun dismissLoading() {
        progressDialog.dismiss()
    }

    override fun showMessage(messageId: Int) {
        showMessage(getString(messageId))
    }

    override fun showMessage(message: CharSequence) {
        getRootView()?.let { Snackbar.make(it, message, Snackbar.LENGTH_LONG).show() }
    }

    private fun getRootView(): View? = if (view != null) view else activity.window.decorView.rootView

}