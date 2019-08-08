package com.groep4.mindfulness.activities

import kotlinx.coroutines.*
import android.content.Intent
import android.os.Bundle
import android.os.Parcel
import android.os.PersistableBundle
import android.support.v4.R.id.async
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.groep4.mindfulness.R
import com.groep4.mindfulness.fragments.*
import com.groep4.mindfulness.interfaces.CallbackInterface
import com.groep4.mindfulness.model.Gebruiker
import com.groep4.mindfulness.model.Oefening
import com.groep4.mindfulness.model.Sessie
import com.groep4.mindfulness.utils.ExtendedDataHolder
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class MainActivity : AppCompatActivity(), CallbackInterface {

    private val BACK_STACK_ROOT_TAG = "root_fragment"
    private val client = OkHttpClient()

    lateinit var mAuth: FirebaseAuth
    lateinit var db: FirebaseFirestore

    var gebruiker : Gebruiker = Gebruiker()
    var sessies: ArrayList<Sessie> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mAuth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        val settings = FirebaseFirestoreSettings.Builder()
            .setTimestampsInSnapshotsEnabled(true)
            .build();
        db.setFirestoreSettings(settings);

        Logger.addLogAdapter(AndroidLogAdapter())

        // Set gebruiker
        if(this.gebruiker == null) {
            getAangemeldeGebruiker()
        }

        // Sessies
        sessies = getSessiesFromDB()
        val extras = ExtendedDataHolder.getInstance()
        extras.putExtra("sessielist", sessies)


        //Set no new fragment if there already is one
        if (savedInstanceState == null) {
            setFragment(FragmentMain(), false)
        }
    }

    /**
     * Om fragment te tonen
     */
    override fun setFragment(fragment: Fragment, addToBackstack: Boolean) {
        if (addToBackstack)
            supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragment_holder_main, fragment, "pageContent")
                    .addToBackStack(BACK_STACK_ROOT_TAG)
                    .commit()
        else
            supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragment_holder_main, fragment, "pageContent")
                    .commit()
    }

    /**
     * ActionMenu aanmaken
     */
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    /**
     * Sessies ophalen
     */
    fun getSessiesFromDB(): ArrayList<Sessie> {
        val tempSessies: ArrayList<Sessie> = ArrayList();

        db.collection("sessies").get().addOnCompleteListener { task ->
            for(document in task.result!!) {
                var sessie = Sessie()
                sessie.id = (document["id"] as Long).toInt()
                sessie.naam = document["naam"] as String
                sessie.oefeningen = getOefeningen(document["oefeningen"] as ArrayList<HashMap<String,Any>>)
                sessie.beschrijving = document["beschrijving"] as String
                sessie.sessieCode = document["sessieCode"] as String
                tempSessies.add(sessie)
            }
        }

        return tempSessies;
    }

    /**
     * Aangemelde gebruiker ophalen
     */
    fun getAangemeldeGebruiker() : Gebruiker {
        val id = mAuth.currentUser!!.uid

        db.collection("Users").document(id).get().addOnCompleteListener { task ->
            this.gebruiker = task.result!!.toObject(Gebruiker::class.java)!!
        }

        return this.gebruiker;
    }

    /**
     * Oefeningen van sessie ophalen
     */
    fun getOefeningen(oefeningen : ArrayList<HashMap<String,Any>>): ArrayList<Oefening>{
        val tempOefeningen: ArrayList<Oefening> = ArrayList();
        for(oef in oefeningen) {
            if(oef["groepen"].toString().contains(gebruiker.groepsnr!!.toChar())) {
                var oefening = Oefening();
                oefening.oefenigenId = (oef["oefenigenId"] as Long).toInt()
                oefening.naam = oef["naam"].toString()
                oefening.beschrijving = oef["beschrijving"].toString()
                oefening.sessieId = (oef["Id"] as Long).toInt()
                oefening.url = oef["url"].toString()
                oefening.fileMimeType = oef["mimeType"].toString()
                oefening.groepen = oef["groepen"].toString()
                oefening.fileOriginalName = oef["fileOriginalName"].toString()
                oefening.fileSize = oef["fileSize"].toString()
                oefening.fileName = oef["fileName"].toString()
                tempOefeningen.add(oefening)
            }
        }
        return tempOefeningen
        /*val oefeningen: ArrayList<Oefening> = ArrayList()

        // HTTP Request oefeningen
        val request = Request.Builder()
                /*.header("Authorization", "token abcd")*/
                .url("http://141.134.155.219:3000/oefeningen/$sessieId")
                .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("ERROR", "HTTP request failed: $e")
            }

            override fun onResponse(call: Call, response: Response) {
                val jsonarray = JSONArray(response.body()!!.string())
                for (i in 0 until jsonarray.length()) {
                    val jsonobject = jsonarray.getJSONObject(i)
                    val oefeningenId = jsonobject.getInt("oefeningId")
                    val naam = jsonobject.getString("naam")
                    val beschrijving = jsonobject.getString("beschrijving")
                    val sessieid = jsonobject.getInt("sessieId")
                    val fileUrl = jsonobject.getString("fileName")
                    val fileMimeType = jsonobject.getString("fileMimetype")
                    val groepen = jsonobject.getString("groepen")

                    if(groepen.contains(gebruiker!!.groepsnr.toString())) {
                        //val oefening: Oefening = Oefening(oefeningenId, naam, beschrijving, sessieid, fileUrl, fileMimeType, groepen)
                        //oefeningen.add(oefening)
                    }
                }
            }
        })
        return oefeningen*/
    }

    /**
     * Terugkeerknop
     */
    override fun onBackPressed() {
        super.onBackPressed()
    }

    /**
     * gegevens van de gebruiker bewerken
     */
    fun veranderGegevensGebruiker(gebruikersnaam : String, regio : String, telnr : String) : Gebruiker {
        gebruiker!!.name = gebruikersnaam
        gebruiker!!.regio = regio
        gebruiker!!.telnr = telnr
        return gebruiker!!
    }

    /**
     * gegevens van de gebruiker opslaan
     */
    fun gegevensGebruikerOpslaan(gebruiker : Gebruiker)  {
        db.collection("Users").document(gebruiker.uid!!).set(gebruiker)
    }

    /**
     * Opent menu om naar FragmentProfiel te gaan of uit te loggen
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle presses on the action bar menu items
        when (item.itemId) {
            R.id.action_logout -> {

                val builder = AlertDialog.Builder(this@MainActivity)
                builder.setMessage("Wil je uitloggen ?")

                builder.setPositiveButton("Ja"){dialog, which ->
                    FirebaseAuth.getInstance().signOut()
                    Toast.makeText(this,"Account uitgelogd", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, ActivityLogin::class.java)
                    this.startActivity(intent)
                    finish()
                }

                builder.setNegativeButton("Nee"){dialog,which ->
                }

                val dialog: AlertDialog = builder.create()
                dialog.show()


                return true
            }

            R.id.action_profiel -> {
                supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.fragment_holder_main, FragmentProfiel(), "pageContent")
                        .addToBackStack(BACK_STACK_ROOT_TAG)
                        .commit()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * Slaat de feedback op
     */
    fun postFeedback(url: String, body:FormBody): String {
        var response2 : String? = null
        val thread = Thread(Runnable {
            val mediaType: MediaType? = MediaType.parse("application/json; charset=utf-8")
            val client: OkHttpClient = OkHttpClient()
            //val body: RequestBody = RequestBody.create(mediaType, json)
            val request: Request = Request.Builder().url(url).post(body).build()
            val response = client.newCall(request).execute()
            response2 = response.body().toString()
        })
        thread.start()
        return response2.orEmpty()
    }


    /**
     * Voegt de unlockte sessie toe aan de gebruiker
     */
    fun sessieUnlocked() {
        gebruiker!!.sessieId += 1
        gegevensGebruikerOpslaan(gebruiker)
    }
}