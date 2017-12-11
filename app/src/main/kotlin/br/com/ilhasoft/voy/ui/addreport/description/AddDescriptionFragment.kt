package br.com.ilhasoft.voy.ui.addreport.description

import android.os.Bundle
import android.view.LayoutInflater

import android.view.ViewGroup
import android.view.View


import br.com.ilhasoft.voy.databinding.FragmentAddDescriptionBinding
import br.com.ilhasoft.voy.ui.base.BaseFragment

class AddDescriptionFragment : BaseFragment(), AddDescriptionFragmentContract {

    private val binding: FragmentAddDescriptionBinding by lazy {
        FragmentAddDescriptionBinding.inflate(LayoutInflater.from(context))
    }

    private val presenter: AddDescriptionFragmentPresenter by lazy { AddDescriptionFragmentPresenter() }

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
