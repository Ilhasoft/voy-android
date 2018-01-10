package br.com.ilhasoft.voy.ui.base

import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.ViewGroup
import br.com.ilhasoft.support.core.app.IndeterminateProgressDialog
import br.com.ilhasoft.support.core.mvp.BasicView
import br.com.ilhasoft.voy.R

/**
 * Created by lucasbarros on 22/11/17.
 */
abstract class BaseActivity : AppCompatActivity(), BasicView {

    private val progressDialog: IndeterminateProgressDialog by lazy {
        val dialog = IndeterminateProgressDialog(this)
        dialog.setMessage(getString(R.string.wait))
        dialog.setCancelable(false)
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
        Snackbar.make(getRootView(), message, Snackbar.LENGTH_SHORT).show()
    }

    private fun getRootView(): View {
        val view = findViewById<View>(android.R.id.content)
        if (view is ViewGroup) {
            return view.getChildAt(0)
        }
        return window.decorView.rootView
    }

}