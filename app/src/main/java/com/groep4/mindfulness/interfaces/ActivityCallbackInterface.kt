package com.groep4.mindfulness.interfaces

import android.support.v4.app.Fragment

internal interface CallbackInterface {
    fun setFragment(fragment: Fragment, addToBackstack: Boolean)
}