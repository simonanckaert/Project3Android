package com.groep4.mindfulness.model

import android.os.Parcel
import android.os.Parcelable

class Feedback : Parcelable{
    var id: String? = ""
    var oefeningId: String? = ""
    var sessieId: String? = ""
    var beschrijving: String? = ""
    var ratingFeedback: String? = ""

    constructor(id: String, oefeningId: String, sessieId: String, beschrijving: String, ratingFeedback: String) {
        this.id = id
        this.oefeningId = oefeningId
        this.sessieId = sessieId
        this.beschrijving = beschrijving
        this.ratingFeedback = ratingFeedback
    }

    constructor()

    private constructor(parcel: Parcel) {
        this.id = parcel.readString()
        this.oefeningId = parcel.readString()
        this.sessieId = parcel.readString()
        this.beschrijving = parcel.readString()
        this.ratingFeedback = parcel.readString()
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeString(id)
        dest?.writeString(oefeningId)
        dest?.writeString(sessieId)
        dest?.writeString(beschrijving)
        dest?.writeString(ratingFeedback!!)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Feedback> {
        override fun createFromParcel(parcel: Parcel): Feedback {
            return Feedback(parcel)
        }

        override fun newArray(size: Int): Array<Feedback?> {
            return arrayOfNulls(size)
        }
    }
}