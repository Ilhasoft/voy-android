package br.com.ilhasoft.voy.ui.addreport.theme

import android.os.Bundle
import android.view.LayoutInflater

import android.view.ViewGroup
import android.view.View


import br.com.ilhasoft.voy.databinding.FragmentAddThemeBinding
import br.com.ilhasoft.voy.ui.base.BaseFragment

class AddThemeFragment : BaseFragment(), AddThemeFragmentContract {

    private val binding: FragmentAddThemeBinding by lazy {
        FragmentAddThemeBinding.inflate(LayoutInflater.from(context))
    }

    private val presenter: AddThemeFragmentPresenter by lazy { AddThemeFragmentPresenter() }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding.presenter = presenter
        return binding.root
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.attachView(this)
    }

    override fun onStart() {
        super.onStart()
        presenter.start()
    }

    override fun onStop() {
        super.onStop()
        presenter.stop()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }
}
