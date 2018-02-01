package br.com.ilhasoft.voy.ui.addreport

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.net.Uri
import br.com.ilhasoft.voy.models.Report
import br.com.ilhasoft.voy.network.tags.TagService
import br.com.ilhasoft.voy.shared.helpers.RxHelper
import br.com.ilhasoft.voy.ui.report.ReportsActivity
import io.reactivex.subjects.PublishSubject
import timber.log.Timber

/**
 * Created by lucasbarros on 15/01/18.
 */
class ReportViewModel : ViewModel() {

    companion object {
        private const val LIST_MAX_SIZE = 4
    }

    val linkAdded = PublishSubject.create<String>().toSerialized()
    val linkRemoved = PublishSubject.create<String>().toSerialized()
    val linkAlreadyExist = PublishSubject.create<String>().toSerialized()

    var buttonEnable: MutableLiveData<Boolean> = MutableLiveData()
        private set
    var buttonTitle: MutableLiveData<Int> = MutableLiveData()
        private set

    private var tagsFromSever = MutableLiveData<MutableList<String>>()
    private val tagService by lazy { TagService() }

    var themeId: Int = ReportsActivity.themeId
    var name: String = ""
    var description: String? = null
    val links by lazy { mutableListOf<String>() }
    var medias = mutableListOf<Uri>()
    var mediasFromServer = mutableListOf<Uri>()
    val tags by lazy { mutableListOf<String>() }

    var report = Report()

    fun addUri(uri: Uri) {
        medias.add(uri)
        setButtonEnable(medias.isNotEmpty())
    }

    fun removeUri(uri: Uri) {
        medias.apply {
            remove(single { it == uri })
        }
        setButtonEnable(medias.isNotEmpty())
    }

    fun setButtonEnable(enable: Boolean) {
        buttonEnable.value = enable
    }

    fun setButtonTitle(resourceId: Int) {
        buttonTitle.value = resourceId
    }

    fun addLink(link: String) {
        if (links.contains(link)) {
            linkAlreadyExist.onNext(link)
        } else {
            links.add(link)
            linkAdded.onNext(link)
        }
    }

    fun removeLink(link: String) {
        links.remove(link)
        linkRemoved.onNext(link)
    }

    fun verifyListSize(): Boolean {
        return links.size < LIST_MAX_SIZE
    }

    fun getAllTags(): LiveData<MutableList<String>> {
        if (tagsFromSever.value == null || tagsFromSever.value?.isEmpty() == true) {
            tagService.getTags(themeId)
                    .compose(RxHelper.defaultFlowableSchedulers())
                    .subscribe({
                        tagsFromSever.value = it.map { it.tag }.toMutableList()
                    }, {
                        Timber.e(it)
                    })
        }
        return tagsFromSever
    }

    fun addTag(tag: String) {
        tags.add(tag)
    }

    fun removeTag(tag: String) {
        tags.remove(tag)
    }

    fun isTagSelected(tag: String): Boolean = tags.contains(tag)

    fun hasNewMedias(): Boolean {
        return mediasToSave().isNotEmpty()
    }

    fun mediasToDelete() = mediasFromServer.minus(medias)

    fun mediasToSave(): List<Uri> = medias.minus(mediasFromServer)

    fun setUpWithReport(report: Report) {
        this.report = report
        report.files.map { mediasFromServer.add(Uri.parse(it.file)) }
        medias.clear()
        medias.addAll(mediasFromServer)
        name = report.name
        description = report.description
        links.clear()
        links.addAll(report.urls)
        tags.clear()
        tags.addAll(report.tags)
    }

}