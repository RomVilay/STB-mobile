package com.example.applicationstb.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
open class Fiche(
   open var _id:String,
   open var numDevis: String?,
   open var numFiche: String?,
   open var type: Long?,
   open var status: Long?,
   open var client: Client?,
   open var contact: String?,
   open var telContact: String?,
   open var techniciens: Array<User>?,
   open val resp: User?,
   open var dateDebut: Date?,
   open var dureeTotale: Long?,
   open var observations: String?,
   open var photo: Array<String>?): Parcelable {
}