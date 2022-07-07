package com.example.applicationstb.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
class Joint(var title: String?, var jointAvant: String?, var jointArriere: String?): Parcelable {
    override fun toString(): String {
        return "{\"title\":\"${title}\",\"jointAvant\":\"${jointAvant}\",\"jointArriere\":\"${jointArriere}\"}"
    }
}