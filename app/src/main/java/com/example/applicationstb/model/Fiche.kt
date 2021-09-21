package com.example.applicationstb.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
open class Fiche(
   open val _id:String,
   open val numDevis: String,
   open val numFiche: String,
   open val type: Long,
   open val status: Long,
   open val client: Client,
   open val contact: String?,
   open val telContact: String?,
   open val techniciens: Array<User>?,
   open val resp: User?,
   open  val dateDebut: Date?,
   open val dureeTotale: Long?,
   open val observation: String?,
   open val photo: Array<String>?): Parcelable {
}