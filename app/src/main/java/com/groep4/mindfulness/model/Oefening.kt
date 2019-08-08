package com.groep4.mindfulness.model

import android.os.Parcel
import android.os.Parcelable
import android.util.Log

class Oefening : Parcelable {
    var oefenigenId: Int = 0
    var sessieId: Int = 0
    var naam: String = ""
    var beschrijving: String = ""
    var groepen: String = ""


    // File
    var url: String = ""
    var fileMimeType: String = ""
    var fileOriginalName: String = ""
    var fileSize: String = ""
    var fileName: String = ""


    constructor(id: Int, naam: String, beschrijving: String, sId: Int, url: String,
                mimeType: String, groepen: String, fileOriginalName: String, fileSize: String, fileName: String){
        this.oefenigenId = id
        this.naam = naam
        this.beschrijving = beschrijving
        this.sessieId = sId
        this.url = url
        this.fileMimeType = mimeType
        this.groepen = groepen
        this.fileOriginalName = fileOriginalName
        this.fileSize = fileSize
        this.fileName = fileName
    }

    constructor()

    private constructor(parcel: Parcel) {
        this.oefenigenId = parcel.readInt()
        //Log.d("TAG", parcel.readString())
        this.sessieId = parcel.readInt()
        this.naam = parcel.readString().orEmpty()
        this.beschrijving = parcel.readString().orEmpty()

        this.url = parcel.readString().orEmpty()
        this.fileMimeType = parcel.readString().orEmpty()
        this.groepen = parcel.readString().orEmpty()
        this.fileOriginalName = parcel.readString().orEmpty()
        this.fileSize = parcel.readString().orEmpty()
        this.fileName = parcel.readString().orEmpty()
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeInt(oefenigenId)
        dest?.writeString(naam)
        dest?.writeString(beschrijving)
        dest?.writeInt(sessieId)
        dest?.writeString(url)
        dest?.writeString(fileMimeType)
        dest?.writeString(groepen)
        dest?.writeString(fileOriginalName)
        dest?.writeString(fileSize)
        dest?.writeString(fileName)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Oefening> {
        override fun createFromParcel(parcel: Parcel): Oefening {
            return Oefening(parcel)
        }

        override fun newArray(size: Int): Array<Oefening?> {
            return arrayOfNulls(size)
        }
    }
}