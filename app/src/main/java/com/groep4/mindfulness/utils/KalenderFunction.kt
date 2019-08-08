package com.groep4.mindfulness.utils

import java.lang.Long
import java.text.SimpleDateFormat
import java.util.*


class KalenderFunction {

    fun Epoch2DateString(epochSeconds: String, formatString: String): String {
        val updatedate = Date(Long.parseLong(epochSeconds))
        val format = SimpleDateFormat(formatString)
        return format.format(updatedate)
    }


    fun Epoch2Calender(epochSeconds: String): Calendar {
        val updatedate = Date(java.lang.Long.parseLong(epochSeconds))
        val cal = Calendar.getInstance()
        cal.setTime(updatedate)

        return cal
    }

}