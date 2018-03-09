package br.com.ilhasoft.voy.ui.addreport

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.net.Uri
import br.com.ilhasoft.voy.models.Report
import br.com.ilhasoft.voy.models.ReportFile
import br.com.ilhasoft.voy.models.ThemeData
import io.reactivex.subjects.PublishSubject
import timber.log.Timber

/**
 * Created by lucasbarros on 15/01/18.
 */
class ReportViewModel(private val addReportInteractor: AddReportInteractor) : ViewModel() {

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

    private var tags = MutableLiveData<MutableList<String>>()

    var name: String = ""
    var description: String? = null
    val links by lazy { mutableListOf<String>() }
    var medias = mutableListOf<String>()
    var mediasFromServer = mutableListOf<ReportFile>()
    val selectedTags by lazy { mutableListOf<String>() }
    val allowLinks = ThemeData.allowLinks

    var report = Report()

    fun addUri(uri: String) {
        medias.add(uri)
        setButtonEnable(medias.isNotEmpty())
    }

    fun removeUri(uri: String) {
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
        if (tags.value == null || tags.value?.isEmpty() == true) {
            addReportInteractor.getTags(ThemeData.themeId)
                .subscribe({ tags.value = it }, { Timber.e(it) })
        }
        return tags
    }

    fun addTag(tag: String) {
        selectedTags.add(tag)
    }

    fun removeTag(tag: String) {
        selectedTags.remove(tag)
    }

    fun isTagSelected(tag: String): Boolean = selectedTags.contains(tag)

    fun hasNewMedias(): Boolean {
        return mediasToSave().isNotEmpty()
    }

    fun mediasToDelete(): List<ReportFile> {
        val toDelete: MutableList<ReportFile> = mutableListOf()
        mediasFromServer.forEach {
            if(!medias.contains(it.file))
                toDelete.add(it)
        }
        return toDelete
    }

    fun mediasToSave(): List<String> = medias.minus(mediasFromServer.map { it.file })

    fun setUpWithReport(report: Report) {
        this.report = report
        mediasFromServer.addAll(report.files)
        medias.clear()
        medias.addAll(mediasFromServer.map { it.file })
        name = report.name
        description = report.description
        links.clear()
        links.addAll(report.urls)
        selectedTags.clear()
        selectedTags.addAll(report.tags)
    }

}