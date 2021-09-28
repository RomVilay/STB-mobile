package com.example.applicationstb.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
class Vehicule ( var _id: String, var nom: String?, var kilometrage: Int?) : Parcelable {
    override fun toString(): String {
        return "{\"id\":\"${_id}\",\"nom\":\"${nom}\",\"kilometrage\":${kilometrage}}"
    }
}