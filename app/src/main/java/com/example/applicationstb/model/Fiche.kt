package com.example.applicationstb.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.util.*

@Parcelize
@Serializable
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
   @Contextual
   open var dateDebut: Date?,
   open var dureeTotale: Long?,
   open var observations: String?,
   open var photos: Array<String>?): Parcelable {
}