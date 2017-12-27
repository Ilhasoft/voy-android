package br.com.ilhasoft.voy.ui.addreport.medias

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.View
import br.com.ilhasoft.support.media.MediaSelectorDelegate
import br.com.ilhasoft.voy.databinding.FragmentAddMediasBinding
import br.com.ilhasoft.voy.models.Fragments
import br.com.ilhasoft.voy.models.Media
import br.com.ilhasoft.voy.shared.widget.AddImageView
import br.com.ilhasoft.voy.ui.addreport.AddReportActivity

import br.com.ilhasoft.voy.ui.base.BaseFragment
import br.com.ilhasoft.voy.ui.shared.OnAddImageClickListener
import br.com.ilhasoft.voy.ui.shared.OnReportChangeListener


class AddMediasFragment :
        BaseFragment(),
        AddMediasFragmentContract,
        OnAddImageClickListener {

    companion object {
        val TAG = "Medias"
    }

    private val binding: FragmentAddMediasBinding by lazy {
        FragmentAddMediasBinding.inflate(LayoutInflater.from(context))
    }

    private val presenter: AddMediasFragmentPresenter by lazy { AddMediasFragmentPresenter() }

    private val reportListener: OnReportChangeListener by lazy { activity as AddReportActivity }

    private val delegate: MediaSelectorDelegate by lazy {
        val listener = MediaSelectorDelegate.OnLoadMediaListener {
            it?.let { onNewPhoto(it) }
        }
        MediaSelectorDelegate(context, "br.com.ilhasoft.voy.provider")
                .setOnLoadGalleryImageListener(listener)
                .setOnLoadImageListener(listener)
                .setOnLoadVideoListener(listener)
    }

    private var imageViewSelected: AddImageView? = null

    override fun onCreateView(inflater: LayoutInflater, parent: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding.presenter = presenter
        return binding.root
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        setUpImages()
        presenter.attachView(this)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        presenter.setReportReference(arguments.getParcelable("Report"))
    }

    override fun onStart() {
        super.onStart()
        presenter.start()
    }

    override fun onResume() {
        super.onResume()
        presenter.verifyMediaListSize()
        reportListener.updateNextFragmentReference(Fragments.DESCRIPTION)
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

    override fun changeActionButtonStatus(status: Boolean) {
        reportListener.changeActionButtonStatus(status)
    }

    override fun updateReportMedias(mediaList: MutableList<Media>) {
        reportListener.updateReportMedias(mediaList)
    }

    private fun setupView() {
        binding.run {
            presenter = this@AddMediasFragment.presenter
        }
    }

    private fun setUpImages() = with(binding) {
        image1.setImageListener(this@AddMediasFragment)
        image2.setImageListener(this@AddMediasFragment)
        image3.setImageListener(this@AddMediasFragment)
        image4.setImageListener(this@AddMediasFragment)
    }

    private fun onNewPhoto(uri: Uri) {
        imageViewSelected?.setMediaFromUri(uri)
        presenter.addMedia(uri)
    }

    override fun getMedia() {
        delegate.selectMedia(this, MediaSelectorDelegate.GALLERY_IMAGE
                or MediaSelectorDelegate.CAMERA_IMAGE
                or MediaSelectorDelegate.VIDEO)
    }

}
