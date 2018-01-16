package br.com.ilhasoft.voy.ui.addreport

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.net.Uri
import io.reactivex.subjects.PublishSubject

/**
 * Created by lucasbarros on 15/01/18.
 */
class ReportViewModel : ViewModel() {

    companion object {
        private val LIST_MAX_SIZE = 4
    }

    val linkAdded = PublishSubject.create<String>().toSerialized()
    val linkRemoved = PublishSubject.create<String>().toSerialized()
    val linkAlreadyExist = PublishSubject.create<String>().toSerialized()
    var buttonEnable: MutableLiveData<Boolean> = MutableLiveData()
        private set
    var buttonTitle: MutableLiveData<Int> = MutableLiveData()
        private set

    var name: String? = null
    var description: String? = null
    val links by lazy { mutableListOf<String>() }
    var medias = mutableListOf<Uri>()

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
}