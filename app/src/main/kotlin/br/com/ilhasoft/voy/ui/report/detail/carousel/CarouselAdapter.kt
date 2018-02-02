package br.com.ilhasoft.voy.ui.report.detail.carousel

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.PagerAdapter

/**
 * Created by jones on 1/1/18.
 */
class CarouselAdapter(fragmentManager: FragmentManager,
                      private var carouselItem: List<CarouselItem>?) :
        FragmentPagerAdapter(fragmentManager) {

    override fun getItemPosition(item: Any?): Int = PagerAdapter.POSITION_NONE

    override fun getItem(position: Int): Fragment = carouselItem?.get(position)!!.fragment

    override fun getCount(): Int = carouselItem!!.size

    fun addAll(items: List<CarouselItem>) {
        carouselItem = items
        notifyDataSetChanged()
    }

}