package br.com.ilhasoft.voy.ui.addreport.medias

import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.ilhasoft.support.media.MediaSelectorDelegate
import br.com.ilhasoft.voy.databinding.FragmentAddMediasBinding
import br.com.ilhasoft.voy.models.AddReportFragmentType
import br.com.ilhasoft.voy.models.Report
import br.com.ilhasoft.voy.shared.widget.AddImageView
import br.com.ilhasoft.voy.ui.addreport.AddReportActivity
import br.com.ilhasoft.voy.ui.addreport.ReportViewModel
import br.com.ilhasoft.voy.ui.base.BaseFragment
import br.com.ilhasoft.voy.ui.shared.OnAddImageClickListener
import br.com.ilhasoft.voy.ui.shared.OnReportChangeListener

class AddMediasFragment :
        BaseFragment(),
        OnAddImageClickListener {

    companion object {
        const val TAG = "Medias"
        private const val ARG_REPORT = "report"
        fun newInstance(report: Report): AddMediasFragment {
            return AddMediasFragment().apply {
                arguments = Bundle().also { it.putParcelable(ARG_REPORT, report) }
            }
        }
    }

    private val binding: FragmentAddMediasBinding by lazy {
        FragmentAddMediasBinding.inflate(LayoutInflater.from(context))
    }

    private val model: ReportViewModel by lazy {
        ViewModelProviders.of(activity).get(ReportViewModel::class.java)
    }

    private val presenter: AddMediasFragmentPresenter by lazy { AddMediasFragmentPresenter(model) }

    private val reportListener: OnReportChangeListener by lazy { activity as AddReportActivity }
    private val delegate: MediaSelectorDelegate by lazy {
        val listener = MediaSelectorDelegate.OnLoadMediaListener {
            it?.let { onNewPhoto(it) }
        }
        MediaSelectorDelegate(context, "br.com.ilhasoft.voy.provider")
                .setOnLoadImageListener(listener)
                .setOnLoadVideoListener(listener)
    }
    private var imageViewSelected: AddImageView? = null

    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup?, savedInstanceState: Bundle?): View? {
        return binding.root
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        setupImages()
        presenter.attachView(this)
    }

    override fun onStart() {
        super.onStart()
        presenter.start()
    }

    override fun onResume() {
        super.onResume()
        model.isNextEnable(AddReportFragmentType.MEDIAS)
        reportListener.updateNextFragmentReference(AddReportFragmentType.DESCRIPTION)
    }

    override fun onStop() {
        super.onStop()
        presenter.stop()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        delegate.onActivityResult(requestCode, resultCode, data)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        delegate.onRequestPermissionResult(this, requestCode, grantResults)
    }

    override fun onClickAddImage(addImageView: AddImageView) {
        imageViewSelected = addImageView
        getMedia()
    }

    override fun onClickRemove(uri: Uri?) {
        presenter.removeMedia(uri)
    }

    private fun getMedia() {
        delegate.selectMedia(this, MediaSelectorDelegate.CAMERA_IMAGE
                or MediaSelectorDelegate.VIDEO)
    }

    private fun setupView() {
        binding.run {
            presenter = this@AddMediasFragment.presenter
        }
    }

    private fun setupImages() = with(binding) {
        image1.setImageListener(this@AddMediasFragment)
        image2.setImageListener(this@AddMediasFragment)
        image3.setImageListener(this@AddMediasFragment)
        image4.setImageListener(this@AddMediasFragment)
    }

    private fun onNewPhoto(uri: Uri) {
        imageViewSelected?.setMediaFromUri(uri)
        presenter.addMedia(uri)
    }

}
