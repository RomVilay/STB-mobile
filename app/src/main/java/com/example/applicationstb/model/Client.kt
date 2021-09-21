package com.example.applicationstb.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class Client(val id:String, val enterprise: String, val telephone: Long?, val address: String?) : Parcelable {

}