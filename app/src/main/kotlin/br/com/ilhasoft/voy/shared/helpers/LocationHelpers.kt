package br.com.ilhasoft.voy.shared.helpers

import io.reactivex.Observable


/**
 * Created by lucasbarros on 26/01/18.
 */
object LocationHelpers {

    fun isLocationInsidePolygon(userLat: Double, userLng: Double, vertices: List<Pair<Double, Double>>): Observable<Boolean> {
        return Observable.fromCallable { isPointInPolygon(Pair(userLat, userLng), vertices) }
    }

    private fun isPointInPolygon(tap: Pair<Double, Double>, vertices: List<Pair<Double, Double>>): Boolean {
        val intersectCount = (0 until vertices.size - 1).count { rayCastIntersect(tap, vertices[it], vertices[it + 1]) }
        return intersectCount % 2 == 1 // odd = inside, even = outside;
    }

    private fun rayCastIntersect(tap: Pair<Double, Double>, vertA: Pair<Double, Double>, vertB: Pair<Double, Double>): Boolean {

        val aY = vertA.second
        val bY = vertB.second
        val aX = vertA.first
        val bX = vertB.first
        val pY = tap.second
        val pX = tap.first

        if (aY > pY && bY > pY || aY < pY && bY < pY || aX < pX && bX < pX) {
            // a and b can't both be above or below pt.y
            // a and b must be east of pt.x
            return false
        }

        val m = (aY - bY) / (aX - bX)
        val bee = -aX * m + aY // y = mx + b
        val x = (pY - bee) / m

        return x > pX
    }
}