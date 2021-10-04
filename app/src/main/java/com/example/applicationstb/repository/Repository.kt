package com.example.applicationstb.repository

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.applicationstb.localdatabase.*
import com.example.applicationstb.model.*
import com.squareup.moshi.*
import kotlinx.parcelize.Parcelize
import org.json.JSONArray
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.text.SimpleDateFormat
import java.util.*
import kotlin.coroutines.coroutineContext

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

class BodyChantier(var materiel: String?, var objet: String?, var observations: String?, var status: Long?): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readLong()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(materiel)
        parcel.writeString(objet)
        parcel.writeString(observations)
        parcel.writeLong(status!!)
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
    var tension:Long?
                  ): Parcelable {
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
        parcel.readLong()
    ) {
    }

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
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BodyBobinage> {
        override fun createFromParcel(parcel: Parcel): BodyBobinage {
            return BodyBobinage(parcel)
        }

        override fun newArray(size: Int): Array<BodyBobinage?> {
            return arrayOfNulls(size)
        }
    }
}

class BodyDemontageTriphase ( var marque: String?,
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
                      var refRoulementAvant: String?,
                      var refRoulementArriere: String?,
                      var typeRoulementAvant: String ?,
                      var typeRoulementArriere: String ?,
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
): Parcelable {
    constructor(parcel: Parcel) : this(
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
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
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
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
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
        parcel.writeString(refRoulementAvant!!)
        parcel.writeString(refRoulementArriere!!)
        parcel.writeString(typeRoulementAvant!!)
        parcel.writeString(typeRoulementArriere!!)
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
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BodyDemontageTriphase> {
        override fun createFromParcel(parcel: Parcel): BodyDemontageTriphase {
            return BodyDemontageTriphase(parcel)
        }

        override fun newArray(size: Int): Array<BodyDemontageTriphase?> {
            return arrayOfNulls(size)
        }
    }
}

class BodyDemontageCC ( var marque: String?,
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
                        var refRoulementAvant: String?,
                        var refRoulementArriere: String?,
                        var typeRoulementAvant: String ?,
                        var typeRoulementArriere: String ?,
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
): Parcelable {
    constructor(parcel: Parcel) : this(
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
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
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
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
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
        parcel.writeString(refRoulementAvant!!)
        parcel.writeString(refRoulementArriere!!)
        parcel.writeString(typeRoulementAvant!!)
        parcel.writeString(typeRoulementArriere!!)
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
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BodyDemontageCC> {
        override fun createFromParcel(parcel: Parcel): BodyDemontageCC {
            return BodyDemontageCC(parcel)
        }

        override fun newArray(size: Int): Array<BodyDemontageCC?> {
            return arrayOfNulls(size)
        }
    }
}

class BodyRemontageTriphase (  var remontageRoulement: Int?,
                               var collageRoulementPorteeArbre: Int?,
                               var collageRoulementPorteeFlasque: Int?,
                               var verificationFixationCouronne: Boolean?,
                               var verificationIsolementPorteBalais: Boolean?,
                               var isolementPorteBalaisV: Int?,
                               var isolementPorteBalaisOhm: Int?,
                               var tensionStatorInducteurs: Boolean?,
                               var tensionStatorInducteursU: Float?,
                               var tensionStatorInducteursV: Float?,
                               var tensionStatorInducteursW: Float?,
                               var intensiteStatorInducteur: Boolean?,
                               var intensiteStatorInducteurU: Float?,
                               var intensiteStatorInducteurV: Float?,
                               var intensiteStatorInducteurW: Float?,
                               var tensionInduitRotor: Boolean?,
                               var tensionInduitRotorU: Float?,
                               var tensionInduitRotorV: Float?,
                               var tensionInduitRotorW: Float?,
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
    constructor(parcel: Parcel) : this(
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

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(remontageRoulement!!)
        parcel.writeInt(collageRoulementPorteeArbre!!)
        parcel.writeInt(collageRoulementPorteeFlasque!!)
        parcel.writeBoolean(verificationFixationCouronne!!)
        parcel.writeBoolean(verificationIsolementPorteBalais!!)
        parcel.writeInt(isolementPorteBalaisV!!)
        parcel.writeInt(isolementPorteBalaisOhm!!)
        parcel.writeBoolean(tensionStatorInducteurs!!)
        parcel.writeFloat(tensionStatorInducteursU!!)
        parcel.writeFloat(tensionStatorInducteursV!!)
        parcel.writeFloat(tensionStatorInducteursW!!)
        parcel.writeBoolean(intensiteStatorInducteur!!)
        parcel.writeFloat(intensiteStatorInducteurU!!)
        parcel.writeFloat(intensiteStatorInducteurV!!)
        parcel.writeFloat(intensiteStatorInducteurW!!)
        parcel.writeBoolean(tensionInduitRotor!!)
        parcel.writeFloat(tensionInduitRotorU!!)
        parcel.writeFloat(tensionInduitRotorV!!)
        parcel.writeFloat(tensionInduitRotorW!!)
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
        override fun createFromParcel(parcel: Parcel): BodyRemontageTriphase {
            return BodyRemontageTriphase(parcel)
        }

        override fun newArray(size: Int): Array<BodyRemontageTriphase?> {
            return arrayOfNulls(size)
        }
    }
}

class BodyRemontageCC (  var remontageRoulement: Int?,
                               var collageRoulementPorteeArbre: Int?,
                               var collageRoulementPorteeFlasque: Int?,
                               var verificationFixationCouronne: Boolean?,
                               var verificationIsolementPorteBalais: Boolean?,
                               var isolementPorteBalaisV: Int?,
                               var isolementPorteBalaisOhm: Int?,
                               var tensionStatorInducteurs: Boolean?,
                               var tensionStatorInducteursU: Float?,
                               var tensionStatorInducteursV: Float?,
                               var tensionStatorInducteursW: Float?,
                               var intensiteStatorInducteur: Boolean?,
                               var intensiteStatorInducteurU: Float?,
                               var intensiteStatorInducteurV: Float?,
                               var intensiteStatorInducteurW: Float?,
                               var tensionInduitRotor: Boolean?,
                               var tensionInduitRotorU: Float?,
                               var tensionInduitRotorV: Float?,
                               var tensionInduitRotorW: Float?,
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
    constructor(parcel: Parcel) : this(
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

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(remontageRoulement!!)
        parcel.writeInt(collageRoulementPorteeArbre!!)
        parcel.writeInt(collageRoulementPorteeFlasque!!)
        parcel.writeBoolean(verificationFixationCouronne!!)
        parcel.writeBoolean(verificationIsolementPorteBalais!!)
        parcel.writeInt(isolementPorteBalaisV!!)
        parcel.writeInt(isolementPorteBalaisOhm!!)
        parcel.writeBoolean(tensionStatorInducteurs!!)
        parcel.writeFloat(tensionStatorInducteursU!!)
        parcel.writeFloat(tensionStatorInducteursV!!)
        parcel.writeFloat(tensionStatorInducteursW!!)
        parcel.writeBoolean(intensiteStatorInducteur!!)
        parcel.writeFloat(intensiteStatorInducteurU!!)
        parcel.writeFloat(intensiteStatorInducteurV!!)
        parcel.writeFloat(intensiteStatorInducteurW!!)
        parcel.writeBoolean(tensionInduitRotor!!)
        parcel.writeFloat(tensionInduitRotorU!!)
        parcel.writeFloat(tensionInduitRotorV!!)
        parcel.writeFloat(tensionInduitRotorW!!)
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
class RemontageTriphaseResponse(
    var fiche: RemontageTriphase?
)
class RemontageCCResponse(
    var fiche: RemontageCourantC?
)
class VehiculesResponse(
    var vehicule:Vehicule?
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
    val retrofit = Retrofit.Builder()
        .baseUrl(url)
        .addConverterFactory(MoshiConverterFactory.create(moshiBuilder.build()))
        .build()
    val service : APIstb by lazy {  retrofit.create(APIstb::class.java) }
    var db : LocalDatabase? = null;
    var chantierDao : ChantierDao? = null;
    var bobinageDao : BobinageDao ? = null;
    var demontageTriphaseDao : DemontageTriphaseDao? = null;
    var demontageCCDao : DemontageCCDao? = null;
    var remontageTriphaseDao: RemontageTriphaseDao? = null;
    var remontageCourantCDao: RemontageCCDao? = null;

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
    fun getDemontageTriphase(token:String,ficheId:String, callback: Callback<DemontageTriphaseResponse>){
        var call = service.getDemontageTriphase(token,ficheId)
        var fiche:Triphase? = null
        call.enqueue(callback)
    }
    fun patchDemontageTriphase(token:String,ficheId:String, triphase:Triphase, callback:Callback<DemontageTriphaseResponse>){
        var body = BodyDemontageTriphase(
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
            triphase.refJointAvant,
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
            triphase.dureeEssai
        )
        var call = service.patchDemontageTriphase(token,ficheId,body)
        var fiche:Chantier? = null
        call.enqueue(callback)
    }

    fun getDemontageCC(token:String,ficheId:String, callback: Callback<DemontageCCResponse>){
        var call = service.getDemontageCC(token,ficheId)
        var fiche:CourantContinu? = null
        call.enqueue(callback)
    }

    fun patchDemontageCC(token:String,ficheId:String, fiche:CourantContinu, callback:Callback<DemontageCCResponse>){
        var body = BodyDemontageCC(
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
            fiche.refJointAvant,
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
            fiche.intensiteExcitation
        )
        var call = service.patchDemontageCC(token,ficheId,body)
        var fiche:CourantContinu? = null
        call.enqueue(callback)
    }
    fun getRemontageCC(token:String,ficheId:String, callback: Callback<RemontageCCResponse>){
        var call = service.getRemontageCC(token,ficheId)
        var fiche:RemontageCourantC? = null
        call.enqueue(callback)
    }

    fun patchRemontageCC(token:String,ficheId:String, fiche:RemontageCourantC, callback:Callback<RemontageCCResponse>){
        var body = BodyRemontageCC(
            fiche.remontageRoulement,
            fiche.collageRoulementPorteeArbre,
            fiche.collageRoulementPorteeFlasque,
            fiche.verificationFixationCouronne,
            fiche.verificationIsolementPorteBalais,
            fiche.isolementPorteBalaisV,
            fiche.isolementPorteBalaisOhm,
            fiche.tensionStatorInducteurs,
            fiche.tensionStatorInducteursU,
            fiche.tensionStatorInducteursV,
            fiche.tensionStatorInducteursW,
            fiche.intensiteStatorInducteur,
            fiche.intensiteStatorInducteurU,
            fiche.intensiteStatorInducteurV,
            fiche.intensiteStatorInducteurW,
            fiche.tensionInduitRotor,
            fiche.tensionInduitRotorU,
            fiche.tensionInduitRotorV,
            fiche.tensionInduitRotorW,
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
            fiche.remontageRoulement,
            fiche.collageRoulementPorteeArbre,
            fiche.collageRoulementPorteeFlasque,
            fiche.verificationFixationCouronne,
            fiche.verificationIsolementPorteBalais,
            fiche.isolementPorteBalaisV,
            fiche.isolementPorteBalaisOhm,
            fiche.tensionStatorInducteurs,
            fiche.tensionStatorInducteursU,
            fiche.tensionStatorInducteursV,
            fiche.tensionStatorInducteursW,
            fiche.intensiteStatorInducteur,
            fiche.intensiteStatorInducteurU,
            fiche.intensiteStatorInducteurV,
            fiche.intensiteStatorInducteurW,
            fiche.tensionInduitRotor,
            fiche.tensionInduitRotorU,
            fiche.tensionInduitRotorV,
            fiche.tensionInduitRotorW,
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
            0)
        var call = service.patchBobinage(token,ficheId,body)
        var fiche:Bobinage? = null
        call.enqueue(callback)
    }
    fun patchChantier(token:String,ficheId:String, chantier:Chantier, callback:Callback<ChantierResponse>){
        var body = BodyChantier(chantier.materiel, chantier.objet, chantier.observations, chantier.status)
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
      demontageTriphaseDao = db!!.demontageTriphaseDao()
      demontageCCDao = db!!.demontageCCDao()
        Log.i("INFO","db créée")
    }

    suspend fun insertChantierLocalDatabase(chantier: Chantier){
        chantierDao!!.insertAll(chantier.toEntity())
    }
   suspend fun getAllChantierLocalDatabase(): List<ChantierEntity>{
        return chantierDao!!.getAll()
    }
    suspend fun getByIdChantierLocalDatabse( id: String): Chantier? {
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
}