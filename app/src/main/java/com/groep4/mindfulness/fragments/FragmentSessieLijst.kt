package com.groep4.mindfulness.fragments

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.badoualy.stepperindicator.StepperIndicator
import com.groep4.mindfulness.R
import com.groep4.mindfulness.activities.MainActivity
import com.groep4.mindfulness.adapters.SessiesPagerAdapter
import com.groep4.mindfulness.model.Sessie
import kotlinx.android.synthetic.main.fragment_kalender.view.*
import kotlinx.android.synthetic.main.fragment_sessie.*


class FragmentSessieLijst : Fragment() {

    var sessies: ArrayList<Sessie> = ArrayList()
    var previousPage: Int = 0
    private val handler = Handler()
    var imgBuildings: ArrayList<Int>? = null
    var imgMisc: ArrayList<Int>? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.fragment_sessie_lijst, container, false)
        val main: MainActivity = (activity as MainActivity)

        // (Statische) sessies toevoegen, in afwachting van DB
        addSessies()

        // Top bar info instellen
        view.tr_page.setBackgroundColor(ContextCompat.getColor(context!!, R.color.colorPrimary))
        view.tv_page.setText(R.string.sessies)

        val pager = view.findViewById<ViewPager>(R.id.pager_sessies)!!
        // offscreenpagelimit nodig zodat de pages niet telkens herladen worden bij het scrollen
        pager.offscreenPageLimit = 8
        val pagerAdapter = SessiesPagerAdapter(childFragmentManager, sessies)
        pager.adapter = pagerAdapter

        // Background images toevoegen aan arrays zodat de sessieviews ze kunnen gebruiken
        addBackgroundImages()

        // Bij de start van sessielistview de bus laten rijden
        // Bug: currentItem returns 0 on backpress sessiepage...
        handler.postDelayed({
            val sessieFragment: FragmentSessie = pagerAdapter.getRegisteredFragment(pager.currentItem) as FragmentSessie
            sessieFragment.drive(true)
        }, 15)

        pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {

                // Bij switchen van viewpages de bus forward of backward laten rijden naargelang de previous page
                val handler = Handler()
                handler.postDelayed({
                    val sessieFragmentCurrent: FragmentSessie = pagerAdapter.getRegisteredFragment(position) as FragmentSessie
                    val sessieFragmentPrevious: FragmentSessie = pagerAdapter.getRegisteredFragment(previousPage) as FragmentSessie
                    if (previousPage <= position) {
                        if (main.gebruiker!!.sessieId >= sessies[position].id){
                        sessieFragmentCurrent.drive(true)
                        sessieFragmentCurrent.fireworkAnimationSmall()
                        sessieFragmentCurrent.fireworkAnimationBig()}
                    }
                    else {
                        if (main.gebruiker!!.sessieId >= sessies[position].id) {
                            sessieFragmentCurrent.drive(false)
                            sessieFragmentPrevious.fireworkAnimationSmall()
                        }
                    }
                    sessieFragmentPrevious.setBusVisible(false)
                    previousPage = position
                }, 0)

                // als men bij de positie na de sessies komt, de finishview, vuurwerkgeluid afspelen
                if ( alleSessiesUnlocked()) {
                    if (sessies.size == position + 1) {
                        val sessieFragmentCurrent: FragmentSessie = pagerAdapter.getRegisteredFragment(position) as FragmentSessie
                        sessieFragmentCurrent.playSound()
                    }
                }

                // Als sessie locked is, cardview grijs maken
                if ( main.gebruiker!!.sessieId < sessies[position].id){
                    var sessieFragmentCurrent: FragmentSessie = pagerAdapter.getRegisteredFragment(position) as FragmentSessie
                    sessieFragmentCurrent.cv_sessie.setCardBackgroundColor(Color.parseColor("#818181"))
                }
            }
        })

        val indicator = view.findViewById(R.id.stepper_indicator) as StepperIndicator
        // We keep last page for a "finishing" page
        indicator.setViewPager(pager, false)
        indicator.addOnStepClickListener { step ->
            pager.setCurrentItem(step, true)
        }
        
        // Inflate
        return view
    }

    // Sessies toevoegen (static placeholders voor te testen)
    private fun addSessies() {
        val mainActivity = activity as MainActivity
        sessies = mainActivity.sessies

        // Indien DB niet bereikbaar is of DB telt minder dan 8 sessies, de lijst opvullen met lege sessies.
        if (!alleSessiesUnlocked()){
            while (sessies.size < 8) {
                sessies.add(Sessie(0, "Geen sessie gevonden.", "", ArrayList(), ""))
            }
        } else {
        while (sessies.size < 9) {
            sessies.add(Sessie(0, "Geen sessie gevonden.", "", ArrayList(), ""))
        }}
    }

    private fun addBackgroundImages(){
        imgBuildings = ArrayList()
        imgBuildings!!.add(R.mipmap.building00)
        imgBuildings!!.add(R.mipmap.building01)
        imgBuildings!!.add(R.mipmap.building02)
        imgBuildings!!.add(R.mipmap.building03)
        imgBuildings!!.add(R.mipmap.building04)
        imgBuildings!!.add(R.mipmap.building05)
        imgBuildings!!.add(R.mipmap.building06)

        imgMisc = ArrayList()
        imgMisc!!.add(R.mipmap.bush00)
        imgMisc!!.add(R.mipmap.bush01)
        imgMisc!!.add(R.mipmap.tree00)
        imgMisc!!.add(R.mipmap.tree01)
        imgMisc!!.add(R.mipmap.tree02)
    }

    /*
    Hierin wordt nagegaan of alle sessies unlocked zijn
     */
    private fun alleSessiesUnlocked(): Boolean {
        val main = activity as MainActivity
        if (main.gebruiker!!.sessieId == sessies.size){
            return true
        }
        return false
    }
}

