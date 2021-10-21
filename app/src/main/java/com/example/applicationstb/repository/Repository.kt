package com.example.applicationstb.repository

import android.content.Context
import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.room.Room
import com.example.applicationstb.localdatabase.*
import com.example.applicationstb.model.*
import com.squareup.moshi.*
import okhttp3.OkHttpClient
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


class BodyLogin(var username: String?, var password: String?): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(username)
        parcel.writeString(password)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BodyLogin> {
        override fun createFromParcel(parcel: Parcel): BodyLogin {
            return BodyLogin(parcel)
        }

        override fun newArray(size: Int): Array<BodyLogin?> {
            return arrayOfNulls(size)
        }
    }
}

class BodyChantier(var materiel: String?, var objet: String?, var observations: String?, var status: Long?, var dureeTotale: Long?): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readLong(),
        parcel.readLong()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(materiel)
        parcel.writeString(objet)
        parcel.writeString(observations)
        parcel.writeLong(status!!)
        parcel.writeLong(dureeTotale!!)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BodyChantier> {
        override fun createFromParcel(parcel: Parcel): BodyChantier {
            return BodyChantier(parcel)
        }

        override fun newArray(size: Int): Array<BodyChantier?> {
            return arrayOfNulls(size)
        }
    }
}

class BodyBobinage(var marqueMoteur : String?,
    var typeBobinage: String?,
    var vitesse:Float?,
    var puissance:Float?,
    var phases:Long?,
    var frequences: Float?,
    var courant: Float?,
    var nbSpires: Long?,
    var resistanceU: Float?,
    var resistanceV: Float?,
    var resistanceW: Float?,
    var isolementUT: Float?,
    var isolementVT: Float?,
    var isolementWT: Float?,
    var isolementUV: Float?,
    var isolementUW: Float?,
    var isolementVW: Float?,
    var status: Long?,
    var calageEncoches: Boolean?,
    var sectionsFils: List<Section>? ,
    var observations: String?,
    var poids:Float?,
    var tension:Long?,
    var dureeTotale: Long?
                  ): Parcelable {
    @RequiresApi(Build.VERSION_CODES.Q)
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readLong(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readLong(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readLong(),
        parcel.readBoolean(),
        listOf<Section>().apply {
            parcel.readList(this,Section::class.java.classLoader)
        },
        parcel.readString(),
        parcel.readFloat(),
        parcel.readLong(),
        parcel.readLong(),
    ) {
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(marqueMoteur!!)
        parcel.writeString(typeBobinage!!)
        parcel.writeFloat(vitesse!!)
        parcel.writeFloat(puissance!!)
        parcel.writeLong(phases!!)
        parcel.writeFloat(frequences!!)
        parcel.writeFloat(courant!!)
        parcel.writeLong(nbSpires!!)
        parcel.writeFloat(resistanceU!!)
        parcel.writeFloat(resistanceV!!)
        parcel.writeFloat(resistanceW!!)
        parcel.writeFloat(isolementUT!!)
        parcel.writeFloat(isolementVT!!)
        parcel.writeFloat(isolementWT!!)
        parcel.writeFloat(isolementUV!!)
        parcel.writeFloat(isolementUW!!)
        parcel.writeFloat(isolementVW!!)
        parcel.writeLong(status!!)
        parcel.writeBoolean(calageEncoches!!)
        listOf<Section>().apply {
            parcel.writeList(this)
        }
        parcel.writeString(observations!!)
        parcel.writeFloat(poids!!)
        parcel.writeLong(tension!!)
        parcel.writeLong(dureeTotale!!)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BodyBobinage> {
        @RequiresApi(Build.VERSION_CODES.Q)
        override fun createFromParcel(parcel: Parcel): BodyBobinage {
            return BodyBobinage(parcel)
        }

        override fun newArray(size: Int): Array<BodyBobinage?> {
            return arrayOfNulls(size)
        }
    }
}

class BodyDemontageTriphase (
                      var status: Long?,
                      var typeMoteur: String?,
                      var marque: String?,
                      var numSerie: Int?,
                      var puissance: Float?,
                      var bride: Float?,
                      var vitesse : Float?,
                      var arbreSortantEntrant:Boolean?, //arbre sortant ou rentrant
                      var accouplement:Boolean?,
                      var coteAccouplement:String?,
                      var clavette: Boolean?,
                      var aspect:Int?,
                      var aspectInterieur:Int?,
                      var couplage:String?,
                      var flasqueAvant: Int?,
                      var flasqueArriere: Int?,
                      var porteeRAvant: Int?,
                      var porteeRArriere:  Int?,
                      var boutArbre: Boolean?,
                      var rondelleElastique: Boolean?,
                      var refRoulementAvant: Array<String>?,
                      var refRoulementArriere: Array<String>?,
                      var typeRoulementAvant: Array<String>?,
                      var typeRoulementArriere: Array<String>?,
                      var refJointAvant: String?,
                      var refJointArriere: String?,
                      var typeJointAvant: Boolean?,
                      var typeJointArriere: Boolean?,
                      var ventilateur: Int?,
                      var capotV: Int?,
                      var socleBoiteABorne : Int?,
                      var capotBoiteABorne : Int?,
                      var plaqueABorne : Int?,
                      var presenceSondes: Boolean ?,
                      var typeSondes: String?,
                      var equilibrage: Boolean?,
                      var peinture : String?,
                      var isolementPhaseMasseStatorUM: Int?,
                      var isolementPhaseMasseStatorVM: Int?,
                      var isolementPhaseMasseStatorWM: Int?,
                      var isolementPhasePhaseStatorUV: Int?,
                      var isolementPhasePhaseStatorVW: Int?,
                      var isolementPhasePhaseStatorUW: Int?,
                      var resistanceStatorU: Int?,
                      var resistanceStatorV: Int?,
                      var resistanceStatorW: Int?,
                      var tensionU: Int?,
                      var tensionV: Int?,
                      var tensionW: Int?,
                      var intensiteU: Int?,
                      var intensiteV: Int?,
                      var intensiteW: Int?,
                      var dureeEssai: Int?,
                      var dureeTotale: Int?,
                      var observations: String?
): Parcelable {
    @RequiresApi(Build.VERSION_CODES.Q)
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readBoolean(),
        parcel.readBoolean(),
        parcel.readString(),
        parcel.readBoolean(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readBoolean(),
        parcel.readBoolean(),
        arrayOf<String>().apply {
            parcel.readArray(String::class.java.classLoader)
        },
        arrayOf<String>().apply {
            parcel.readArray(String::class.java.classLoader)
        },
        arrayOf<String>().apply {
            parcel.readArray(String::class.java.classLoader)
        },
        arrayOf<String>().apply {
            parcel.readArray(String::class.java.classLoader)
        },
        parcel.readString(),
        parcel.readString(),
        parcel.readBoolean(),
        parcel.readBoolean(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readBoolean(),
        parcel.readString(),
        parcel.readBoolean(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString()
    ) {
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(status!!)
        parcel.writeString(typeMoteur!!)
        parcel.writeString(marque!!)
        parcel.writeInt(numSerie!!)
        parcel.writeFloat(puissance!!)
        parcel.writeFloat(bride!!)
        parcel.writeFloat(vitesse!!)
        parcel.writeBoolean(arbreSortantEntrant!!)
        parcel.writeBoolean(accouplement!!)
        parcel.writeString(coteAccouplement!!)
        parcel.writeBoolean(clavette!!)
        parcel.writeInt(aspect!!)
        parcel.writeInt(aspectInterieur!!)
        parcel.writeString(couplage!!)
        parcel.writeInt(flasqueAvant!!)
        parcel.writeInt(flasqueArriere!!)
        parcel.writeInt(porteeRAvant!!)
        parcel.writeInt(porteeRArriere!!)
        parcel.writeBoolean(boutArbre!!)
        parcel.writeBoolean(rondelleElastique!!)
        arrayOf<String>().apply {
            parcel.writeArray(refRoulementAvant)
        }
        arrayOf<String>().apply {
            parcel.writeArray(refRoulementArriere)
        }
        arrayOf<String>().apply {
            parcel.writeArray(typeRoulementAvant)
        }
        arrayOf<String>().apply {
            parcel.writeArray(typeRoulementArriere)
        }
        parcel.writeString(refJointAvant!!)
        parcel.writeString(refJointArriere!!)
        parcel.writeBoolean(typeJointAvant!!)
        parcel.writeBoolean(typeJointArriere!!)
        parcel.writeInt(ventilateur!!)
        parcel.writeInt(capotV!!)
        parcel.writeInt(socleBoiteABorne!!)
        parcel.writeInt(capotBoiteABorne!!)
        parcel.writeInt(plaqueABorne!!)
        parcel.writeBoolean(presenceSondes!!)
        parcel.writeString(typeSondes!!)
        parcel.writeBoolean(equilibrage!!)
        parcel.writeString(peinture!!)
        parcel.writeInt(isolementPhaseMasseStatorUM!!)
        parcel.writeInt(isolementPhaseMasseStatorVM!!)
        parcel.writeInt(isolementPhaseMasseStatorWM!!)
        parcel.writeInt(isolementPhasePhaseStatorUV!!)
        parcel.writeInt(isolementPhasePhaseStatorVW!!)
        parcel.writeInt(isolementPhasePhaseStatorUW!!)
        parcel.writeInt(resistanceStatorU!!)
        parcel.writeInt(resistanceStatorV!!)
        parcel.writeInt(resistanceStatorW!!)
        parcel.writeInt(tensionU!!)
        parcel.writeInt(tensionV!!)
        parcel.writeInt(tensionW!!)
        parcel.writeInt(intensiteU!!)
        parcel.writeInt(intensiteV!!)
        parcel.writeInt(intensiteW!!)
        parcel.writeInt(dureeEssai!!)
        parcel.writeInt(dureeTotale!!)
        parcel.writeString(observations!!)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BodyDemontageTriphase> {
        @RequiresApi(Build.VERSION_CODES.Q)
        override fun createFromParcel(parcel: Parcel): BodyDemontageTriphase {
            return BodyDemontageTriphase(parcel)
        }

        override fun newArray(size: Int): Array<BodyDemontageTriphase?> {
            return arrayOfNulls(size)
        }
    }
}

class BodyDemontageCC ( var status: Long?,
                        var typeMoteur: String?,
                        var marque: String?,
                        var numSerie: Int?,
                        var puissance: Float?,
                        var bride: Float?,
                        var vitesse : Float?,
                        var arbreSortantEntrant:Boolean?, //arbre sortant ou rentrant
                        var accouplement:Boolean?,
                        var coteAccouplement:String?,
                        var clavette: Boolean?,
                        var aspect:Int?,
                        var aspectInterieur:Int?,
                        var couplage:String?,
                        var flasqueAvant: Int?,
                        var flasqueArriere: Int?,
                        var porteeRAvant: Int?,
                        var porteeRArriere:  Int?,
                        var boutArbre: Boolean?,
                        var rondelleElastique: Boolean?,
                        var refRoulementAvant: Array<String>?,
                        var refRoulementArriere: Array<String>?,
                        var typeRoulementAvant: Array<String>?,
                        var typeRoulementArriere: Array<String>?,
                        var refJointAvant: String?,
                        var refJointArriere: String?,
                        var typeJointAvant: Boolean?,
                        var typeJointArriere: Boolean?,
                        var ventilateur: Int?,
                        var capotV: Int?,
                        var socleBoiteABorne : Int?,
                        var capotBoiteABorne : Int?,
                        var plaqueABorne : Int?,
                        var presenceSondes: Boolean ?,
                        var typeSondes: String?,
                        var equilibrage: Boolean?,
                        var peinture : String?,
                        var isolationMasseInduit: Int?,
                        var isolationMassePolesPrincipaux: Int?,
                        var isolationMassePolesAuxilliaires: Int?,
                        var isolationMassePolesCompensatoires: Int?,
                        var isolationMassePorteBalais: Int?,
                        var resistanceInduit: Int?,
                        var resistancePP: Int?,
                        var resistancePA: Int?,
                        var resistancePC: Int?,
                        var tensionInduit: Int?,
                        var intensiteInduit: Int?,
                        var tensionExcitation: Int?,
                        var intensiteExcitation: Int?,
                        var dureeTotale: Int?,
                        var observations: String?
): Parcelable {
    @RequiresApi(Build.VERSION_CODES.Q)
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readBoolean(),
        parcel.readBoolean(),
        parcel.readString(),
        parcel.readBoolean(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readBoolean(),
        parcel.readBoolean(),
        arrayOf<String>().apply {
            parcel.readArray(String::class.java.classLoader)
        },
        arrayOf<String>().apply {
            parcel.readArray(String::class.java.classLoader)
        },
        arrayOf<String>().apply {
            parcel.readArray(String::class.java.classLoader)
        },
        arrayOf<String>().apply {
            parcel.readArray(String::class.java.classLoader)
        },
        parcel.readString(),
        parcel.readString(),
        parcel.readBoolean(),
        parcel.readBoolean(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readBoolean(),
        parcel.readString(),
        parcel.readBoolean(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString()
    ) {
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(status!!)
        parcel.writeString(typeMoteur!!)
        parcel.writeString(marque!!)
        parcel.writeInt(numSerie!!)
        parcel.writeFloat(puissance!!)
        parcel.writeFloat(bride!!)
        parcel.writeFloat(vitesse!!)
        parcel.writeBoolean(arbreSortantEntrant!!)
        parcel.writeBoolean(accouplement!!)
        parcel.writeString(coteAccouplement!!)
        parcel.writeBoolean(clavette!!)
        parcel.writeInt(aspect!!)
        parcel.writeInt(aspectInterieur!!)
        parcel.writeString(couplage!!)
        parcel.writeInt(flasqueAvant!!)
        parcel.writeInt(flasqueArriere!!)
        parcel.writeInt(porteeRAvant!!)
        parcel.writeInt(porteeRArriere!!)
        parcel.writeBoolean(boutArbre!!)
        parcel.writeBoolean(rondelleElastique!!)
        arrayOf<String>().apply {
            parcel.writeArray(refRoulementAvant)
        }
        arrayOf<String>().apply {
            parcel.writeArray(refRoulementArriere)
        }
        arrayOf<String>().apply {
            parcel.writeArray(typeRoulementAvant)
        }
        arrayOf<String>().apply {
            parcel.writeArray(typeRoulementArriere)
        }
        parcel.writeString(refJointAvant!!)
        parcel.writeString(refJointArriere!!)
        parcel.writeBoolean(typeJointAvant!!)
        parcel.writeBoolean(typeJointArriere!!)
        parcel.writeInt(ventilateur!!)
        parcel.writeInt(capotV!!)
        parcel.writeInt(socleBoiteABorne!!)
        parcel.writeInt(capotBoiteABorne!!)
        parcel.writeInt(plaqueABorne!!)
        parcel.writeBoolean(presenceSondes!!)
        parcel.writeString(typeSondes!!)
        parcel.writeBoolean(equilibrage!!)
        parcel.writeString(peinture!!)
        parcel.writeInt(isolationMasseInduit!!)
        parcel.writeInt(isolationMassePolesPrincipaux!!)
        parcel.writeInt(isolationMassePolesAuxilliaires!!)
        parcel.writeInt(isolationMassePolesCompensatoires!!)
        parcel.writeInt(isolationMassePorteBalais!!)
        parcel.writeInt(resistanceInduit!!)
        parcel.writeInt(resistancePP!!)
        parcel.writeInt(resistancePA!!)
        parcel.writeInt(resistancePC!!)
        parcel.writeInt(tensionInduit!!)
        parcel.writeInt(intensiteInduit!!)
        parcel.writeInt(tensionExcitation!!)
        parcel.writeInt(intensiteExcitation!!)
        parcel.writeInt(dureeTotale!!)
        parcel.writeString(observations!!)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BodyDemontageCC> {
        @RequiresApi(Build.VERSION_CODES.Q)
        override fun createFromParcel(parcel: Parcel): BodyDemontageCC {
            return BodyDemontageCC(parcel)
        }

        override fun newArray(size: Int): Array<BodyDemontageCC?> {
            return arrayOfNulls(size)
        }
    }
}

class BodyDemontageAlternateur (
    var observations: String?,
    var typeMoteur: String?,
    var status: Long?,
    var marque: String?,
    var numSerie: Int?,
    var puissance: Float?,
    var bride: Float?,
    var vitesse : Float?,
    var arbreSortantEntrant:Boolean?, //arbre sortant ou rentrant
    var accouplement:Boolean?,
    var coteAccouplement:String?,
    var clavette: Boolean?,
    var aspect:Int?,
    var aspectInterieur:Int?,
    var couplage:String?,
    var flasqueAvant: Int?,
    var flasqueArriere: Int?,
    var porteeRAvant: Int?,
    var porteeRArriere:  Int?,
    var boutArbre: Boolean?,
    var rondelleElastique: Boolean?,
   var refRoulementAvant: Array<String>?,
                      var refRoulementArriere: Array<String>?,
                      var typeRoulementAvant: Array<String>?,
                      var typeRoulementArriere: Array<String>?,
    var refJointAvant: String?,
    var refJointArriere: String?,
    var typeJointAvant: Boolean?,
    var typeJointArriere: Boolean?,
    var ventilateur: Int?,
    var capotV: Int?,
    var socleBoiteABorne : Int?,
    var capotBoiteABorne : Int?,
    var plaqueABorne : Int?,
    var presenceSondes: Boolean ?,
    var typeSondes: String?,
    var equilibrage: Boolean?,
    var peinture : String?,
    var isolementMasseStatorPrincipalU: Float?,
    var isolementMasseStatorPrincipalV: Float?,
    var isolementMasseStatorPrincipalW	: Float?,
    var isolementMasseRotorPrincipal	: Float?,
    var isolementMasseStatorExcitation	: Float?,
    var isolementMasseRotorExcitation	: Float?,
    var resistanceStatorPrincipalU	: Float?,
    var resistanceStatorPrincipalV	: Float?,
    var resistanceStatorPrincipalW	: Float?,
    var resistanceRotorPrincipal	: Float?,
    var resistanceStatorExcitation	: Float?,
    var resistanceRotorExcitation	: Float?,
    var isolementPhasePhaseStatorPrincipalUV	: Float?,
    var isolementPhasePhaseStatorPrincipalVW	: Float?,
    var isolementPhasePhaseStatorPrincipalUW	: Float?,
    var testDiode : Boolean?,
    var tensionU	: Float?,
    var tensionV	: Float?,
    var tensionW	: Float?,
    var intensiteU	: Float?,
    var intensiteV	: Float?,
    var intensiteW	: Float?,
    var tensionExcitation	: Float?,
    var intensiteExcitation	: Float?,
    var dureeTotale: Int?
): Parcelable {
    @RequiresApi(Build.VERSION_CODES.Q)
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readLong(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readBoolean(),
        parcel.readBoolean(),
        parcel.readString(),
        parcel.readBoolean(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readBoolean(),
        parcel.readBoolean(),
        arrayOf<String>().apply {
            parcel.readArray(String::class.java.classLoader)
        },
        arrayOf<String>().apply {
            parcel.readArray(String::class.java.classLoader)
        },
        arrayOf<String>().apply {
            parcel.readArray(String::class.java.classLoader)
        },
        arrayOf<String>().apply {
            parcel.readArray(String::class.java.classLoader)
        },
        parcel.readString(),
        parcel.readString(),
        parcel.readBoolean(),
        parcel.readBoolean(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readBoolean(),
        parcel.readString(),
        parcel.readBoolean(),
        parcel.readString(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readBoolean(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readInt(),
    ) {
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(observations!!)
        parcel.writeString(typeMoteur!!)
        parcel.writeLong(status!!)
        parcel.writeString(marque!!)
        parcel.writeInt(numSerie!!)
        parcel.writeFloat(puissance!!)
        parcel.writeFloat(bride!!)
        parcel.writeFloat(vitesse!!)
        parcel.writeBoolean(arbreSortantEntrant!!)
        parcel.writeBoolean(accouplement!!)
        parcel.writeString(coteAccouplement!!)
        parcel.writeBoolean(clavette!!)
        parcel.writeInt(aspect!!)
        parcel.writeInt(aspectInterieur!!)
        parcel.writeString(couplage!!)
        parcel.writeInt(flasqueAvant!!)
        parcel.writeInt(flasqueArriere!!)
        parcel.writeInt(porteeRAvant!!)
        parcel.writeInt(porteeRArriere!!)
        parcel.writeBoolean(boutArbre!!)
        parcel.writeBoolean(rondelleElastique!!)
        arrayOf<String>().apply {
            parcel.writeArray(refRoulementAvant)
        }
        arrayOf<String>().apply {
            parcel.writeArray(refRoulementArriere)
        }
        arrayOf<String>().apply {
            parcel.writeArray(typeRoulementAvant)
        }
        arrayOf<String>().apply {
            parcel.writeArray(typeRoulementArriere)
        }
        parcel.writeString(refJointAvant!!)
        parcel.writeString(refJointArriere!!)
        parcel.writeBoolean(typeJointAvant!!)
        parcel.writeBoolean(typeJointArriere!!)
        parcel.writeInt(ventilateur!!)
        parcel.writeInt(capotV!!)
        parcel.writeInt(socleBoiteABorne!!)
        parcel.writeInt(capotBoiteABorne!!)
        parcel.writeInt(plaqueABorne!!)
        parcel.writeBoolean(presenceSondes!!)
        parcel.writeString(typeSondes!!)
        parcel.writeBoolean(equilibrage!!)
        parcel.writeString(peinture!!)
        parcel.writeFloat(isolementMasseStatorPrincipalU!!)
        parcel.writeFloat(isolementMasseStatorPrincipalV!!)
        parcel.writeFloat(isolementMasseStatorPrincipalW!!)
        parcel.writeFloat(isolementMasseRotorPrincipal!!)
        parcel.writeFloat(isolementMasseStatorExcitation!!)
        parcel.writeFloat(resistanceStatorPrincipalU!!)
        parcel.writeFloat(resistanceStatorPrincipalV!!)
        parcel.writeFloat(resistanceStatorPrincipalW!!)
        parcel.writeFloat(resistanceRotorPrincipal!!)
        parcel.writeFloat(resistanceStatorExcitation!!)
        parcel.writeFloat(resistanceRotorExcitation!!)
        parcel.writeFloat(isolementPhasePhaseStatorPrincipalUV!!)
        parcel.writeFloat(isolementPhasePhaseStatorPrincipalVW!!)
        parcel.writeFloat(isolementPhasePhaseStatorPrincipalUW!!)
        parcel.writeBoolean(testDiode!!)
        parcel.writeFloat(tensionU!!)
        parcel.writeFloat(tensionV!!)
        parcel.writeFloat(tensionW!!)
        parcel.writeFloat(intensiteU!!)
        parcel.writeFloat(intensiteV!!)
        parcel.writeFloat(intensiteW!!)
        parcel.writeFloat(tensionExcitation!!)
        parcel.writeFloat(intensiteExcitation!!)
        parcel.writeInt(dureeTotale!!)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BodyDemontageAlternateur> {
        @RequiresApi(Build.VERSION_CODES.Q)
        override fun createFromParcel(parcel: Parcel): BodyDemontageAlternateur {
            return BodyDemontageAlternateur(parcel)
        }

        override fun newArray(size: Int): Array<BodyDemontageAlternateur?> {
            return arrayOfNulls(size)
        }
    }
}

class BodyDemontageRotorBobine (
    var observations: String?,
    var typeMoteur: String?,
    var status: Long?,
    var marque: String?,
    var numSerie: Int?,
    var puissance: Float?,
    var bride: Float?,
    var vitesse : Float?,
    var arbreSortantEntrant:Boolean?, //arbre sortant ou rentrant
    var accouplement:Boolean?,
    var coteAccouplement:String?,
    var clavette: Boolean?,
    var aspect:Int?,
    var aspectInterieur:Int?,
    var couplage:String?,
    var flasqueAvant: Int?,
    var flasqueArriere: Int?,
    var porteeRAvant: Int?,
    var porteeRArriere:  Int?,
    var boutArbre: Boolean?,
    var rondelleElastique: Boolean?,
    var refRoulementAvant: Array<String>?,
    var refRoulementArriere: Array<String>?,
    var typeRoulementAvant: Array<String>?,
    var typeRoulementArriere: Array<String>?,
    var refJointAvant: String?,
    var refJointArriere: String?,
    var typeJointAvant: Boolean?,
    var typeJointArriere: Boolean?,
    var ventilateur: Int?,
    var capotV: Int?,
    var socleBoiteABorne : Int?,
    var capotBoiteABorne : Int?,
    var plaqueABorne : Int?,
    var presenceSondes: Boolean ?,
    var typeSondes: String?,
    var equilibrage: Boolean?,
    var peinture : String?,
    var isolementPhaseMasseStatorUM	: Float?,
    var isolementPhaseMasseStatorVM	: Float?,
    var isolementPhaseMasseStatorWM	: Float?,
    var isolementPhaseMasseRotorB1M	: Float?,
    var isolementPhaseMasseRotorB2M	: Float?,
    var isolementPhaseMasseRotorB3M	: Float?,
    var isolementPhaseMassePorteBalaisM	: Float?,
    var isolementPhasePhaseStatorUV	: Float?,
    var isolementPhasePhaseStatorVW	: Float?,
    var isolementPhasePhaseStatorUW	: Float?,
    var resistanceStatorU	: Float?,
    var resistanceStatorV	: Float?,
    var resistanceStatorW	: Float?,
    var resistanceRotorB1B2	: Float?,
    var resistanceRotorB2B2	: Float?,
    var resistanceRotorB1B3	: Float?,
    var tensionU	: Float?,
    var tensionV	: Float?,
    var tensionW	: Float?,
    var tensionRotor	: Float?,
    var intensiteU	: Float?,
    var intensiteV	: Float?,
    var intensiteW	: Float?,
    var intensiteRotor	: Float?,
    var dureeEssai	: Int?,
    var dureeTotale: Int?
): Parcelable {
    @RequiresApi(Build.VERSION_CODES.Q)
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readLong(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readBoolean(),
        parcel.readBoolean(),
        parcel.readString(),
        parcel.readBoolean(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readBoolean(),
        parcel.readBoolean(),
        arrayOf<String>().apply {
            parcel.readArray(String::class.java.classLoader)
        },
        arrayOf<String>().apply {
            parcel.readArray(String::class.java.classLoader)
        },
        arrayOf<String>().apply {
            parcel.readArray(String::class.java.classLoader)
        },
        arrayOf<String>().apply {
            parcel.readArray(String::class.java.classLoader)
        },
        parcel.readString(),
        parcel.readString(),
        parcel.readBoolean(),
        parcel.readBoolean(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readBoolean(),
        parcel.readString(),
        parcel.readBoolean(),
        parcel.readString(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readInt(),
        parcel.readInt(),
    ) {
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(observations!!)
        parcel.writeString(typeMoteur!!)
        parcel.writeLong(status!!)
        parcel.writeString(marque!!)
        parcel.writeInt(numSerie!!)
        parcel.writeFloat(puissance!!)
        parcel.writeFloat(bride!!)
        parcel.writeFloat(vitesse!!)
        parcel.writeBoolean(arbreSortantEntrant!!)
        parcel.writeBoolean(accouplement!!)
        parcel.writeString(coteAccouplement!!)
        parcel.writeBoolean(clavette!!)
        parcel.writeInt(aspect!!)
        parcel.writeInt(aspectInterieur!!)
        parcel.writeString(couplage!!)
        parcel.writeInt(flasqueAvant!!)
        parcel.writeInt(flasqueArriere!!)
        parcel.writeInt(porteeRAvant!!)
        parcel.writeInt(porteeRArriere!!)
        parcel.writeBoolean(boutArbre!!)
        parcel.writeBoolean(rondelleElastique!!)
        arrayOf<String>().apply {
            parcel.writeArray(refRoulementAvant)
        }
        arrayOf<String>().apply {
            parcel.writeArray(refRoulementArriere)
        }
        arrayOf<String>().apply {
            parcel.writeArray(typeRoulementAvant)
        }
        arrayOf<String>().apply {
            parcel.writeArray(typeRoulementArriere)
        }
        parcel.writeString(refJointAvant!!)
        parcel.writeString(refJointArriere!!)
        parcel.writeBoolean(typeJointAvant!!)
        parcel.writeBoolean(typeJointArriere!!)
        parcel.writeInt(ventilateur!!)
        parcel.writeInt(capotV!!)
        parcel.writeInt(socleBoiteABorne!!)
        parcel.writeInt(capotBoiteABorne!!)
        parcel.writeInt(plaqueABorne!!)
        parcel.writeBoolean(presenceSondes!!)
        parcel.writeString(typeSondes!!)
        parcel.writeBoolean(equilibrage!!)
        parcel.writeString(peinture!!)
        parcel.writeFloat(isolementPhaseMasseStatorUM!!)
        parcel.writeFloat(isolementPhaseMasseStatorVM!!)
        parcel.writeFloat(isolementPhaseMasseStatorWM!!)
        parcel.writeFloat(isolementPhaseMasseRotorB1M!!)
        parcel.writeFloat(isolementPhaseMasseRotorB2M!!)
        parcel.writeFloat(isolementPhaseMasseRotorB3M!!)
        parcel.writeFloat(isolementPhaseMassePorteBalaisM!!)
        parcel.writeFloat(isolementPhasePhaseStatorUV!!)
        parcel.writeFloat(isolementPhasePhaseStatorVW!!)
        parcel.writeFloat(isolementPhasePhaseStatorUW!!)
        parcel.writeFloat(resistanceStatorU!!)
        parcel.writeFloat(resistanceStatorV!!)
        parcel.writeFloat(resistanceStatorW!!)
        parcel.writeFloat(resistanceRotorB1B2!!)
        parcel.writeFloat(resistanceRotorB2B2!!)
        parcel.writeFloat(resistanceRotorB1B3!!)
        parcel.writeFloat(tensionU!!)
        parcel.writeFloat(tensionV!!)
        parcel.writeFloat(tensionW!!)
        parcel.writeFloat(intensiteV!!)
        parcel.writeFloat(intensiteW!!)
        parcel.writeFloat(tensionRotor!!)
        parcel.writeFloat(intensiteU!!)
        parcel.writeFloat(intensiteV!!)
        parcel.writeFloat(intensiteW!!)
        parcel.writeFloat(intensiteRotor!!)
        parcel.writeInt(dureeEssai!!)
        parcel.writeInt(dureeTotale!!)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BodyDemontageRotorBobine> {
        @RequiresApi(Build.VERSION_CODES.Q)
        override fun createFromParcel(parcel: Parcel): BodyDemontageRotorBobine {
            return BodyDemontageRotorBobine(parcel)
        }

        override fun newArray(size: Int): Array<BodyDemontageRotorBobine?> {
            return arrayOfNulls(size)
        }
    }
}

class BodyDemontageMonophase (
    var observations: String?,
    var typeMoteur: String?,
    var status: Long?,
    var marque: String?,
    var numSerie: Int?,
    var puissance: Float?,
    var bride: Float?,
    var vitesse : Float?,
    var arbreSortantEntrant:Boolean?, //arbre sortant ou rentrant
    var accouplement:Boolean?,
    var coteAccouplement:String?,
    var clavette: Boolean?,
    var aspect:Int?,
    var aspectInterieur:Int?,
    var couplage:String?,
    var flasqueAvant: Int?,
    var flasqueArriere: Int?,
    var porteeRAvant: Int?,
    var porteeRArriere:  Int?,
    var boutArbre: Boolean?,
    var rondelleElastique: Boolean?,
    var refRoulementAvant: Array<String>?,
    var refRoulementArriere: Array<String>?,
    var typeRoulementAvant: Array<String>?,
    var typeRoulementArriere: Array<String>?,
    var refJointAvant: String?,
    var refJointArriere: String?,
    var typeJointAvant: Boolean?,
    var typeJointArriere: Boolean?,
    var ventilateur: Int?,
    var capotV: Int?,
    var socleBoiteABorne : Int?,
    var capotBoiteABorne : Int?,
    var plaqueABorne : Int?,
    var presenceSondes: Boolean ?,
    var typeSondes: String?,
    var equilibrage: Boolean?,
    var peinture : String?,
    var isolementPhaseMasse: Float?,
    var resistanceTravail	: Float?,
    var resistanceDemarrage	: Float?,
    var valeurCondensateur	: Float?,
    var tension	: Float?,
    var intensite	: Float?,
    var dureeTotale: Int?
): Parcelable {
    @RequiresApi(Build.VERSION_CODES.Q)
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readLong(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readBoolean(),
        parcel.readBoolean(),
        parcel.readString(),
        parcel.readBoolean(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readBoolean(),
        parcel.readBoolean(),
        arrayOf<String>().apply {
            parcel.readArray(String::class.java.classLoader)
        },
        arrayOf<String>().apply {
            parcel.readArray(String::class.java.classLoader)
        },
        arrayOf<String>().apply {
            parcel.readArray(String::class.java.classLoader)
        },
        arrayOf<String>().apply {
            parcel.readArray(String::class.java.classLoader)
        },
        parcel.readString(),
        parcel.readString(),
        parcel.readBoolean(),
        parcel.readBoolean(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readBoolean(),
        parcel.readString(),
        parcel.readBoolean(),
        parcel.readString(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readInt(),
    ) {
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(typeMoteur!!)
        parcel.writeString(observations!!)
        parcel.writeLong(status!!)
        parcel.writeString(marque!!)
        parcel.writeInt(numSerie!!)
        parcel.writeFloat(puissance!!)
        parcel.writeFloat(bride!!)
        parcel.writeFloat(vitesse!!)
        parcel.writeBoolean(arbreSortantEntrant!!)
        parcel.writeBoolean(accouplement!!)
        parcel.writeString(coteAccouplement!!)
        parcel.writeBoolean(clavette!!)
        parcel.writeInt(aspect!!)
        parcel.writeInt(aspectInterieur!!)
        parcel.writeString(couplage!!)
        parcel.writeInt(flasqueAvant!!)
        parcel.writeInt(flasqueArriere!!)
        parcel.writeInt(porteeRAvant!!)
        parcel.writeInt(porteeRArriere!!)
        parcel.writeBoolean(boutArbre!!)
        parcel.writeBoolean(rondelleElastique!!)
        arrayOf<String>().apply {
            parcel.writeArray(refRoulementAvant)
        }
        arrayOf<String>().apply {
            parcel.writeArray(refRoulementArriere)
        }
        arrayOf<String>().apply {
            parcel.writeArray(typeRoulementAvant)
        }
        arrayOf<String>().apply {
            parcel.writeArray(typeRoulementArriere)
        }
        parcel.writeString(refJointAvant!!)
        parcel.writeString(refJointArriere!!)
        parcel.writeBoolean(typeJointAvant!!)
        parcel.writeBoolean(typeJointArriere!!)
        parcel.writeInt(ventilateur!!)
        parcel.writeInt(capotV!!)
        parcel.writeInt(socleBoiteABorne!!)
        parcel.writeInt(capotBoiteABorne!!)
        parcel.writeInt(plaqueABorne!!)
        parcel.writeBoolean(presenceSondes!!)
        parcel.writeString(typeSondes!!)
        parcel.writeBoolean(equilibrage!!)
        parcel.writeString(peinture!!)
        parcel.writeFloat(isolementPhaseMasse!!)
        parcel.writeFloat(resistanceTravail!!)
        parcel.writeFloat(resistanceDemarrage!!)
        parcel.writeFloat(valeurCondensateur!!)
        parcel.writeFloat(tension!!)
        parcel.writeFloat(intensite!!)
        parcel.writeInt(dureeTotale!!)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BodyDemontageMonophase> {
        @RequiresApi(Build.VERSION_CODES.Q)
        override fun createFromParcel(parcel: Parcel): BodyDemontageMonophase {
            return BodyDemontageMonophase(parcel)
        }

        override fun newArray(size: Int): Array<BodyDemontageMonophase?> {
            return arrayOfNulls(size)
        }
    }
}

class BodyDemoPompe(
    var status: Long?,
    var typeMoteur: String?,
    var marque: String?,
    var numSerie: Int?,
    var fluide: String?,
    var sensRotation: Boolean?,
    var typeRessort: Int?,
    var typeJoint: String?,
    var matiere: Int?,
    var diametreArbre:Float?,
    var diametreExtPR:Float?,
    var diametreExtPF:Float?,
    var epaisseurPF:Float?,
    var longueurRotativeNonComprimee:Float?,
    var longueurRotativeComprimee:Float?,
    var longueurRotativeTravail:Float?,
    var observations:String?,
    var dureeTotale:Long?,
): Parcelable {
    @RequiresApi(Build.VERSION_CODES.Q)
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readBoolean(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readString(),
        parcel.readLong(),
    ) {
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(status!!)
        parcel.writeString(typeMoteur!!)
        parcel.writeString(marque!!)
        parcel.writeInt(numSerie!!)
        parcel.writeString(fluide!!)
        parcel.writeBoolean(sensRotation!!)
        parcel.writeInt(typeRessort!!)
        parcel.writeString(typeJoint!!)
        parcel.writeInt(matiere!!)
        parcel.writeFloat(diametreArbre!!)
        parcel.writeFloat(diametreExtPR!!)
        parcel.writeFloat(diametreExtPF!!)
        parcel.writeFloat(epaisseurPF!!)
        parcel.writeFloat(longueurRotativeNonComprimee!!)
        parcel.writeFloat(longueurRotativeComprimee!!)
        parcel.writeFloat(longueurRotativeTravail!!)
        parcel.writeString(observations!!)
        parcel.writeLong(dureeTotale!!)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BodyDemoPompe> {
        @RequiresApi(Build.VERSION_CODES.Q)
        override fun createFromParcel(parcel: Parcel): BodyDemoPompe {
            return BodyDemoPompe(parcel)
        }

        override fun newArray(size: Int): Array<BodyDemoPompe?> {
            return arrayOfNulls(size)
        }
    }
}

class BodyRemontageTriphase(
    var status: Int?,
    var dureeTotale: Long?,
    var observations: String?,
    var remontageRoulement: Int?,
    var collageRoulementPorteeArbre: Int?,
    var collageRoulementPorteeFlasque: Int?,
    var verificationFixationCouronne: Boolean?,
    var verificationIsolementPorteBalais: Boolean?,
    var isolementPorteBalaisV: Int?,
    var isolementPorteBalaisOhm: Int?,
     var tensionStator:Boolean?,
     var tensionStatorU:Float?,
     var tensionStatorV:Float?,
     var tensionStatorW:Float?,
     var tensionInducteurs: Boolean?,
     var tensionInducteursU: Float?,
     var tensionInducteursV: Float?,
     var tensionInducteursW: Float?,
     var intensiteStator:Boolean?,
     var intensiteStatorU:Float?,
     var intensiteStatorV:Float?,
     var intensiteStatorW:Float?,
     var intensiteInducteurs: Boolean?,
     var intensiteInducteursU: Float?,
     var intensiteInducteursV: Float?,
     var intensiteInducteursW: Float?,
     var tensionInduit:Boolean?,
     var tensionInduitU:Float?,
     var tensionInduitV:Float?,
     var tensionInduitW:Float?,
     var tensionRotor: Boolean?,
     var tensionRotorU: Float?,
     var tensionRotorV: Float?,
     var tensionRotorW: Float?,
    var intensiteInduit: Boolean,
    var intensiteInduitU: Float?,
    var vitesseU: Float?,
    var puissanceU: Float?,
    var dureeEssai: Float?,
    var sensRotation: Int?,
    var vitesse1V: Float?,  // vitesse 1v
    var acceleration1V: Float?,  //accélération 1v
    var vitesse2V: Float?,  // vitesse 2v
    var acceleration2V: Float?,  //accélération 2v
    var vitesse1H: Float?,  // vitesse 1H
    var acceleration1H: Float?,  //accélération 1H
    var vitesse2H: Float?,  // vitesse 2H
    var acceleration2H: Float?,  //accélération 2H
    var vitesse2A: Float?,  // vitesse 2A
    var acceleration2A: Float?,  //accélération 2A
    var isolementPhaseMasse: Float?,
    var isolementPhase: Float?,
    var resistanceStatorU: Float?,
    var resistanceStatorV: Float?,
    var resistanceStatorW: Float?,
    var isolementPMStatorU: Float?,
    var isolementPMStatorV: Float?,
    var isolementPMStatorW: Float?,
    var isolementPMRotorU: Float?,
    var isolementPMRotorV: Float?,
    var isolementPMRotorW: Float?,
    var isolementPhaseStatorUV: Float?,
    var isolementPhaseStatorVW: Float?,
    var isolementPhaseStatorUW: Float?,
    var isolementPhaseRotorUV: Float?,
    var isolementPhaseRotorVW: Float?,
    var isolementPhaseRotorUW: Float?,
): Parcelable {
    @RequiresApi(Build.VERSION_CODES.Q)
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readLong(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readBoolean(),
        parcel.readBoolean(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readBoolean(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readBoolean(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readBoolean(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readBoolean(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readBoolean(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readBoolean(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readBoolean(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readInt(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readFloat(),
    ) {
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(status!!)
        parcel.writeLong(dureeTotale!!)
        parcel.writeString(observations!!)
        parcel.writeInt(remontageRoulement!!)
        parcel.writeInt(collageRoulementPorteeArbre!!)
        parcel.writeInt(collageRoulementPorteeFlasque!!)
        parcel.writeBoolean(verificationFixationCouronne!!)
        parcel.writeBoolean(verificationIsolementPorteBalais!!)
        parcel.writeInt(isolementPorteBalaisV!!)
        parcel.writeInt(isolementPorteBalaisOhm!!)
        parcel.writeBoolean(tensionStator!!)
        parcel.writeFloat(tensionStatorU!!)
        parcel.writeFloat(tensionStatorV!!)
        parcel.writeFloat(tensionStatorW!!)
        parcel.writeBoolean(tensionInducteurs!!)
        parcel.writeFloat(tensionInducteursU!!)
        parcel.writeFloat(tensionInducteursV!!)
        parcel.writeFloat(tensionInducteursW!!)
        parcel.writeBoolean(intensiteStator!!)
        parcel.writeFloat(intensiteStatorU!!)
        parcel.writeFloat(intensiteStatorV!!)
        parcel.writeFloat(intensiteStatorW!!)
        parcel.writeBoolean(intensiteInducteurs!!)
        parcel.writeFloat(intensiteInducteursU!!)
        parcel.writeFloat(intensiteInducteursV!!)
        parcel.writeFloat(intensiteInducteursW!!)
        parcel.writeBoolean(tensionInduit!!)
        parcel.writeFloat(tensionInduitU!!)
        parcel.writeFloat(tensionInduitV!!)
        parcel.writeFloat(tensionInduitW!!)
        parcel.writeBoolean(tensionRotor!!)
        parcel.writeFloat(tensionRotorU!!)
        parcel.writeFloat(tensionRotorV!!)
        parcel.writeFloat(tensionRotorW!!)
        parcel.writeBoolean(intensiteInduit!!)
        parcel.writeFloat(intensiteInduitU!!)
        parcel.writeFloat(vitesseU!!)
        parcel.writeFloat(puissanceU!!)
        parcel.writeFloat(dureeEssai!!)
        parcel.writeInt(sensRotation!!)
        parcel.writeFloat(vitesse1V!!)
        parcel.writeFloat(acceleration1V!!)
        parcel.writeFloat(vitesse2V!!)
        parcel.writeFloat(acceleration2V!!)
        parcel.writeFloat(vitesse1H!!)
        parcel.writeFloat(acceleration1H!!)
        parcel.writeFloat(vitesse2H!!)
        parcel.writeFloat(acceleration2H!!)
        parcel.writeFloat(vitesse2A!!)
        parcel.writeFloat(acceleration2A!!)
        parcel.writeFloat(isolementPhaseMasse!!)
        parcel.writeFloat(isolementPhase!!)
        parcel.writeFloat(resistanceStatorU!!)
        parcel.writeFloat(resistanceStatorV!!)
        parcel.writeFloat(resistanceStatorW!!)
        parcel.writeFloat(isolementPMStatorU!!)
        parcel.writeFloat(isolementPMStatorV!!)
        parcel.writeFloat(isolementPMStatorW!!)
        parcel.writeFloat(isolementPMRotorU!!)
        parcel.writeFloat(isolementPMRotorV!!)
        parcel.writeFloat(isolementPMRotorW!!)
        parcel.writeFloat(isolementPhaseStatorUV!!)
        parcel.writeFloat(isolementPhaseStatorVW!!)
        parcel.writeFloat(isolementPhaseRotorUW!!)
        parcel.writeFloat(isolementPhaseStatorUV!!)
        parcel.writeFloat(isolementPhaseStatorVW!!)
        parcel.writeFloat(isolementPhaseRotorUW!!)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BodyRemontageTriphase> {
        @RequiresApi(Build.VERSION_CODES.Q)
        override fun createFromParcel(parcel: Parcel): BodyRemontageTriphase {
            return BodyRemontageTriphase(parcel)
        }

        override fun newArray(size: Int): Array<BodyRemontageTriphase?> {
            return arrayOfNulls(size)
        }
    }
}
class BodyRemontage(
    var status: Int?,
    var dureeTotale: Long?,
    var observations: String?,
    var remontageRoulement: Int?,
    var collageRoulementPorteeArbre: Int?,
    var collageRoulementFlasque: Int?,
    var verificationFixationCouronne: Boolean?,
    var verificationIsolementPorteBalais: Boolean?,
    var isolementPorteBalaisV: Int?,
    var isolementPorteBalaisOhm: Int?
): Parcelable {
    @RequiresApi(Build.VERSION_CODES.Q)
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readLong(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readBoolean(),
        parcel.readBoolean(),
        parcel.readInt(),
        parcel.readInt(),

    ) {
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(status!!)
        parcel.writeLong(dureeTotale!!)
        parcel.writeString(observations!!)
        parcel.writeInt(remontageRoulement!!)
        parcel.writeInt(collageRoulementPorteeArbre!!)
        parcel.writeInt(collageRoulementFlasque!!)
        parcel.writeBoolean(verificationFixationCouronne!!)
        parcel.writeBoolean(verificationIsolementPorteBalais!!)
        parcel.writeInt(isolementPorteBalaisV!!)
        parcel.writeInt(isolementPorteBalaisOhm!!)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BodyRemontage> {
        @RequiresApi(Build.VERSION_CODES.Q)
        override fun createFromParcel(parcel: Parcel): BodyRemontage {
            return BodyRemontage(parcel)
        }

        override fun newArray(size: Int): Array<BodyRemontage?> {
            return arrayOfNulls(size)
        }
    }
}

class BodyRemontageCC(
    var status: Int?,
    var dureeTotale: Long?,
    var observations: String?,
    var remontageRoulement: Int?,
    var collageRoulementPorteeArbre: Int?,
    var collageRoulementFlasque: Int?,
    var verificationFixationCouronne: Boolean?,
    var verificationIsolementPorteBalais: Boolean?,
    var isolementPorteBalaisV: Int?,
    var isolementPorteBalaisOhm: Int?,
    var tensionStator:Boolean?,
    var tensionStatorU:Float?,
    var tensionStatorV:Float?,
    var tensionStatorW:Float?,
    var tensionInducteurs: Boolean?,
    var tensionInducteursU: Float?,
    var tensionInducteursV: Float?,
    var tensionInducteursW: Float?,
    var intensiteStator:Boolean?,
    var intensiteStatorU:Float?,
    var intensiteStatorV:Float?,
    var intensiteStatorW:Float?,
    var intensiteInducteurs: Boolean?,
    var intensiteInducteursU: Float?,
    var intensiteInducteursV: Float?,
    var intensiteInducteursW: Float?,
    var tensionInduit:Boolean?,
    var tensionInduitU:Float?,
    var tensionInduitV:Float?,
    var tensionInduitW:Float?,
    var tensionRotor: Boolean?,
    var tensionRotorU: Float?,
    var tensionRotorV: Float?,
    var tensionRotorW: Float?,
    var intensiteInduit: Boolean,
    var intensiteInduitU: Float?,
    var vitesseU: Float?,
    var puissanceU: Float?,
    var dureeEssai: Float?,
    var sensRotation: Int?,
    var vitesse1V: Float?,  // vitesse 1v
    var acceleration1V: Float?,  //accélération 1v
    var vitesse2V: Float?,  // vitesse 2v
    var acceleration2V: Float?,  //accélération 2v
    var vitesse1H: Float?,  // vitesse 1H
    var acceleration1H: Float?,  //accélération 1H
    var vitesse2H: Float?,  // vitesse 2H
    var acceleration2H: Float?,  //accélération 2H
    var vitesse2A: Float?,  // vitesse 2A
    var acceleration2A: Float?,  //accélération 2A
    var resistanceInducteurs: Float?,
    var resistanceInduit: Float?,
    var isolementInducteursMasse: Float?,
    var isolementInduitMasse: Float?,
    var isolementInduitInducteurs: Float?,
    var releveIsoInducteursMasse: Float?,
    var releveIsoInduitMasse: Float?,
    var releveIsoInduitInducteurs: Float?,
): Parcelable {
    @RequiresApi(Build.VERSION_CODES.Q)
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readLong(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readBoolean(),
        parcel.readBoolean(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readBoolean(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readBoolean(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readBoolean(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readBoolean(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readBoolean(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readBoolean(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readBoolean(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readInt(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readFloat()
    ) {
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(status!!)
        parcel.writeLong(dureeTotale!!)
        parcel.writeString(observations!!)
        parcel.writeInt(remontageRoulement!!)
        parcel.writeInt(collageRoulementPorteeArbre!!)
        parcel.writeInt(collageRoulementFlasque!!)
        parcel.writeBoolean(verificationFixationCouronne!!)
        parcel.writeBoolean(verificationIsolementPorteBalais!!)
        parcel.writeInt(isolementPorteBalaisV!!)
        parcel.writeInt(isolementPorteBalaisOhm!!)
        parcel.writeBoolean(tensionStator!!)
        parcel.writeFloat(tensionStatorU!!)
        parcel.writeFloat(tensionStatorV!!)
        parcel.writeFloat(tensionStatorW!!)
        parcel.writeBoolean(tensionInducteurs!!)
        parcel.writeFloat(tensionInducteursU!!)
        parcel.writeFloat(tensionInducteursV!!)
        parcel.writeFloat(tensionInducteursW!!)
        parcel.writeBoolean(intensiteStator!!)
        parcel.writeFloat(intensiteStatorU!!)
        parcel.writeFloat(intensiteStatorV!!)
        parcel.writeFloat(intensiteStatorW!!)
        parcel.writeBoolean(intensiteInducteurs!!)
        parcel.writeFloat(intensiteInducteursU!!)
        parcel.writeFloat(intensiteInducteursV!!)
        parcel.writeFloat(intensiteInducteursW!!)
        parcel.writeBoolean(tensionInduit!!)
        parcel.writeFloat(tensionInduitU!!)
        parcel.writeFloat(tensionInduitV!!)
        parcel.writeFloat(tensionInduitW!!)
        parcel.writeBoolean(tensionRotor!!)
        parcel.writeFloat(tensionRotorU!!)
        parcel.writeFloat(tensionRotorV!!)
        parcel.writeFloat(tensionRotorW!!)
        parcel.writeBoolean(intensiteInduit!!)
        parcel.writeFloat(intensiteInduitU!!)
        parcel.writeFloat(vitesseU!!)
        parcel.writeFloat(puissanceU!!)
        parcel.writeFloat(dureeEssai!!)
        parcel.writeInt(sensRotation!!)
        parcel.writeFloat(vitesse1V!!)
        parcel.writeFloat(acceleration1V!!)
        parcel.writeFloat(vitesse2V!!)
        parcel.writeFloat(acceleration2V!!)
        parcel.writeFloat(vitesse1H!!)
        parcel.writeFloat(acceleration1H!!)
        parcel.writeFloat(vitesse2H!!)
        parcel.writeFloat(acceleration2H!!)
        parcel.writeFloat(vitesse2A!!)
        parcel.writeFloat(acceleration2A!!)
        parcel.writeFloat(resistanceInducteurs!!)
        parcel.writeFloat(resistanceInduit!!)
        parcel.writeFloat(isolementInducteursMasse!!)
        parcel.writeFloat(isolementInduitMasse!!)
        parcel.writeFloat(isolementInduitInducteurs!!)
        parcel.writeFloat(releveIsoInducteursMasse!!)
        parcel.writeFloat(releveIsoInduitMasse!!)
        parcel.writeFloat(releveIsoInduitInducteurs!!)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BodyRemontageCC> {
        @RequiresApi(Build.VERSION_CODES.Q)
        override fun createFromParcel(parcel: Parcel): BodyRemontageCC {
            return BodyRemontageCC(parcel)
        }

        override fun newArray(size: Int): Array<BodyRemontageCC?> {
            return arrayOfNulls(size)
        }
    }
}

class LoginResponse(
    @field:Json(name = "auth-token")
    var token:String?,
    var user:User?,
    var error: String?
)
class FichesResponse(
    var fiches:Array<Fiche>?
)
class ChantierResponse(
    var fiche:Chantier?
)
class BobinageResponse(
    var fiche:Bobinage?
)
class DemontageTriphaseResponse(
    var fiche: Triphase?
)
class DemontageCCResponse(
    var fiche: CourantContinu?
)
class DemontagePompeResponse(
    var fiche: DemontagePompe?
)
class RemontageTriphaseResponse(
    var fiche: RemontageTriphase?
)
class RemontageCCResponse(
    var fiche: RemontageCourantC?
)
class VehiculesResponse(
    var vehicule:Vehicule?
)
class DemontageResponse(
    var fiche:DemontageMoteur?
)
class DemontageAlternateurResponse(
    var fiche:DemontageAlternateur?
)
class DemontageMonophaseResponse(
    var fiche:DemontageMonophase?
)
class DemontageRotorBobineResponse(
    var fiche:DemontageRotorBobine?
)
class RemontageResponse(
    var fiche:Remontage?
)
class ClientsResponse(
    var client:Client?
)

class CustomDateAdapter : JsonAdapter <Date>() {
    private val dateFormat = SimpleDateFormat(SERVER_FORMAT, Locale.getDefault())

    @FromJson
    override fun fromJson(reader: JsonReader ): Date? {
        return try {
            val dateAsString = reader.nextString()
            synchronized(dateFormat) {
                dateFormat.parse(dateAsString)
            }
        } catch (e: Exception) {
            null
        }
    }
    @ToJson
    override fun toJson(writer: JsonWriter, value: Date?) {
        if (value != null) {
            synchronized(dateFormat) {
                writer.value(value.toString())
            }
        }
    }
    companion object {
        const val SERVER_FORMAT = ("yyyy-MM-dd'T'HH:mm") // define your server format here
    }
}
class Repository (var context:Context) {
    private val moshiBuilder = Moshi.Builder().add(CustomDateAdapter())
    val url = "http://195.154.107.195:4000"
    var okHttpClient = OkHttpClient.Builder()
        .callTimeout(1,TimeUnit.MINUTES)
        .connectTimeout(1, TimeUnit.MINUTES)
        .readTimeout(1, TimeUnit.MINUTES)
        .writeTimeout(2, TimeUnit.MINUTES)
        .build()

    val retrofit = Retrofit.Builder()
        .baseUrl(url)
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create(moshiBuilder.build()))
        .build()
    val service : APIstb by lazy {  retrofit.create(APIstb::class.java) }
    var db : LocalDatabase? = null;
    var chantierDao : ChantierDao? = null;
    var bobinageDao : BobinageDao ? = null;
    var demontageTriphaseDao : DemontageTriphaseDao? = null;
    var demontageCCDao : DemontageCCDao? = null;
    var demontageMonoDao : DemontageMonophaseDao? = null;
    var demontageRBDao : DemontageRotorBobineDao? = null;
    var demontageAlterDao : DemontageAlternateurDao? = null;
    var demontagePDao : DemontagePDao? = null;
    var remontageTriphaseDao: RemontageTriphaseDao? = null;
    var remontageCourantCDao: RemontageCCDao? = null;
    var vehiculeDao: VehiculeDao? = null;
    var clientDao: ClientsDao? = null;
    var remontageDao: RemontageDao? = null;

    fun logUser(username:String,psw:String,callback: Callback<LoginResponse>) {
        var body = BodyLogin(username,psw)
        var call = service.loginUser(body)
        var user: User? = null;
        call.enqueue(callback)
    }
    fun getFiches(token:String, callback:Callback<FichesResponse>) {
        var call = service.getFiches(token)
        var fiches: Array<Fiche>? = null
        call.enqueue(callback)
    }
    fun getFichesUser(token:String, userid:String, callback:Callback<FichesResponse>) {
        var call = service.getFichesUser(token,userid)
        var fiches: Array<Fiche>? = null
        call.enqueue(callback)
    }
    fun getChantier(token:String,ficheId:String, callback:Callback<ChantierResponse>){
        var call = service.getChantier(token,ficheId)
        var fiche:Chantier? = null
        call.enqueue(callback)
    }
    fun getBobinage(token:String,ficheId:String, callback:Callback<BobinageResponse>){
        var call = service.getBobinage(token,ficheId)
        var fiche:Bobinage? = null
        call.enqueue(callback)
    }
    fun getDemontage(token:String,ficheId:String, callback:Callback<DemontageResponse>){
        val call = service.getDemontage(token,ficheId)
        var fiche:DemontageMoteur?=null
        call.enqueue(callback)
    }
    fun getDemontageMono(token:String,ficheId:String, callback:Callback<DemontageMonophaseResponse>){
        val call = service.getDemontageMonophase(token,ficheId)
        var fiche:DemontageMonophase?=null
        call.enqueue(callback)
    }
    fun getDemontageAlternateur(token:String,ficheId:String, callback:Callback<DemontageAlternateurResponse>){
        val call = service.getDemontageAlternateur(token,ficheId)
        var fiche:DemontageAlternateur?=null
        call.enqueue(callback)
    }
    fun getDemontageRB(token:String,ficheId:String, callback:Callback<DemontageRotorBobineResponse>){
        val call = service.getDemontageRotorBobine(token,ficheId)
        var fiche:DemontageRotorBobine?=null
        call.enqueue(callback)
    }
    fun getDemontageTriphase(token:String,ficheId:String, callback: Callback<DemontageTriphaseResponse>){
        var call = service.getDemontageTriphase(token,ficheId)
        var fiche:Triphase? = null
        call.enqueue(callback)
    }
    fun getDemontagePompe(token:String,ficheId:String, callback: Callback<DemontagePompeResponse>){
        var call = service.getDemoPompe(token,ficheId)
        var fiche:DemontagePompe? = null
        call.enqueue(callback)
    }
    fun getAllVehicules(token:String, callback: Callback<VehiculesResponse>){
        var call = service.getAllVehicules(token)
        var vehicules:Array<Vehicule>? = null
        call.enqueue(callback)
    }
    fun getVehiculesById(token:String, id:String, callback: Callback<VehiculesResponse>){
        var call = service.getVehiculeById(token,id)
        var vehicule:Vehicule? = null
        call.enqueue(callback)
    }
    fun getAllClients(token:String, callback: Callback<ClientsResponse>){
        var call = service.getAllClients(token)
        var vehicules:Array<Client>? = null
        call.enqueue(callback)
    }
    fun patchDemontageTriphase(token:String,ficheId:String, triphase:Triphase, callback:Callback<DemontageTriphaseResponse>){
        var body = BodyDemontageTriphase(
            triphase.status,
            triphase.typeMoteur,
            triphase.marque,
            triphase.numSerie,
            triphase.puissance,
            triphase.bride,
            triphase.vitesse,
            triphase.arbreSortantEntrant,
            triphase.accouplement,
            triphase.coteAccouplement,
            triphase.clavette,
            triphase.aspect,
            triphase.aspectInterieur,
            triphase.couplage,
            triphase.flasqueAvant,
            triphase.flasqueArriere,
            triphase.porteeRAvant,
            triphase.porteeRArriere,
            triphase.boutArbre,
            triphase.rondelleElastique,
            triphase.refRoulementAvant,
            triphase.refRoulementArriere,
            triphase.typeRoulementAvant,
            triphase.typeRoulementArriere,
            triphase.refJointAvant,
            triphase.refJointArriere,
            triphase.typeJointAvant,
            triphase.typeJointArriere,
            triphase.ventilateur,
            triphase.capotV,
            triphase.socleBoiteABorne,
            triphase.capotBoiteABorne,
            triphase.plaqueABorne,
            triphase.presenceSondes,
            triphase.typeSondes,
            triphase.equilibrage,
            triphase.peinture,
            triphase.isolementPhaseMasseStatorUM,
            triphase.isolementPhaseMasseStatorVM,
            triphase.isolementPhaseMasseStatorWM,
            triphase.isolementPhasePhaseStatorUV,
            triphase.isolementPhasePhaseStatorVW,
            triphase.isolementPhasePhaseStatorUW,
            triphase.resistanceStatorU,
            triphase.resistanceStatorV,
            triphase.resistanceStatorW,
            triphase.tensionU,
            triphase.tensionV,
            triphase.tensionW,
            triphase.intensiteU,
            triphase.intensiteV,
            triphase.intensiteW,
            triphase.dureeEssai,
            triphase.dureeTotale!!.toInt(),
            triphase.observations
        )
        var call = service.patchDemontageTriphase(token,ficheId,body)
        var fiche:Triphase? = null
        call.enqueue(callback)
    }
    fun patchDemontageMeca(token:String,ficheId:String, triphase:Triphase, callback:Callback<DemontageTriphaseResponse>){
        var body = BodyDemontageTriphase(
            triphase.status,
            triphase.typeMoteur,
            triphase.marque,
            triphase.numSerie,
            triphase.puissance,
            triphase.bride,
            triphase.vitesse,
            triphase.arbreSortantEntrant,
            triphase.accouplement,
            triphase.coteAccouplement,
            triphase.clavette,
            triphase.aspect,
            triphase.aspectInterieur,
            triphase.couplage,
            triphase.flasqueAvant,
            triphase.flasqueArriere,
            triphase.porteeRAvant,
            triphase.porteeRArriere,
            triphase.boutArbre,
            triphase.rondelleElastique,
            triphase.refRoulementAvant,
            triphase.refRoulementArriere,
            triphase.typeRoulementAvant,
            triphase.typeRoulementArriere,
            triphase.refJointAvant,
            triphase.refJointArriere,
            triphase.typeJointAvant,
            triphase.typeJointArriere,
            triphase.ventilateur,
            triphase.capotV,
            triphase.socleBoiteABorne,
            triphase.capotBoiteABorne,
            triphase.plaqueABorne,
            triphase.presenceSondes,
            triphase.typeSondes,
            triphase.equilibrage,
            triphase.peinture,
            triphase.isolementPhaseMasseStatorUM,
            triphase.isolementPhaseMasseStatorVM,
            triphase.isolementPhaseMasseStatorWM,
            triphase.isolementPhasePhaseStatorUV,
            triphase.isolementPhasePhaseStatorVW,
            triphase.isolementPhasePhaseStatorUW,
            triphase.resistanceStatorU,
            triphase.resistanceStatorV,
            triphase.resistanceStatorW,
            triphase.tensionU,
            triphase.tensionV,
            triphase.tensionW,
            triphase.intensiteU,
            triphase.intensiteV,
            triphase.intensiteW,
            triphase.dureeEssai,
            triphase.dureeTotale!!.toInt(),
            triphase.observations
        )
        var call = service.patchDemontageTriphase(token,ficheId,body)
        var fiche:Triphase? = null
        call.enqueue(callback)
    }

    fun getDemontageCC(token:String,ficheId:String, callback: Callback<DemontageCCResponse>){
        var call = service.getDemontageCC(token,ficheId)
        var fiche:CourantContinu? = null
        call.enqueue(callback)
    }

    fun patchDemontageCC(token:String,ficheId:String, fiche:CourantContinu, callback:Callback<DemontageCCResponse>){
        var body = BodyDemontageCC(
            fiche.status,
            fiche.typeMoteur,
            fiche.marque,
            fiche.numSerie,
            fiche.puissance,
            fiche.bride,
            fiche.vitesse,
            fiche.arbreSortantEntrant,
            fiche.accouplement,
            fiche.coteAccouplement,
            fiche.clavette,
            fiche.aspect,
            fiche.aspectInterieur,
            fiche.couplage,
            fiche.flasqueAvant,
            fiche.flasqueArriere,
            fiche.porteeRAvant,
            fiche.porteeRArriere,
            fiche.boutArbre,
            fiche.rondelleElastique,
            fiche.refRoulementAvant,
            fiche.refRoulementArriere,
            fiche.typeRoulementAvant,
            fiche.typeRoulementArriere,
            fiche.refJointAvant,
            fiche.refJointArriere,
            fiche.typeJointAvant,
            fiche.typeJointArriere,
            fiche.ventilateur,
            fiche.capotV,
            fiche.socleBoiteABorne,
            fiche.capotBoiteABorne,
            fiche.plaqueABorne,
            fiche.presenceSondes,
            fiche.typeSondes,
            fiche.equilibrage,
            fiche.peinture,
            fiche.isolationMasseInduit,
            fiche.isolationMassePolesPrincipaux,
            fiche.isolationMassePolesAuxilliaires,
            fiche.isolationMassePolesCompensatoires,
            fiche.isolationMassePorteBalais,
            fiche.resistanceInduit,
            fiche.resistancePP,
            fiche.resistancePA,
            fiche.resistancePC,
            fiche.tensionInduit,
            fiche.intensiteInduit,
            fiche.tensionExcitation,
            fiche.intensiteExcitation,
            fiche.dureeTotale!!.toInt(),
            fiche.observations
        )
        var call = service.patchDemontageCC(token,ficheId,body)
        var fiche:CourantContinu? = null
        call.enqueue(callback)
    }
    fun getRemontage(token:String,ficheId:String, callback:Callback<RemontageResponse>){
        val call = service.getRemontage(token,ficheId)
        var fiche:Remontage?=null
        call.enqueue(callback)
    }
    fun getRemontageCC(token:String,ficheId:String, callback: Callback<RemontageCCResponse>){
        var call = service.getRemontageCC(token,ficheId)
        var fiche:RemontageCourantC? = null
        call.enqueue(callback)
    }

    fun patchRemontageCC(token:String,ficheId:String, fiche:RemontageCourantC, callback:Callback<RemontageCCResponse>){
        var body = BodyRemontageCC(
            fiche.status!!.toInt(),
            fiche.dureeTotale,
            fiche.observations,
            fiche.remontageRoulement,
            fiche.collageRoulementPorteeArbre,
            fiche.collageRoulementFlasque,
            fiche.verificationFixationCouronne,
            fiche.verificationIsolementPorteBalais,
            fiche.isolementPorteBalaisV,
            fiche.isolementPorteBalaisOhm,
            fiche.tensionStator,
            fiche.tensionStatorU,
            fiche.tensionStatorV,
            fiche.tensionStatorW,
            fiche.tensionInducteurs,
            fiche.tensionInducteursU,
            fiche.tensionInducteursV,
            fiche.tensionInducteursW,
            fiche.intensiteStator,
            fiche.intensiteStatorU,
            fiche.intensiteStatorV,
            fiche.intensiteStatorW,
            fiche.intensiteInducteurs,
            fiche.intensiteInducteursU,
            fiche.intensiteInducteursV,
            fiche.intensiteInducteursW,
            fiche.tensionInduit,
            fiche.tensionInduitU,
            fiche.tensionInduitV,
            fiche.tensionInduitW,
            fiche.tensionRotor,
            fiche.tensionRotorU,
            fiche.tensionRotorV,
            fiche.tensionRotorW,
            fiche.intensiteInduit,
            fiche.intensiteInduitU,
            fiche.vitesseU,
            fiche.puissanceU,
            fiche.dureeEssai,
            fiche.sensRotation,
            fiche.vitesse1V,
            fiche.acceleration1V,
            fiche.vitesse2V,
            fiche.acceleration2V,
            fiche.vitesse1H,
            fiche.acceleration1H,
            fiche.vitesse2H,
            fiche.acceleration2H,
            fiche.vitesse2A,
            fiche.acceleration2A,
            fiche.resistanceInducteurs,
            fiche.resistanceInduit,
            fiche.isolementInducteursMasse,
            fiche.isolementInduitMasse,
            fiche.isolementInduitInducteurs,
            fiche.releveIsoInducteursMasse,
            fiche.releveIsoInduitMasse,
            fiche.releveIsoInduitInducteurs
        )
        Log.i("INFO","tensioninducteurs : ${body.tensionInducteurs}")
        var call = service.patchRemontageCC(token,ficheId,body)
        var fiche:RemontageCourantC? = null
        call.enqueue(callback)
    }
    fun getRemontageTriphase(token:String,ficheId:String, callback: Callback<RemontageTriphaseResponse>){
        var call = service.getRemontageTriphase(token,ficheId)
        var fiche:RemontageTriphase? = null
        call.enqueue(callback)
    }

    fun patchRemontageTriphase(token:String,ficheId:String, fiche:RemontageTriphase, callback:Callback<RemontageTriphaseResponse>){
        var body = BodyRemontageTriphase(
            fiche.status!!.toInt(),
            fiche.dureeTotale,
            fiche.observations,
            fiche.remontageRoulement,
            fiche.collageRoulementPorteeArbre,
            fiche.collageRoulementFlasque,
            fiche.verificationFixationCouronne,
            fiche.verificationIsolementPorteBalais,
            fiche.isolementPorteBalaisV,
            fiche.isolementPorteBalaisOhm,
            fiche.tensionStator,
            fiche.tensionStatorU,
            fiche.tensionStatorV,
            fiche.tensionStatorW,
            fiche.tensionInducteurs,
            fiche.tensionInducteursU,
            fiche.tensionInducteursV,
            fiche.tensionInducteursW,
            fiche.intensiteStator,
            fiche.intensiteStatorU,
            fiche.intensiteStatorV,
            fiche.intensiteStatorW,
            fiche.intensiteInducteurs,
            fiche.intensiteInducteursU,
            fiche.intensiteInducteursV,
            fiche.intensiteInducteursW,
            fiche.tensionInduit,
            fiche.tensionInduitU,
            fiche.tensionInduitV,
            fiche.tensionInduitW,
            fiche.tensionRotor,
            fiche.tensionRotorU,
            fiche.tensionRotorV,
            fiche.tensionRotorW,
            fiche.intensiteInduit,
            fiche.intensiteInduitU,
            fiche.vitesseU,
            fiche.puissanceU,
            fiche.dureeEssai,
            fiche.sensRotation,
            fiche.vitesse1V,
            fiche.acceleration1V,
            fiche.vitesse2V,
            fiche.acceleration2V,
            fiche.vitesse1H,
            fiche.acceleration1H,
            fiche.vitesse2H,
            fiche.acceleration2H,
            fiche.vitesse2A,
            fiche.acceleration2A,
            fiche.isolementPhaseMasse,
            fiche.isolementPhase,
            fiche.resistanceStatorU,
            fiche.resistanceStatorV,
            fiche.resistanceStatorW,
            fiche.isolementPMStatorU,
            fiche.isolementPMStatorV,
            fiche.isolementPMStatorW,
            fiche.isolementPMRotorU,
            fiche.isolementPMRotorV,
            fiche.isolementPMRotorW,
            fiche.isolementPhaseStatorUV,
            fiche.isolementPhaseStatorVW,
            fiche.isolementPhaseStatorUW,
            fiche.isolementPhaseRotorUV,
            fiche.isolementPhaseRotorVW,
            fiche.isolementPhaseRotorUW
        )
        var call = service.patchRemontageTriphase(token,ficheId,body)
        var fiche:RemontageTriphase? = null
        call.enqueue(callback)
    }
    fun patchRemontage(token:String,ficheId:String, fiche:Remontage, callback:Callback<RemontageResponse>){
        var body = BodyRemontage(
            fiche.status!!.toInt(),
            fiche.dureeTotale,
            fiche.observations,
            fiche.remontageRoulement,
            fiche.collageRoulementPorteeArbre,
            fiche.collageRoulementFlasque,
            fiche.verificationFixationCouronne,
            fiche.verificationIsolementPorteBalais,
            fiche.isolementPorteBalaisV,
            fiche.isolementPorteBalaisOhm,
        )
        var call = service.patchRemontage(token,ficheId,body)
        var fiche:Remontage? = null
        call.enqueue(callback)
    }
    fun patchDemontagePompe(token:String,ficheId:String, fiche:DemontagePompe, callback:Callback<DemontagePompeResponse>){
        var body = BodyDemoPompe(
            fiche.status,
            fiche.typeMoteur,
            fiche.marque,
            fiche.numSerie,
            fiche.fluide,
            fiche.sensRotation,
            fiche.typeRessort,
            fiche.typeJoint,
            fiche.matiere,
            fiche.diametreArbre,
            fiche.diametreExtPR,
            fiche.diametreExtPF,
            fiche.epaisseurPF,
            fiche.longueurRotativeNonComprimee,
            fiche.longueurRotativeComprimee,
            fiche.longueurRotativeTravail,
            fiche.observations,
            fiche.dureeTotale
        )
        var call = service.patchDemontagePompe(token,ficheId,body)
        var fiche:DemontagePompe? = null
        call.enqueue(callback)

    }

    fun patchDemontageMono(token:String,ficheId:String, fiche:DemontageMonophase, callback:Callback<DemontageMonophaseResponse>){
        var body = BodyDemontageMonophase(
            fiche.observations,
            fiche.typeMoteur,
            fiche.status,
            fiche.marque,
            fiche.numSerie,
            fiche.puissance,
            fiche.bride,
            fiche.vitesse,
            fiche.arbreSortantEntrant,
            fiche.accouplement,
            fiche.coteAccouplement,
            fiche.clavette,
            fiche.aspect,
            fiche.aspectInterieur,
            fiche.couplage,
            fiche.flasqueAvant,
            fiche.flasqueArriere,
            fiche.porteeRAvant,
            fiche.porteeRArriere,
            fiche.boutArbre,
            fiche.rondelleElastique,
            fiche.refRoulementAvant,
            fiche.refRoulementArriere,
            fiche.typeRoulementAvant,
            fiche.typeRoulementArriere,
            fiche.refJointAvant,
            fiche.refJointArriere,
            fiche.typeJointAvant,
            fiche.typeJointArriere,
            fiche.ventilateur,
            fiche.capotV,
            fiche.socleBoiteABorne,
            fiche.capotBoiteABorne,
            fiche.plaqueABorne,
            fiche.presenceSondes,
            fiche.typeSondes,
            fiche.equilibrage,
            fiche.peinture,
            fiche.isolementPhaseMasse,
            fiche.resistanceTravail,
            fiche.resistanceDemarrage,
            fiche.valeurCondensateur,
            fiche.tension,
            fiche.intensite,
            fiche.dureeTotale!!.toInt()
        )
        var call = service.patchDemontageMonophase(token,ficheId,body)
        var fiche:DemontageMonophase? = null
        call.enqueue(callback)

    }
    fun patchDemontageAlter(
        token:String,
        ficheId:String, fiche:DemontageAlternateur, callback: Callback<DemontageAlternateurResponse>
    ){
        var body = BodyDemontageAlternateur(
            fiche.observations,
            fiche.typeMoteur,
            fiche.status,
            fiche.marque,
            fiche.numSerie,
            fiche.puissance,
            fiche.bride,
            fiche.vitesse,
            fiche.arbreSortantEntrant,
            fiche.accouplement,
            fiche.coteAccouplement,
            fiche.clavette,
            fiche.aspect,
            fiche.aspectInterieur,
            fiche.couplage,
            fiche.flasqueAvant,
            fiche.flasqueArriere,
            fiche.porteeRAvant,
            fiche.porteeRArriere,
            fiche.boutArbre,
            fiche.rondelleElastique,
            fiche.refRoulementAvant,
            fiche.refRoulementArriere,
            fiche.typeRoulementAvant,
            fiche.typeRoulementArriere,
            fiche.refJointAvant,
            fiche.refJointArriere,
            fiche.typeJointAvant,
            fiche.typeJointArriere,
            fiche.ventilateur,
            fiche.capotV,
            fiche.socleBoiteABorne,
            fiche.capotBoiteABorne,
            fiche.plaqueABorne,
            fiche.presenceSondes,
            fiche.typeSondes,
            fiche.equilibrage,
            fiche.peinture,
            fiche.isolementMasseStatorPrincipalU,
            fiche.isolementMasseStatorPrincipalV,
            fiche.isolementMasseStatorPrincipalW,
            fiche.isolementMasseRotorPrincipal,
            fiche.isolementMasseStatorExcitation,
            fiche.isolementMasseRotorExcitation,
            fiche.resistanceStatorPrincipalU,
            fiche.resistanceStatorPrincipalV,
            fiche.resistanceStatorPrincipalW,
            fiche.resistanceRotorPrincipal,
            fiche.resistanceStatorExcitation,
            fiche.resistanceRotorExcitation,
            fiche.isolementPhasePhaseStatorPrincipalUV,
            fiche.isolementPhasePhaseStatorPrincipalVW,
            fiche.isolementPhasePhaseStatorPrincipalUW,
            fiche.testDiode,
            fiche.tensionU,
            fiche.tensionV,
            fiche.tensionW,
            fiche.intensiteU,
            fiche.intensiteV,
            fiche.intensiteW,
            fiche.tensionExcitation,
            fiche.intensiteExcitation,
            fiche.dureeTotale!!.toInt()
        )
        var call = service.patchDemontageAlternateur(token,ficheId,body)
        var fiche:DemontageAlternateur? = null
        call.enqueue(callback)

    }
    fun patchDemontageRotor(token:String,ficheId:String, fiche:DemontageRotorBobine, callback:Callback<DemontageRotorBobineResponse>){
        var body = BodyDemontageRotorBobine(
            fiche.observations,
            fiche.typeMoteur,
            fiche.status,
            fiche.marque,
            fiche.numSerie,
            fiche.puissance,
            fiche.bride,
            fiche.vitesse,
            fiche.arbreSortantEntrant,
            fiche.accouplement,
            fiche.coteAccouplement,
            fiche.clavette,
            fiche.aspect,
            fiche.aspectInterieur,
            fiche.couplage,
            fiche.flasqueAvant,
            fiche.flasqueArriere,
            fiche.porteeRAvant,
            fiche.porteeRArriere,
            fiche.boutArbre,
            fiche.rondelleElastique,
            fiche.refRoulementAvant,
            fiche.refRoulementArriere,
            fiche.typeRoulementAvant,
            fiche.typeRoulementArriere,
            fiche.refJointAvant,
            fiche.refJointArriere,
            fiche.typeJointAvant,
            fiche.typeJointArriere,
            fiche.ventilateur,
            fiche.capotV,
            fiche.socleBoiteABorne,
            fiche.capotBoiteABorne,
            fiche.plaqueABorne,
            fiche.presenceSondes,
            fiche.typeSondes,
            fiche.equilibrage,
            fiche.peinture,
            fiche.isolementPhaseMasseStatorUM	,
            fiche.isolementPhaseMasseStatorVM	,
            fiche.isolementPhaseMasseStatorWM	,
            fiche.isolementPhaseMasseRotorB1M	,
            fiche.isolementPhaseMasseRotorB2M	,
            fiche.isolementPhaseMasseRotorB3M	,
            fiche.isolementPhaseMassePorteBalaisM	,
            fiche.isolementPhasePhaseStatorUV	,
            fiche.isolementPhasePhaseStatorVW	,
            fiche.isolementPhasePhaseStatorUW	,
            fiche.resistanceStatorU	,
            fiche.resistanceStatorV	,
            fiche.resistanceStatorW	,
            fiche.resistanceRotorB1B2	,
            fiche.resistanceRotorB2B2	,
            fiche.resistanceRotorB1B3	,
            fiche.tensionU	,
            fiche.tensionV	,
            fiche.tensionW	,
            fiche.tensionRotor	,
            fiche.intensiteU	,
            fiche.intensiteV	,
            fiche.intensiteW	,
            fiche.intensiteRotor	,
            fiche.dureeEssai,
            fiche.dureeTotale!!.toInt()
        )
        var call = service.patchDemontageRotorBobine(token,ficheId,body)
        var fiche:DemontageRotorBobine? = null
        call.enqueue(callback)

    }

    fun patchBobinage(token:String,ficheId:String, bobinage:Bobinage, callback:Callback<BobinageResponse>){
        if (bobinage.resistanceV == null) {
            bobinage.resistanceV = 0f
        }
        if (bobinage.resistanceW == null) {
            bobinage.resistanceW = 0f
        }
        if (bobinage.isolementUT == null) {
            bobinage.isolementUT = 0f }

        if (bobinage.isolementVT == null ) {
            bobinage.isolementVT = 0f
        }
        if (bobinage.isolementWT == null) {
            bobinage.isolementWT = 0f
        }
        if (bobinage.isolementUV == null) {
            bobinage.isolementUV = 0f
        }
        if (bobinage.isolementUW == null) {
            bobinage.isolementUW = 0f
        }
        if (bobinage.isolementVW == null ){
            bobinage.isolementVW = 0f
        }
        if (bobinage.poids == null){
            bobinage.poids = 0f
        }
        var body = BodyBobinage(
            bobinage.marqueMoteur,
            bobinage.typeBobinage,
            bobinage.vitesse,
            bobinage.puissance,
            bobinage.phases,
            bobinage.frequences,
            bobinage.courant,
            bobinage.nbSpires,
            bobinage.resistanceU,
            bobinage.resistanceV,
            bobinage.resistanceW,
            bobinage.isolementUT,
            bobinage.isolementVT,
            bobinage.isolementWT,
            bobinage.isolementUV,
            bobinage.isolementUW,
            bobinage.isolementVW,
            bobinage.status,
            if (bobinage.calageEncoches !== null){
                bobinage.calageEncoches
            } else false,
            bobinage.sectionsFils!!.toList(),
            if (bobinage.observations !== null){
                bobinage.observations} else "",
            bobinage.poids,
            0,
            bobinage.dureeTotale)
        var call = service.patchBobinage(token,ficheId,body)
        var fiche:Bobinage? = null
        call.enqueue(callback)
    }
    fun patchChantier(token:String,ficheId:String, chantier:Chantier, callback:Callback<ChantierResponse>){
        var body = BodyChantier(chantier.materiel, chantier.objet, chantier.observations, chantier.status, chantier.dureeTotale)
        var call = service.patchChantier(token,ficheId,body)
        var fiche:Chantier? = null
        call.enqueue(callback)
    }
    fun getVehiculeById(token:String, vehiculeId:String, callback:Callback<VehiculesResponse>) {
        var call = service.getVehiculeById(token,vehiculeId)
        var vehicule: Vehicule? = null
        call.enqueue(callback)
    }
    suspend fun createDb(){
      db = Room.databaseBuilder(context, LocalDatabase::class.java, "database-local")
          .build()
      chantierDao = db!!.chantierDao()
      bobinageDao = db!!.bobinageDao()
      remontageTriphaseDao = db!!.remontageTriphaseDao()
      remontageCourantCDao = db!!.remontageCCDao()
      remontageDao = db!!.remontageDao()
      demontageTriphaseDao = db!!.demontageTriphaseDao()
      demontageCCDao = db!!.demontageCCDao()
      demontagePDao = db!!.demontagePDao()
      demontageMonoDao = db!!.demontageMonophaseDao()
      demontageAlterDao = db!!.demontageAlternateurDao()
      demontageRBDao = db!!.demontageRotorBobineDao()
      vehiculeDao = db!!.vehiculesDao()
      clientDao = db!!.clientDao()
        Log.i("INFO","db créée")
    }
    //requêtes chantier
    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun getAllVehiculesLocalDatabase(): List<VehiculeEntity>{
        return vehiculeDao!!.getAll()
    }
    suspend fun insertVehiculesLocalDatabase(vehicule: Vehicule){
        vehiculeDao!!.insertAll(vehicule.toEntity())
    }
    suspend fun getByIdVehiculesLocalDatabse( id: String): Vehicule? {
        if (vehiculeDao!!.getById(id) !== null) {
            return vehiculeDao!!.getById(id).toVehicule()
        } else return null
    }
    suspend fun updateVehiculesLocalDatabse( vehicule: VehiculeEntity){
        vehiculeDao!!.update(vehicule)
    }
    suspend fun deleteVehiculeLocalDatabse( vehicule: VehiculeEntity){
        vehiculeDao!!.delete(vehicule)
    }
    suspend fun getAllClientsLocalDatabase(): List<ClientEntity>{
        return clientDao!!.getAll()
    }
    suspend fun insertClientsLocalDatabase(client: Client){
        clientDao!!.insertAll(client.toEntity())
    }
    suspend fun updateClientsLocalDatabse( client: ClientEntity){
        clientDao!!.update(client)
    }
    suspend fun deleteClientsLocalDatabse( client: ClientEntity){
        clientDao!!.delete(client)
    }
    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun insertChantierLocalDatabase(chantier: Chantier){
        chantierDao!!.insertAll(chantier.toEntity())
    }
   suspend fun getAllChantierLocalDatabase(): List<ChantierEntity>{
        return chantierDao!!.getAll()
    }
    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun getByIdChantierLocalDatabse(id: String): Chantier? {
        if (chantierDao!!.getById(id) !== null) {
            return chantierDao!!.getById(id).toChantier()
        } else return null
    }
    suspend fun updateChantierLocalDatabse( chantier: ChantierEntity){
        chantierDao!!.update(chantier)
    }
    suspend fun deleteChantierLocalDatabse( chantier: ChantierEntity){
        chantierDao!!.delete(chantier)
    }
    //requêtes bobinage
    suspend fun insertBobinageLocalDatabase(bobinage: Bobinage){
        bobinageDao!!.insertAll(bobinage.toEntity())
    }
    suspend fun getAllBobinageLocalDatabase(): List<BobinageEntity>{
        return bobinageDao!!.getAll()
    }
    suspend fun getByIdBobinageLocalDatabse(id: String) : Bobinage? {
        try {
            if (bobinageDao!!.getById(id) !== null) {
                return bobinageDao!!.getById(id).toBobinage()
            } else return null
        } catch (e:Error){
            Log.i("e",e.message!!)
            return null
        }
    }
    suspend fun updateBobinageLocalDatabse( bobinage: BobinageEntity){
        bobinageDao!!.update(bobinage)
    }
    suspend fun deleteBobinageLocalDatabse( bobinage: BobinageEntity){
        bobinageDao!!.delete(bobinage)
    }
    //dao demontage triphase
    suspend fun insertDemoTriLocalDatabase(demo: Triphase){
       demontageTriphaseDao!!.insertAll(demo.toEntity())
    }
    suspend fun getAllDemontageTriLocalDatabase(): List<DemontageTriphaseEntity >{
        return demontageTriphaseDao!!.getAll()
    }
    suspend fun getByIdDemoTriLocalDatabse(id: String) : Triphase? {
        try {
            if (demontageTriphaseDao!!.getById(id) !== null) {
                return demontageTriphaseDao!!.getById(id).toTriphase()
            } else return null
        } catch (e:Error){
            Log.i("e",e.message!!)
            return null
        }
    }
    suspend fun updateDemoTriLocalDatabse( demo: DemontageTriphaseEntity){
        demontageTriphaseDao!!.update(demo)
    }
    suspend fun deleteDemontageTriphaseLocalDatabse( demo: DemontageTriphaseEntity){
        demontageTriphaseDao!!.delete(demo)
    }

    // dao demo courant continu
    suspend fun insertDemoCCLocalDatabase(demo: CourantContinu){
        demontageCCDao!!.insertAll(demo.toEntity())
    }
    suspend fun getAllDemontageCCLocalDatabase(): List<DemontageCCEntity >{
        return demontageCCDao!!.getAll()
    }
    suspend fun getByIdDemoCCLocalDatabse(id: String) : CourantContinu? {
        try {
            if (demontageCCDao!!.getById(id) !== null) {
                return demontageCCDao!!.getById(id).toCContinu()
            } else return null
        } catch (e:Error){
            Log.i("e",e.message!!)
            return null
        }
    }
    suspend fun updateDemoCCLocalDatabse( demo: DemontageCCEntity){
        demontageCCDao!!.update(demo)
    }
    suspend fun deleteDemontageCCLocalDatabse( demo: DemontageCCEntity){
        demontageCCDao!!.delete(demo)
    }
    //demo pompes
    suspend fun insertDemoPompeLocalDatabase(demo: DemontagePompe){
        demontagePDao!!.insertAll(demo.toEntity())
    }
    suspend fun getAllDemontagePompeLocalDatabase(): List<DemoPompeEntity >{
        return demontagePDao!!.getAll()
    }
    suspend fun getByIdDemoPompeLocalDatabse(id: String) : DemontagePompe? {
        try {
            if (demontagePDao!!.getById(id) !== null) {
                return demontagePDao!!.getById(id).toDemoPompe()
            } else return null
        } catch (e:Error){
            Log.i("e",e.message!!)
            return null
        }
    }
    suspend fun updateDemoPompeLocalDatabse( demo: DemoPompeEntity){
        demontagePDao!!.update(demo)
    }
    suspend fun deleteDemontagePompeLocalDatabse( demo: DemoPompeEntity){
        demontagePDao!!.delete(demo)
    }

    //demo mono
    suspend fun insertDemoMonoLocalDatabase(demo: DemontageMonophase){
        demontageMonoDao!!.insertAll(demo.toEntity())
    }
    suspend fun getAllDemontageMonoLocalDatabase(): List<DemontageMonophaseEntity>{
        return demontageMonoDao!!.getAll()
    }
    suspend fun getByIdDemoMonoLocalDatabse(id: String) : DemontageMonophase? {
        try {
            if (demontageMonoDao!!.getById(id) !== null) {
                return demontageMonoDao!!.getById(id).toMonophase()
            } else return null
        } catch (e:Error){
            Log.i("e",e.message!!)
            return null
        }
    }
    suspend fun updateDemoMonoLocalDatabse( demo: DemontageMonophaseEntity){
        demontageMonoDao!!.update(demo)
    }
    suspend fun deleteDemontageMonoLocalDatabse( demo: DemontageMonophaseEntity){
        demontageMonoDao!!.delete(demo)
    }
    //demo Alternateur
    suspend fun insertDemoAlterLocalDatabase(demo: DemontageAlternateur){
        demontageAlterDao!!.insertAll(demo.toEntity())
    }
    suspend fun getAllDemontageAlterLocalDatabase(): List<DemontageAlternateurEntity>{
        return demontageAlterDao!!.getAll()
    }
    suspend fun getByIdDemoAlterLocalDatabse(id: String) : DemontageAlternateur? {
        try {
            if (demontageAlterDao!!.getById(id) !== null) {
                return demontageAlterDao!!.getById(id).toDemontageAlternateur()
            } else return null
        } catch (e:Error){
            Log.i("e",e.message!!)
            return null
        }
    }
    suspend fun updateDemoAlterLocalDatabse( demo: DemontageAlternateurEntity){
        demontageAlterDao!!.update(demo)
    }
    suspend fun deleteDemontageAlterLocalDatabse( demo: DemontageAlternateurEntity){
        demontageAlterDao!!.delete(demo)
    }
    //demo Rotor
    suspend fun insertDemoRBLocalDatabase(demo: DemontageRotorBobine){
        demontageRBDao!!.insertAll(demo.toEntity())
    }
    suspend fun getAllDemontageRBLocalDatabase(): List<DemontageRotorBEntity>{
        return demontageRBDao!!.getAll()
    }
    suspend fun getByIdDemoRBLocalDatabse(id: String) : DemontageRotorBobine? {
        try {
            if (demontageRBDao!!.getById(id) !== null) {
                return demontageRBDao!!.getById(id).toDemoRotorB()
            } else return null
        } catch (e:Error){
            Log.i("e",e.message!!)
            return null
        }
    }
    suspend fun updateDemoRBLocalDatabse( demo: DemontageRotorBEntity){
        demontageRBDao!!.update(demo)
    }
    suspend fun deleteDemontageRBLocalDatabse( demo: DemontageRotorBEntity){
        demontageRBDao!!.delete(demo)
    }

    //dao remontage triphase
    suspend fun insertRemoTriLocalDatabase(remo: RemontageTriphase){
        remontageTriphaseDao!!.insertAll(remo.toEntity())
    }
    suspend fun getAllRemontageTriLocalDatabase(): List<RemontageTriphaseEntity>{
        return remontageTriphaseDao!!.getAll()
    }
    suspend fun getByIdRemoTriLocalDatabse(id: String) : RemontageTriphase? {
        try {
            if (remontageTriphaseDao!!.getById(id) !== null) {
                return remontageTriphaseDao!!.getById(id).toRTriphase()
            } else return null
        } catch (e:Error){
            Log.i("e",e.message!!)
            return null
        }
    }
    suspend fun updateRemoTriLocalDatabse( remo: RemontageTriphaseEntity){
        remontageTriphaseDao!!.update(remo)
    }
    suspend fun deleteRemontageTriphaseLocalDatabse( remo: RemontageTriphaseEntity){
        remontageTriphaseDao!!.delete(remo)
    }
    //dao remontage
    suspend fun insertRemoLocalDatabase(remo: Remontage){
        remontageDao!!.insertAll(remo.toRemoEntity())
    }
    suspend fun getAllRemontageLocalDatabase(): List<RemontageEntity>{
        return remontageDao!!.getAll()
    }
    suspend fun getByIdRemoLocalDatabse(id: String) : Remontage? {
        try {
            if (remontageDao!!.getById(id) !== null) {
                return remontageDao!!.getById(id).toRemo()
            } else return null
        } catch (e:Error){
            Log.i("e",e.message!!)
            return null
        }
    }
    suspend fun updateRemoLocalDatabse( remo: RemontageEntity){
        remontageDao!!.update(remo)
    }
    suspend fun deleteRemontageLocalDatabse( remo: RemontageEntity){
        remontageDao!!.delete(remo)
    }

    // dao remo courant continu
    suspend fun insertRemoCCLocalDatabase(remo: RemontageCourantC){
        remontageCourantCDao!!.insertAll(remo.toEntity())
    }
    suspend fun getAllRemontageCCLocalDatabase(): List<RemontageCCEntity>{
        return remontageCourantCDao!!.getAll()
    }
    suspend fun getByIdRemoCCLocalDatabse(id: String) : RemontageCourantC? {
        try {
            if (remontageCourantCDao!!.getById(id) !== null) {
                return remontageCourantCDao!!.getById(id).toRCourantC()
            } else return null
        } catch (e:Error){
            Log.i("e",e.message!!)
            return null
        }
    }
    suspend fun updateRemoCCLocalDatabse( remo: RemontageCCEntity){
        remontageCourantCDao!!.update(remo)
    }
    suspend fun deleteRemontageCCLocalDatabse( remo: RemontageCCEntity){
        remontageCourantCDao!!.delete(remo)
    }
}