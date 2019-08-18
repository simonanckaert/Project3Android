package com.groep4.mindfulness.fragments

import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.util.Log
import android.view.*
import com.groep4.mindfulness.R
import com.groep4.mindfulness.adapters.ViewPagerAdapter
import kotlinx.android.synthetic.main.fragment_profiel.view.*

class FragmentProfiel: Fragment() {

    private var tabLayout: TabLayout? = null
    private var appBarLayout: AppBarLayout? = null
    private lateinit var viewPager: ViewPager
    private var fragmentAdapter : ViewPagerAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_profiel, container, false)

        tabLayout = view.findViewById(R.id.tablayout_id)
        appBarLayout = view.findViewById(R.id.appbarid)
        viewPager = view.findViewById(R.id.viewpager_id)

        fragmentAdapter = ViewPagerAdapter(childFragmentManager)
        // adding fragments
        fragmentAdapter!!.addFragment(FragmentProfielInfo(),"Info")
        fragmentAdapter!!.addFragment(FragmentProfielOverzicht(), "Voortgang")

        //adapter setup
        view.viewpager_id.adapter = fragmentAdapter
        view.tablayout_id.setupWithViewPager(view.viewpager_id)

        return view
    }

}