package br.com.ilhasoft.voy.ui.report.detail.carousel

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.ilhasoft.support.media.view.MediaViewOptions
import br.com.ilhasoft.support.media.view.models.ImageMedia
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

    private var media: Media? = null

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
            if (type == Media.TYPE_VIDEO) {
                //FIXME handle unsupported media files type and codec (3gp, mp4 codec)
                startActivity(VideoViewActivity.createIntent(context).putExtra("url", url))
            } else {
                startActivity(MediaViewOptions(ImageMedia(url)).createIntent(context))
            }
        }
    }

    private fun setupView() {
        binding.run {
            presenter = this@CarouselFragment.presenter
            media = this@CarouselFragment.media
            media?.apply {
                if (type == Media.TYPE_VIDEO)
                    play.visibility = View.VISIBLE
                //TODO implement database query for local uris before requesting from url
                GlideApp.with(context)
                        .load(url)
                        .centerCrop()
                        .into(image)
            }
        }
    }

}
