package br.com.ilhasoft.voy.network.mappers

import br.com.ilhasoft.voy.models.Mapper
import br.com.ilhasoft.voy.network.ServiceFactory
import br.com.ilhasoft.voy.shared.extensions.putIfNotNull
import io.reactivex.Flowable
import io.reactivex.Single

/**
 * Created by developer on 09/01/18.
 */
class MapperService : ServiceFactory<MapperApi>(MapperApi::class.java) {

    fun getMappers(theme: Int? = null): Flowable<MutableList<Mapper>> {
        val mappersRequest = mutableMapOf<String, Int?>()
        mappersRequest.apply { putIfNotNull("theme", theme) }
        return api.getMappers(mappersRequest)
    }

    fun getMapper(mapperId: String, theme: Int? = null): Single<Mapper> {
        val mapperRequest = mutableMapOf<String, Int?>()
        mapperRequest.apply { putIfNotNull("theme", theme) }
        return api.getMapper(mapperId, mapperRequest)
    }

}