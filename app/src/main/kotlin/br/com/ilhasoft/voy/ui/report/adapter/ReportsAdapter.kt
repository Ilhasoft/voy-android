package br.com.ilhasoft.voy.ui.report.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.PagerAdapter

/**
 * Created by developer on 28/11/17.
 */
class ReportsAdapter(fragmentManager: FragmentManager,
                     private val navigationItems: MutableList<NavigationItem>) :
        FragmentPagerAdapter(fragmentManager) {

    override fun getItemPosition(item: Any?): Int = PagerAdapter.POSITION_NONE

    override fun getPageTitle(position: Int): CharSequence = navigationItems[position].title

    override fun getItem(position: Int): Fragment = navigationItems[position].fragment

    override fun getCount(): Int = navigationItems.size

}