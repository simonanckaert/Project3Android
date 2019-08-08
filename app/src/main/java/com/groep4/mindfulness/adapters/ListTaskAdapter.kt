package com.groep4.mindfulness.adapters


import android.app.Activity
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.amulyakhare.textdrawable.util.ColorGenerator
import com.groep4.mindfulness.R
import com.groep4.mindfulness.fragments.FragmentKalender
import com.groep4.mindfulness.utils.KalenderFunction
import java.util.ArrayList
import java.util.HashMap

/**
 *  Adapter voor het opvullen van de ListView
 */

class ListTaskAdapter(private val activity: Activity, private val data: ArrayList<HashMap<String, String>>) : BaseAdapter() {
    override fun getCount(): Int {
        return data.size
    }

    override fun getItem(position: Int): Any {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        var convertView = convertView
        var holder: ListTaskViewHolder? = null
        
        
        if (convertView == null) {
            holder = ListTaskViewHolder()
            convertView = LayoutInflater.from(activity).inflate(
                    R.layout.list_row, parent, false)
            holder.task_image = convertView!!.findViewById(R.id.task_image) as TextView
            holder.task_name = convertView.findViewById(R.id.task_name) as TextView
            holder.task_date = convertView.findViewById(R.id.task_date) as TextView
            convertView.tag = holder
        } else {
            holder = convertView.tag as ListTaskViewHolder
        }
        holder.task_image!!.id = position
        holder.task_name!!.id = position
        holder.task_date!!.id = position

        var mapValue = HashMap<String, String>()
        mapValue = data[position]

        try {
            holder.task_name!!.text = mapValue[FragmentKalender.KEY_TASK]
            holder.task_date!!.text = mapValue[FragmentKalender.KEY_DATE]
            val v : String = mapValue[FragmentKalender.KEY_DATE].toString()


            /**
             *  Kleur pijl genereren voor iedere task
             */

            val generator = ColorGenerator.MATERIAL
            val color = generator.getColor(getItem(position))
            holder.task_image!!.setTextColor(color)

            /**
             *  HTML code voor pijltje
             */
            holder.task_image!!.text = Html.fromHtml("&#x25BA;" )


        } catch (e: Exception) {
        }

        return convertView
    }
}

internal class ListTaskViewHolder {
    var task_image: TextView? = null
    var task_name: TextView? = null
    var task_date: TextView? = null
}