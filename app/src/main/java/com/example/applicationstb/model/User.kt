package com.example.applicationstb.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
class User(val _id: String, val nom: String?, val prenom: String?, val status: Int, val username: String, val password: String, var token: String?) : Parcelable {
}