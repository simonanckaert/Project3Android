package com.groep4.mindfulness.adapters

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter


class ViewPagerAdapter(fm: FragmentManager): FragmentStatePagerAdapter(fm) {

    private val fragmentList: ArrayList<Fragment> = ArrayList()
    private val fragmentListTitles: ArrayList<String> = ArrayList()

    /**
     * Geeft fragment terug
     */
    override fun getItem(position: Int): Fragment {
        return fragmentList[position]
    }
    /**
     * Geeft aantal fragmenten terug
     */
    override fun getCount(): Int {
        return fragmentListTitles.count()
    }
    /**
     * Geeft fragmentTitel terug
     */
    override fun getPageTitle(position: Int): CharSequence {
        return fragmentListTitles[position]
    }
    /**
     * Voegt fragment toe aan de lijst
     */
    fun addFragment(fragment: Fragment, title: String){
        fragmentList.add(fragment)
        fragmentListTitles.add(title)
    }

}