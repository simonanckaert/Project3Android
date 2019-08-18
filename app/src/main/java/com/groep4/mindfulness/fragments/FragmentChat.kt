package com.groep4.mindfulness.fragments


import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.firebase.ui.database.FirebaseListAdapter
import com.firebase.ui.database.FirebaseListOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import com.groep4.mindfulness.R
import com.groep4.mindfulness.activities.MainActivity
import com.groep4.mindfulness.interfaces.CallbackInterface
import com.groep4.mindfulness.model.Message
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_chat.view.*
import java.text.DateFormat
import java.util.*

class FragmentChat : Fragment(){

    //Layout objecten
    private var listView: ListView? = null
    private var btnSend: View? = null
    private var editText: EditText? = null
    //Firebase objecten
    private var adapter: ArrayAdapter<String>? = null
    private var db : FirebaseFirestore = FirebaseFirestore.getInstance()
    private var currentUserId: FirebaseUser? = FirebaseAuth.getInstance().currentUser!!

    private var messagesFirebase : MutableList<Message> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_chat, container, false)

        // Top bar info instellen
        view.tr_page.setBackgroundColor(ContextCompat.getColor(context!!, R.color.colorGreen))
        view.tv_page.setText(R.string.contact)

        view.contactUser.text = "Psycholoog"

        btnSend = view.findViewById(R.id.btn_chat_send)

        displayChatMessages(view)

        /** Bericht sturen naar de database met als sleutel het userid van de huidig ingelogde gebruiker. */
        btnSend!!.setOnClickListener {
            editText = view.findViewById(R.id.msg_type)
            val naam = (activity as MainActivity)!!.gebruiker.name
            var message = Message()
            message.content = editText!!.text.toString()
            message.gelezen = false
            message.messageTime = Date().time
            message.messageUser = naam.toString()
            //var map = mapOf("messages" to messagesFirebase)
            db!!.collection("chat").document(currentUserId!!.uid)
                    .collection("messages").document((this.messagesFirebase.size+1).toString()).set(message)


            editText!!.setText("")
            displayChatMessages(view)
        }
        //displayChatMessages(view)

        return view
    }

    override fun onStart() {
        super.onStart()
        //adapter!!.startListening()
    }

    override fun onStop() {
        super.onStop()
        //adapter!!.
    }

    /**
     * De listAdapter zorgt ervoor dat de List opgevuld raakt met chatberichten opgehaald uit de database met de huidige gebruiker's userid als argument.
     */
    private fun displayChatMessages(view: View) {

        listView = view.findViewById<View>(R.id.list_msg) as ListView

        this.messagesFirebase = mutableListOf()
        db.collection("chat").document(currentUserId!!.uid).collection("messages").get().addOnCompleteListener { task ->
            for(document in task.result!!) {
                var message = Message()
                message.content = document["content"].toString()
                message.gelezen = document["gelezen"].toString().equals("true")
                message.messageTime = document["messageTime"].toString().toLong()
                message.messageUser = document["messageUser"].toString()
                this.messagesFirebase.add(this.messagesFirebase.count(), message)
            }
            this.messagesFirebase.sortBy { message -> message.messageTime }

            var listItems = arrayOfNulls<String>(this.messagesFirebase.size)
            for(i in 0 until this.messagesFirebase.size) {
                listItems[i] = this.messagesFirebase[i].content + " - " + this.messagesFirebase[i].messageUser
            }

            adapter =  ArrayAdapter(context!!, android.R.layout.simple_list_item_1, listItems.requireNoNulls())

            listView!!.adapter = adapter
        }


             /*override fun getView(position: Int, v: View, model: Message) {
                val messageText = v.findViewById(R.id.message_text) as TextView
                val messageUser = v.findViewById(R.id.message_user) as TextView
                val messageTime = v.findViewById(R.id.message_time) as TextView

                val messageBackground = v.findViewById(R.id.message_background) as RelativeLayout
                messageBackground.setBackgroundColor(Color.WHITE)
                /**
                 * Als het bericht in de db niet matcht met de huidige gebruiker, zet de achtergrond blauw.
                 * */
                if(model.messageUser != currentUserId!!.displayName!!){
                    messageBackground.setBackgroundColor(Color.parseColor("#9BBBD8"))
                }

                messageText.text = model.content
                messageTime.text = DateFormat.getDateInstance().format(model.messageTime)
                messageUser.text = model.messageUser
            }*/





    }




}
