package br.com.ilhasoft.voy.ui.report.detail.carousel

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.ilhasoft.support.media.view.MediaViewOptions
import br.com.ilhasoft.support.media.view.models.ImageMedia
import br.com.ilhasoft.voy.GlideApp
import br.com.ilhasoft.voy.databinding.FragmentCarouselBinding
import br.com.ilhasoft.voy.models.ReportFile
import br.com.ilhasoft.voy.ui.base.BaseFragment

class CarouselFragment : BaseFragment(), CarouselContract {

    companion object {
        @JvmStatic
        fun newInstance(media: ReportFile?): CarouselFragment {
            val args = Bundle()
            args.putParcelable(ReportFile.TAG, media)
            return createWithArguments(args)
        }

        private fun createWithArguments(args: Bundle): CarouselFragment {
            val fragment = CarouselFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private var media: ReportFile? = null

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
        media = arguments.getParcelable(ReportFile.TAG)
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

    override fun displayMedia(media: ReportFile?) {
        media?.apply {
            if (mediaType == ReportFile.TYPE_VIDEO) {
                //FIXME handle unsupported media files type and codec (3gp, mp4 codec)
                startActivity(VideoViewActivity.createIntent(context, file))
            } else {
                startActivity(MediaViewOptions(ImageMedia(file)).createIntent(context))
            }
        }
    }

    private fun setupView() {
        binding.apply {
            presenter = this@CarouselFragment.presenter
            media = this@CarouselFragment.media
        }

        media?.let {
            if (it.mediaType == ReportFile.TYPE_VIDEO)
                binding.play.visibility = View.VISIBLE
            //TODO implement database query for local uris before requesting from url

            GlideApp.with(context)
                    .load(Uri.parse(it.file))
                    .centerCrop()
                    .into(binding.image)
        }
    }
}
