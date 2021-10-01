package com.example.applicationstb.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
class Client(val _id:String, val enterprise: String?, val telephone: String?, val address: String?) : Parcelable {
    override fun toString(): String {
        return "{\"id\":\"${_id}\",\"enterprise\":\"${enterprise}\",\"telephone\":${telephone},\"address\":${address}}"
    }
}