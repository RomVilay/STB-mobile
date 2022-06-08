package com.example.applicationstb.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
class Roulement(var title: String?, var roulementAvant: String?, var roulementArriere: String?):
    Parcelable {
    override fun toString(): String {
        return "{\"title\":\"${title}\",\"roulementAvant\":\"${roulementAvant}\",\"roulementArriere\":\"${roulementArriere}\"}"
    }
}