package com.groep4.mindfulness.views

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.widget.TextView

// Custom TextView om custom font te kunnen gebruiken
class LogoTextView : TextView {
    constructor(context: Context) : super(context) {
        setFont()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        setFont()
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        setFont()
    }

    private fun setFont() {
        val font = Typeface.createFromAsset(context.assets, "fonts/SCRIPTBL.TTF")
        setTypeface(font, Typeface.NORMAL)
    }
}