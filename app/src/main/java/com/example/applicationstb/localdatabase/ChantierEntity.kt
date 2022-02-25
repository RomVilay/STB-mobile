package com.example.applicationstb.localdatabase

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.example.applicationstb.model.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.encodeToJsonElement
import java.time.LocalDateTime
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
    var photos: Array<String>?,
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
            photos,
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
    @RequiresApi(Build.VERSION_CODES.O)
    @TypeConverter
    fun fromLocalDateTime(value:String?) : LocalDateTime? {
        return value?.let { LocalDateTime.parse(it) }
    }
    @TypeConverter
    fun LocalDateTimeToString(date: LocalDateTime?) : String? {
        return date?.toString()
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

    @TypeConverter
    fun fromRoulementList ( value: MutableList<Roulement>) = Json.encodeToString(value)

    @TypeConverter
    fun toRoulementList (value: String) = Json.decodeFromString<MutableList<Roulement>>(value)

    @TypeConverter
    fun fromJointList ( value: MutableList<Joint>) = Json.encodeToString(value)

    @TypeConverter
    fun toJointList (value: String) = Json.decodeFromString<MutableList<Joint>>(value)


}
