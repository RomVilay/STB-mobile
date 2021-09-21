package com.example.applicationstb.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class Vehicule ( var Id: Int, var nom: String, var kilometrage: Int) : Parcelable {
}