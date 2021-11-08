package com.example.applicationstb.localdatabase

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.example.applicationstb.model.Chantier
import com.example.applicationstb.model.Client
import com.example.applicationstb.model.Section
import com.example.applicationstb.model.Vehicule
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.encodeToJsonElement
import java.util.*

@Entity (tableName="chantiers")
data class ChantierEntity(
    @PrimaryKey var _id:String,
    var numDevis:String?,
    var numFiche:String?,
    var status: Long?,
    var client: String,
    var contact: String?,
    var telContact: String?,
    var dateDebut: Date?,
    var dureeTotale: Long?,
    var observations: String?,
   // var photo: Array<String>?,
    var vehicule: String?,
    var adresseChantier:String?,
    var objet:String?,
    var materiel:String?,
    var diagnostic:String?,
    var signatureTech:String?,
    var signatureClient:String?
){
    @RequiresApi(Build.VERSION_CODES.O)
    fun toChantier() : Chantier{
        return Chantier(
            _id,
            numDevis,
            numFiche,
            1,
            status,
            Client(client,null,null,null, null),
            contact,
            telContact,
            null,
            null,
            dateDebut,
            dureeTotale!!.toLong(),
            observations,
            null,
            vehicule,
            adresseChantier,
            objet,
            materiel,
            diagnostic,
            signatureTech,
            signatureClient
        )
    }
}

class Converters {
    @TypeConverter
    fun fromDate(value:Long?) : Date? {
        return value?.let { Date(it) }
    }
    @TypeConverter
    fun dateToTimestamp(date: Date?) : Long? {
        return date?.time?.toLong()
    }

    @TypeConverter
    fun arrayToJSON(value: Array<String>?) : String? {
        return value!!.joinToString( separator = ",")
    }
    @TypeConverter
    fun arrayFromJSON(value:String) : Array<String> {
        var tab: Array<String> = value.split(",").map { it }.toTypedArray()
        return tab
    }
    @TypeConverter
    fun fromSectionList (value: MutableList<Section>) = Json.encodeToString(value)

    @TypeConverter
    fun toSectionList (value: String) = Json.decodeFromString<MutableList<Section>>(value)




}
