package com.groep4.mindfulness.fragments

import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.groep4.mindfulness.R
import com.groep4.mindfulness.activities.MainActivity
import com.groep4.mindfulness.model.Feedback
import com.groep4.mindfulness.model.Oefening

class FragmentOefeningFeedback : Fragment() {
    private var txtOefeningNaam : TextView? = null
    private var buttonOpslaan : Button? = null
    private var ratingBar : RatingBar? = null
    private var ratingFeedback : Int = 5
    private var txtFeedback : TextInputEditText? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) : View {

        val view = inflater.inflate(R.layout.fragment_oefening_feedback, container, false)

        txtOefeningNaam = view.findViewById(R.id.textViewNaam) as TextView
        buttonOpslaan = view.findViewById(R.id.buttonBevestigen)
        ratingBar = view.findViewById(R.id.ratingBar) as RatingBar
        txtFeedback = view.findViewById(R.id.txtFeedback)

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        var oefening = arguments!!.getParcelable<Oefening>("oefening")

        buttonOpslaan!!.setOnClickListener {
                var feedback = Feedback(oefening.naam, oefening.sessieId.toString(), oefening.oefenigenId.toString(), txtFeedback!!.text.toString(), ratingFeedback.toString())
                (activity as MainActivity).postFeedback(feedback)
                (activity as MainActivity).onBackPressed()
            }

            ratingBar!!.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
                ratingFeedback = (rating * 2).toInt()
            }
        txtOefeningNaam!!.text = oefening.naam
    }
}