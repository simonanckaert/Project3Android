package com.groep4.mindfulness.model

import android.os.Parcel
import android.os.Parcelable

class Gebruiker : Parcelable {
    var uid: String? = ""
    var email : String? = ""
    var regio : String? = ""
    var telnr : String? = ""
    var groepsnr : Int? = 0
    var name : String? = ""
    var sessieId : Int = 1


    constructor(uid : String, email : String, regio : String, telnr : String, groepsnr : Int, name : String, sessieId : Int) {
        this.email = email
        this.uid = uid
        this.regio = regio
        this.telnr = telnr
        this.groepsnr = groepsnr
        this.name = name
        this.sessieId = sessieId
    }

    constructor()

    private constructor(parcel: Parcel) {
        this.uid = parcel.readString()
        this.email = parcel.readString()
        this.regio = parcel.readString()
        this.telnr = parcel.readString()
        this.groepsnr = parcel.readInt()
        this.name = parcel.readString()
        this.sessieId = parcel.readInt()
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeString(uid)
        dest?.writeString(email)
        dest?.writeString(regio)
        dest?.writeString(telnr)
        dest?.writeInt(groepsnr!!)
        dest?.writeString(name)
        dest?.writeInt(sessieId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Gebruiker> {
        override fun createFromParcel(parcel: Parcel): Gebruiker {
            return Gebruiker(parcel)
        }

        override fun newArray(size: Int): Array<Gebruiker?> {
            return arrayOfNulls(size)
        }
    }
}