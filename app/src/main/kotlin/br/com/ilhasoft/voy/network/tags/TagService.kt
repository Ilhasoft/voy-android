package br.com.ilhasoft.voy.network.tags

import br.com.ilhasoft.voy.models.Tag
import br.com.ilhasoft.voy.network.ServiceFactory
import io.reactivex.Flowable
import io.reactivex.Single

/**
 * Created by developer on 09/01/18.
 */
class TagService : ServiceFactory<TagApi>(TagApi::class.java) {

    fun getTags(themeId: Int): Flowable<MutableList<Tag>> {
        val tagsRequest = mutableMapOf<String, Int>()
        tagsRequest["theme"] = themeId
        return api.getTags(tagsRequest)
    }

    fun getTag(tagId: Int): Single<Tag> = api.getTag(tagId)

}