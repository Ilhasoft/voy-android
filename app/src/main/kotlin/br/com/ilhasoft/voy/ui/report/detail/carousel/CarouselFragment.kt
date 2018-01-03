package br.com.ilhasoft.voy.ui.report.detail.carousel

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater

import android.view.ViewGroup
import android.view.View
import br.com.ilhasoft.support.media.view.*
import br.com.ilhasoft.support.media.view.models.ImageMedia
import br.com.ilhasoft.support.media.view.models.VideoMedia
import br.com.ilhasoft.voy.GlideApp

import br.com.ilhasoft.voy.databinding.FragmentCarouselBinding
import br.com.ilhasoft.voy.models.Media
import br.com.ilhasoft.voy.ui.base.BaseFragment

class CarouselFragment : BaseFragment(), CarouselContract {


    companion object {
        @JvmStatic
        fun newInstance(media: Media?): CarouselFragment {
            val args = Bundle()
            args.putParcelable(Media.TAG, media)
            return createWithArguments(args)
        }

        private fun createWithArguments(args: Bundle): CarouselFragment {
            val fragment = CarouselFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private var mediaModel: MediaModel? = null

    private var media: Media? = null

    private val listener by lazy { Listener() }

    private val binding: FragmentCarouselBinding by lazy {
        FragmentCarouselBinding.inflate(LayoutInflater.from(context))
    }

    private val presenter: CarouselPresenter by lazy { CarouselPresenter() }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            binding.root


    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.attachView(this)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        media = arguments.getParcelable(Media.TAG)
        setupView()
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

    override fun displayMedia(media: Media?) {
        media?.apply {
            mediaModel =
                    if (type == Media.TYPE_VIDEO)
                        //change to handle url and video thumbnail
                        VideoMedia(uri.toString(), "https://ilhacloud-dev.s3.amazonaws.com/clickcondo/files/c83b5950a01e73dafa327779dced93bf_JPEG_20161205_175619_1472840156.jpg")
                    else
                        ImageMedia(uri.toString())

            startActivity(MediaViewOptions(mediaModel)
                    .setOnClickMediaListener(listener)
                    .createIntent(context))
        }
    }

    private fun setupView() {
        binding.run {
            presenter = this@CarouselFragment.presenter
            media = this@CarouselFragment.media
            media?.apply {
                if (type == Media.TYPE_VIDEO)
                    play.visibility = View.VISIBLE

                GlideApp.with(context)
                        .load(uri)
                        .centerCrop()
                        .into(image)
            }
        }
    }

    private fun isVideo(uri: Uri?): Boolean {
        val mimeType = context.contentResolver.getType(uri)
        return mimeType != null && mimeType.startsWith("video")
    }

    class Listener : OnClickMediaListener {

        override fun onClick(mediaViewFragment: MediaViewFragment, mediaModel: MediaModel) {
            mediaViewFragment.startActivity(Intent(Intent.ACTION_VIEW, mediaModel.uri))
        }

    }


}
