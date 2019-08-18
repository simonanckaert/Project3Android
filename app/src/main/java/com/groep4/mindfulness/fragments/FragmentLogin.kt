package com.groep4.mindfulness.fragments

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.TargetApi
import android.content.Intent
import android.support.constraint.Constraints.TAG
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.groep4.mindfulness.R
import com.groep4.mindfulness.activities.MainActivity
import com.groep4.mindfulness.model.Gebruiker
import com.groep4.mindfulness.utils.LoginValidation

import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.fragment_login.view.*


 class FragmentLogin : Fragment(){

    /**
     * Houd de inlogtaak bij om ervoor te zorgen dat we deze kunnen annuleren als hierom wordt gevraagd.
     */
    private val RC_SIGN_IN: Int = 1
    lateinit var mAuth: FirebaseAuth
    lateinit var db: FirebaseFirestore

     override fun onCreate(savedInstanceState: Bundle?) {
         super.onCreate(savedInstanceState)
     }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view:View=inflater!!.inflate(R.layout.fragment_login,container,false)

        // Stel het login formulier in.

        view.password.setOnEditorActionListener(TextView.OnEditorActionListener { _, id, _ ->
            if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                attemptLogin()
                return@OnEditorActionListener true
            }
            false
        })
        view.email
        view.email_sign_in_button.setOnClickListener { attemptLogin() }
        activity!!.tv_register.visibility = View.VISIBLE
        activity!!.tv_register.text = "Nog geen account? Klik hier om te registreren."
        activity!!.tv_register.isClickable
        return view
    }


    /**
     *Validatie en aanmelden van account
     */
    private fun attemptLogin() {

        // Reset errors.
        view!!.email.error = null
        view!!.password.error = null

        // values
        val emailStr = view!!.email.text.toString()
        val passwordStr = view!!.password.text.toString()

        var cancel = false
        var focusView: View? = null


        // Controleer op een geldig email adres.
        if (!LoginValidation.isValidEmail(view!!.email)) {

            focusView =view!!.email

            cancel = true
        }

        if (!LoginValidation.isValidPassword(view!!.password)) {

            focusView =view!!.password

            cancel = true
        }

        if (cancel) {

            focusView?.requestFocus()


        } else {
            //Aanmelden
            mAuth = FirebaseAuth.getInstance()
            mAuth.signInWithEmailAndPassword(emailStr,passwordStr)
                    .addOnCompleteListener(activity!!){
                    task ->
                        showProgress(true)
                        activity!!.tv_register.visibility = View.INVISIBLE
                        if (task.isSuccessful()) {
                            db = FirebaseFirestore.getInstance()
                            val userRef = db.collection("Users").document(mAuth.currentUser!!.uid )
                            userRef.get().addOnCompleteListener { document ->
                                if(document.result!!.exists()) {
                                    Toast.makeText(context!!, document.toString(), Toast.LENGTH_LONG)
                                    //mainactivity tonen
                                    val intent = Intent(activity, MainActivity::class.java)
                                    this.startActivity(intent)
                                    activity!!.finish()
                                } else {
                                    mAuth.signOut()
                                    activity!!.tv_register.visibility = View.VISIBLE
                                    view!!.email.error = "Dit account is niet meer geldig"
                                    view!!.email.requestFocus()
                                }
                            }
                        } else {
                            activity!!.tv_register.visibility = View.VISIBLE
                            view!!.password.error = this.getString(R.string.error_incorrect_password)
                            view!!.password.requestFocus()
                        }
                        showProgress(false)
            }
        }
    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    fun showProgress(show: Boolean) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            val shortAnimTime = resources.getInteger(android.R.integer.config_shortAnimTime).toLong()

            activity!!.login_layout.visibility = if (show) View.GONE else View.VISIBLE
            activity!!.login_layout.animate()
                    .setDuration(shortAnimTime)
                    .alpha((if (show) 0 else 1).toFloat())
                    .setListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator) {
                            activity!!.login_layout.visibility = if (show) View.GONE else View.VISIBLE
                        }
                    })

            activity!!.login_progress.visibility = if (show) View.VISIBLE else View.GONE
            activity!!.login_progress.animate()
                    .setDuration(shortAnimTime)
                    .alpha((if (show) 1 else 0).toFloat())
                    .setListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator) {
                            activity!!.login_progress.visibility = if (show) View.VISIBLE else View.GONE
                        }
                    })
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            activity!!.login_progress.visibility = if (show) View.VISIBLE else View.GONE
            activity!!.login_layout.visibility = if (show) View.GONE else View.VISIBLE
        }
    }

     override fun onResume() {
        super.onResume()
         showProgress(false)
    }

}
