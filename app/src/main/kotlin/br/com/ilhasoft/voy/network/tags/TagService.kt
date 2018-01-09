package br.com.ilhasoft.voy.network.tags

import br.com.ilhasoft.voy.models.Tag
import br.com.ilhasoft.voy.network.ServiceFactory
import br.com.ilhasoft.voy.shared.extensions.putIfNotNull
import io.reactivex.Flowable
import io.reactivex.Single

/**
 * Created by developer on 09/01/18.
 */
class TagService : ServiceFactory<TagApi>(TagApi::class.java) {

    fun getTags(theme: Int? = null, project: Int? = null): Flowable<MutableList<Tag>> {
        val tagsRequest = mutableMapOf<String, Int?>()
        tagsRequest.apply {
            putIfNotNull("theme", theme)
            putIfNotNull("project", project)
        }
        return api.getTags(tagsRequest)
    }

    fun getTag(tagId: String): Single<Tag> = api.getTag(tagId)

}