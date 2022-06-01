package com.example.applicationstb.repository

import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.applicationstb.localdatabase.*
import com.example.applicationstb.model.*
import retrofit2.Callback

class BodyRemontageTriphase(
    var status: Int?,
    var dureeTotale: Long?,
    var observations: String?,
    var remontageRoulement: Int?,
    var collageRoulementPorteeArbre: Int?,
    var collageRoulementFlasque: Int?,
    var isolementPhaseMasse: Float?,
    var isolementPhase: Float?,
    var resistanceStatorU: Float?,
    var resistanceStatorV: Float?,
    var resistanceStatorW: Float?,
    var isolementPMRotorU: Float?,
    var isolementPMRotorV: Float?,
    var isolementPMRotorW: Float?,
    var vitesseU: Float?,
    var puissanceU: Float?,
    var sensRotation: Int?,
    var vitesse1V: Float?,  // vitesse 1v
    var acceleration1V: Float?,  //acceleration 1v
    var vitesse2V: Float?,  // vitesse 2v
    var acceleration2V: Float?,  //acceleration 2v
    var vitesse1H: Float?,  // vitesse 1H
    var acceleration1H: Float?,  //acceleration 1H
    var vitesse2H: Float?,  // vitesse 2H
    var acceleration2H: Float?,  //acceleration 2H
    var vitesse2A: Float?,  // vitesse 2A
    var acceleration2A: Float?,  //acceleration 2A
    var tension: Float?,
    var intensiteU: Float?,
    var intensiteV: Float?,
    var intensiteW: Float?,
    var dureeEssai: Float?,
    var photos: Array<String>?
) : Parcelable {
    @RequiresApi(Build.VERSION_CODES.Q)
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readLong(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt(),
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
        arrayOf<String>().apply {
            parcel.readArray(String::class.java.classLoader)
        }
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
        parcel.writeFloat(isolementPhaseMasse!!)
        parcel.writeFloat(isolementPhase!!)
        parcel.writeFloat(resistanceStatorU!!)
        parcel.writeFloat(resistanceStatorV!!)
        parcel.writeFloat(resistanceStatorW!!)
        parcel.writeFloat(isolementPMRotorU!!)
        parcel.writeFloat(isolementPMRotorV!!)
        parcel.writeFloat(isolementPMRotorW!!)
        parcel.writeFloat(vitesseU!!)
        parcel.writeFloat(puissanceU!!)
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
        parcel.writeFloat(tension!!)
        parcel.writeFloat(intensiteU!!)
        parcel.writeFloat(intensiteV!!)
        parcel.writeFloat(intensiteW!!)
        parcel.writeFloat(dureeEssai!!)
        arrayOf<String>().apply {
            parcel.writeArray(photos)
        }
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
    var photos: Array<String>?,
    var subtype: Int?,
    var remontageRoulement: Int?,
    var collageRoulementPorteeArbre: Int?,
    var collageRoulementFlasque: Int?,
    var dureeEssai: Int?,
    //fiches 2/3/5/6/7/9
    var isolementPhaseMasseU: String?,
    var isolementPhaseMasseV: String?,
    var isolementPhaseMasseW: String?,
    var isolementPhasePhaseU: String?,
    var isolementPhasePhaseV: String?,
    var isolementPhasePhaseW: String?,
    var resistanceStatorU: String?,
    var resistanceStatorV: String?,
    var resistanceStatorW: String?,
    var tension: String?,
    var intensiteU: String?,
    var intensiteV: String?,
    var intensiteW: String?,
    // fiches 3/5/6/8/9
    var isolementPhase: String?,
    //fiches 2
    var resistanceTravail: String?,
    var resistanceDemarrage: String?,
    //fiches 3
    var isolementMasseStatorPrincipalU: String?,
    var isolementMasseStatorPrincipalV: String?,
    var isolementMasseStatorPrincipalW: String?,
    var isolementMasseRotorPrincipal: String?,
    var isolementMasseStatorExcitation: String?,
    var isolementMasseRotorExcitation: String?,
    var resistanceRotorPrincipal: String?,
    var resistanceStatorExcitation: String?,
    var resistanceRotorExcitation: String?,
    //fiches 5
    var verificationFixationCouronne: Boolean?,
    var isolementPorteBalais: String?,
    var isolementPhaseMasseRotorU: String?,
    var isolementPhaseMasseRotorV: String?,
    var isolementPhaseMasseRotorW: String?,
    var isolementPhaseRotorUV: String?,
    var isolementPhaseRotorVW: String?,
    var isolementPhaseRotorUW: String?,
    var isolementInduit: String?,
    var isolementPolePrincipal: String?,
    var isolementPoleAuxilliaire: String?,
    var isolementPoleCompensatoire: String?,
    var resistanceInduit: String?,
    var resistancePolePrincipal: String?,
    var resistancePoleAuxilliaire: String?,
    var resistancePoleCompensatoire: String?,
    var tensionInducteursU: String?,
    var tensionInducteursV: String?,
    var tensionInducteursW: String?,
    var intensiteInducteursU: String?,
    var intensiteInducteursV: String?,
    var intensiteInducteursW: String?,
    var tensionInduitU: String?,
    var tensionInduitV: String?,
    var tensionInduitW: String?,
    var intensiteInduit: String?,
    var tensionRotorU: String?,
    var tensionRotorV: String?,
    var tensionRotorW: String?,
    var tensionExcitation: String?,
    var intensiteExcitation: String?,
    //fiches 7
    var typeMotopompe: Int?,
    //fiches 9
    var typeMotoreducteur: Int?
) : Parcelable {
    @RequiresApi(Build.VERSION_CODES.Q)
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readLong(),
        parcel.readString(),
        arrayOf<String>().apply {
            parcel.readArray(String::class.java.classLoader)
        },
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readBoolean(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt()
    ) {
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(status!!)
        parcel.writeLong(dureeTotale!!)
        parcel.writeString(observations!!)
        arrayOf<String>().apply {
            parcel.writeArray(photos)
        }
        parcel.writeInt(subtype!!)
        parcel.writeInt(remontageRoulement!!)
        parcel.writeInt(collageRoulementPorteeArbre!!)
        parcel.writeInt(collageRoulementFlasque!!)
        parcel.writeInt(dureeEssai!!)
        parcel.writeString(isolementPhaseMasseU!!)
        parcel.writeString(isolementPhaseMasseV!!)
        parcel.writeString(isolementPhaseMasseW!!)
        parcel.writeString(isolementPhasePhaseU!!)
        parcel.writeString(isolementPhasePhaseV!!)
        parcel.writeString(isolementPhasePhaseW!!)
        parcel.writeString(resistanceStatorU!!)
        parcel.writeString(resistanceStatorV!!)
        parcel.writeString(resistanceStatorW!!)
        parcel.writeString(tension!!)
        parcel.writeString(intensiteU!!)
        parcel.writeString(intensiteV!!)
        parcel.writeString(intensiteW!!)
        parcel.writeString(isolementPhase!!)
        parcel.writeString(resistanceTravail!!)
        parcel.writeString(resistanceDemarrage!!)
        parcel.writeString(isolementMasseStatorPrincipalU!!)
        parcel.writeString(isolementMasseStatorPrincipalV!!)
        parcel.writeString(isolementMasseStatorPrincipalW!!)
        parcel.writeString(isolementMasseRotorPrincipal)
        parcel.writeString(isolementMasseStatorExcitation)
        parcel.writeString(isolementMasseRotorExcitation)
        parcel.writeString(resistanceRotorPrincipal!!)
        parcel.writeString(resistanceStatorExcitation!!)
        parcel.writeString(resistanceRotorExcitation!!)
        parcel.writeBoolean(verificationFixationCouronne!!)
        parcel.writeString(isolementPorteBalais!!)
        parcel.writeString(isolementPhaseMasseRotorU!!)
        parcel.writeString(isolementPhaseMasseRotorV!!)
        parcel.writeString(isolementPhaseMasseRotorW!!)
        parcel.writeString(isolementPhaseRotorUV!!)
        parcel.writeString(isolementPhaseRotorVW!!)
        parcel.writeString(isolementPhaseRotorUW!!)
        parcel.writeString(isolementInduit!!)
        parcel.writeString(isolementPolePrincipal!!)
        parcel.writeString(isolementPoleAuxilliaire!!)
        parcel.writeString(isolementPoleCompensatoire!!)
        parcel.writeString(resistanceInduit!!)
        parcel.writeString(resistancePolePrincipal!!)
        parcel.writeString(resistancePoleAuxilliaire!!)
        parcel.writeString(resistancePoleCompensatoire!!)
        parcel.writeString(tensionInducteursU!!)
        parcel.writeString(tensionInducteursV!!)
        parcel.writeString(tensionInducteursW!!)
        parcel.writeString(intensiteInducteursU!!)
        parcel.writeString(intensiteInducteursV!!)
        parcel.writeString(intensiteInducteursW!!)
        parcel.writeString(tensionInduitU!!)
        parcel.writeString(tensionInduitV!!)
        parcel.writeString(tensionInduitW!!)
        parcel.writeString(intensiteInduit!!)
        parcel.writeString(tensionRotorU!!)
        parcel.writeString(tensionRotorV!!)
        parcel.writeString(tensionRotorW!!)
        parcel.writeString(tensionExcitation!!)
        parcel.writeString(intensiteExcitation!!)
        parcel.writeInt(typeMotopompe!!)
        parcel.writeInt(typeMotoreducteur!!)
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

class BodyRemontageMotopompe(
    var status: Int?,
    var observations: String?,
    var dureeTotale: Long?,
    var photos: Array<String>?,
    var remontageRoulement: Int?,
    var collageRoulementPorteeArbre: Int?,
    var collageRoulementFlasque: Int?,
    var verificationFixationCouronne: Boolean?,
    var verificationIsolementPorteBalais: Boolean?,
    var isolementPorteBalaisV: Float?,
    var isolementPorteBalaisOhm: Float?,
    var tensionStator: Boolean?,
    var tensionStatorU: Float?,
    var tensionStatorV: Float?,
    var tensionStatorW: Float?,
    var tensionInducteurs: Boolean?,
    var tensionInducteursU: Float?,
    var tensionInducteursV: Float?,
    var tensionInducteursW: Float?,
    var intensiteStator: Boolean?,
    var intensiteStatorU: Float?,
    var intensiteStatorV: Float?,
    var intensiteStatorW: Float?,
    var intensiteInducteurs: Boolean?,
    var intensiteInducteursU: Float?,
    var intensiteInducteursV: Float?,
    var intensiteInducteursW: Float?,
    var tensionInduit: Boolean?,
    var tensionInduitU: Float?,
    var tensionInduitV: Float?,
    var tensionInduitW: Float?,
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
    var acceleration1V: Float?,  //acceleration 1v
    var vitesse2V: Float?,  // vitesse 2v
    var acceleration2V: Float?,  //acceleration 2v
    var vitesse1H: Float?,  // vitesse 1H
    var acceleration1H: Float?,  //acceleration 1H
    var vitesse2H: Float?,  // vitesse 2H
    var acceleration2H: Float?,  //acceleration 2H
    var vitesse2A: Float?,  // vitesse 2A
    var acceleration2A: Float?,  //acceleration 2A
    var typeMotopompe: String?,
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
    var isolementPhaseRotorUW: Float?

) : Parcelable {
    @RequiresApi(Build.VERSION_CODES.Q)
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readLong(),
        arrayOf<String>().apply {
            parcel.readArray(String::class.java.classLoader)
        },
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readBoolean(),
        parcel.readBoolean(),
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
    ) {
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(status!!)
        parcel.writeString(observations!!)
        parcel.writeLong(dureeTotale!!)
        arrayOf<String>().apply {
            parcel.writeArray(photos)
        }
        parcel.writeInt(remontageRoulement!!)
        parcel.writeInt(collageRoulementPorteeArbre!!)
        parcel.writeInt(collageRoulementFlasque!!)
        parcel.writeBoolean(verificationFixationCouronne!!)
        parcel.writeBoolean(verificationIsolementPorteBalais!!)
        parcel.writeFloat(isolementPorteBalaisV!!)
        parcel.writeFloat(isolementPorteBalaisOhm!!)
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

    companion object CREATOR : Parcelable.Creator<BodyRemontageMotopompe> {
        @RequiresApi(Build.VERSION_CODES.Q)
        override fun createFromParcel(parcel: Parcel): BodyRemontageMotopompe {
            return BodyRemontageMotopompe(parcel)
        }

        override fun newArray(size: Int): Array<BodyRemontageMotopompe?> {
            return arrayOfNulls(size)
        }
    }
}

class BodyRemontageMotoreducteur(
    var status: Int?,
    var observations: String?,
    var dureeTotale: Long?,
    var photos: Array<String>?,
    var remontageRoulement: Int?,
    var collageRoulementPorteeArbre: Int?,
    var collageRoulementFlasque: Int?,
    var typeMotoreducteur: String?,
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
    var isolementPhaseRotorUW: Float?

) : Parcelable {
    @RequiresApi(Build.VERSION_CODES.Q)
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readLong(),
        arrayOf<String>().apply {
            parcel.readArray(String::class.java.classLoader)
        },
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
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
    ) {
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(status!!)
        parcel.writeString(observations!!)
        parcel.writeLong(dureeTotale!!)
        arrayOf<String>().apply {
            parcel.writeArray(photos)
        }
        parcel.writeInt(remontageRoulement!!)
        parcel.writeInt(collageRoulementPorteeArbre!!)
        parcel.writeInt(collageRoulementFlasque!!)
        parcel.writeString(typeMotoreducteur!!)
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

    companion object CREATOR : Parcelable.Creator<BodyRemontageMotoreducteur> {
        @RequiresApi(Build.VERSION_CODES.Q)
        override fun createFromParcel(parcel: Parcel): BodyRemontageMotoreducteur {
            return BodyRemontageMotoreducteur(parcel)
        }

        override fun newArray(size: Int): Array<BodyRemontageMotoreducteur?> {
            return arrayOfNulls(size)
        }
    }
}

class BodyRemontageReducteur(
    var status: Int?,
    var dureeTotale: Long?,
    var observations: String?,
    var photos: Array<String>?,
    var remontageRoulement: Int?,
    var collageRoulementPorteeArbre: Int?,
    var collageRoulementFlasque: Int?,
) : Parcelable {
    @RequiresApi(Build.VERSION_CODES.Q)
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readLong(),
        parcel.readString(),
        arrayOf<String>().apply {
            parcel.readArray(String::class.java.classLoader)
        },
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
    ) {
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(status!!)
        parcel.writeLong(dureeTotale!!)
        parcel.writeString(observations!!)
        arrayOf<String>().apply {
            parcel.writeArray(photos)
        }
        parcel.writeInt(remontageRoulement!!)
        parcel.writeInt(collageRoulementPorteeArbre!!)
        parcel.writeInt(collageRoulementFlasque!!)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BodyRemontageReducteur> {
        @RequiresApi(Build.VERSION_CODES.Q)
        override fun createFromParcel(parcel: Parcel): BodyRemontageReducteur {
            return BodyRemontageReducteur(parcel)
        }

        override fun newArray(size: Int): Array<BodyRemontageReducteur?> {
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
    var isolementPhaseMasse: String?,
    var isolementPhase: String?,
    var resistanceStatorU: String?,
    var resistanceStatorV: String?,
    var resistanceStatorW: String?,
    var isolementPhasePhaseU: String?,
    var isolementPhasePhaseV: String?,
    var isolementPhasePhaseW: String?,
    var vitesseU: String?,
    var puissanceU: String?,
    var sensRotation: String?,
    //champs specifiques
    var verificationFixationCouronne: Boolean?,
    var isolementPhaseMasseRotorU: String?,
    var isolementPhaseMasseRotorV: String?,
    var isolementPhaseMasseRotorW: String?,
    var isolementPhaseRotorUV: String?,
    var isolementPhaseRotorVW: String?,
    var isolementPhaseRotorUW: String?,
    var tensionInducteursU: String?,
    var tensionInducteursV: String?,
    var tensionInducteursW: String?,
    var intensiteInducteursU: String?,
    var intensiteInducteursV: String?,
    var intensiteInducteursW: String?,
    var tensionInduitU: String?,
    var tensionInduitV: String?,
    var tensionInduitW: String?,
    var tensionRotorU: String?,
    var tensionRotorV: String?,
    var tensionRotorW: String?,
    var tensionUExcitation: String?,
    var intensiteUExcitation: String?,
    var isolementInduit: String?,
    var isolementPolePrincipal: Float?,
    var isolementPoleAuxilliaire: Float?,
    var isolementPoleCompensatoire: Float?,
    var isolementBoiteBalais: Float?,
    var resistanceInduit: Float?,
    var resistancePolePrincipal: Float?,
    var resistancePoleAuxilliaire: Float?,
    var resistancePoleCompensatoire: Float?,
    var intensiteInduit: Float?,
    var vitesse1V: Float?,  // vitesse 1v
    var acceleration1V: Float?,  //acceleration 1v
    var vitesse2V: Float?,  // vitesse 2v
    var acceleration2V: Float?,  //acceleration 2v
    var vitesse1H: Float?,  // vitesse 1H
    var acceleration1H: Float?,  //acceleration 1H
    var vitesse2H: Float?,  // vitesse 2H
    var acceleration2H: Float?,  //acceleration 2H
    var vitesse2A: Float?,  // vitesse 2A
    var acceleration2A: Float?,  //acceleration 2A
    var photos: Array<String>?
) : Parcelable {
    @RequiresApi(Build.VERSION_CODES.Q)
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readLong(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readBoolean(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
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
        arrayOf<String>().apply {
            parcel.readArray(String::class.java.classLoader)
        }
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
        parcel.writeString(isolementPhaseMasse!!)
        parcel.writeString(isolementPhase!!)
        parcel.writeString(resistanceStatorU!!)
        parcel.writeString(resistanceStatorV!!)
        parcel.writeString(resistanceStatorW!!)
        parcel.writeString(isolementPhasePhaseU!!)
        parcel.writeString(isolementPhasePhaseV!!)
        parcel.writeString(isolementPhasePhaseW!!)
        parcel.writeString(vitesseU!!)
        parcel.writeString(puissanceU!!)
        parcel.writeString(sensRotation!!)
        parcel.writeBoolean(verificationFixationCouronne!!)
        parcel.writeString(isolementPhaseMasseRotorU!!)
        parcel.writeString(isolementPhaseMasseRotorV!!)
        parcel.writeString(isolementPhaseMasseRotorW!!)
        parcel.writeString(isolementPhaseRotorUV!!)
        parcel.writeString(isolementPhaseRotorVW!!)
        parcel.writeString(isolementPhaseRotorUW!!)
        parcel.writeString(tensionInducteursU!!)
        parcel.writeString(tensionInducteursV!!)
        parcel.writeString(tensionInducteursW!!)
        parcel.writeString(tensionInduitU!!)
        parcel.writeString(tensionInduitV!!)
        parcel.writeString(tensionInduitW!!)
        parcel.writeString(tensionRotorU!!)
        parcel.writeString(tensionRotorV!!)
        parcel.writeString(tensionRotorW!!)
        parcel.writeString(tensionUExcitation!!)
        parcel.writeString(intensiteUExcitation!!)
        parcel.writeString(isolementInduit!!)
        parcel.writeFloat(isolementPolePrincipal!!)
        parcel.writeFloat(isolementPoleAuxilliaire!!)
        parcel.writeFloat(isolementPoleCompensatoire!!)
        parcel.writeFloat(intensiteInduit!!)
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
        arrayOf<String>().apply {
            parcel.writeArray(photos)
        }
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

class RemontageResponse(
    var data: FicheRemontage?
)

class RemontageTriphaseResponse(
    var data: RemontageTriphase?
)

class RemontageCCResponse(
    var data: RemontageCourantC?
)

class RemontageMotoreducteurResponse(
    var data: RemontageMotoreducteur?
)

class RemontageMotopompeResponse(
    var data: RemontageMotopompe?
)

class RemontageReducteurResponse(
    var data: RemontageReducteur?
)

class RemontageRepository(var service: APIstb, var db: LocalDatabase) {
    var remontageDao = db!!.remontageDao()
    var remontageTriphaseDao = db!!.remontageTriphaseDao()
    var remontageCourantCDao = db!!.remontageCCDao()
    var remontageMotopompeDao = db!!.remontageMotopompeDao()
    var remontageMotoreducteurDao = db!!.remontageMotoreducteurDao()
    fun getFichesForRemontage(
        token: String,
        numDevis: String,
        callback: Callback<DemontagesResponse>
    ) {
        var call = service.getFichesForRemontage(token, numDevis)
        var fiches: Array<DemontageMoteur>? = null
        call.enqueue(callback)
    }

    fun getRemontage(token: String, ficheId: String, callback: Callback<RemontageResponse>) {
        val call = service.getRemontage(token, ficheId)
        var fiche: Remontage? = null
        call.enqueue(callback)
    }

    fun getRemontageMotoreducteur(
        token: String,
        ficheId: String,
        callback: Callback<RemontageMotoreducteurResponse>
    ) {
        val call = service.getRemontageMotoreducteur(token, ficheId)
        call.enqueue(callback)
    }

    fun getRemontageMotopompe(
        token: String,
        ficheId: String,
        callback: Callback<RemontageMotopompeResponse>
    ) {
        val call = service.getRemontageMotopompe(token, ficheId)
        call.enqueue(callback)
    }

    fun getRemontageReducteur(
        token: String,
        ficheId: String,
        callback: Callback<RemontageReducteurResponse>
    ) {
        val call = service.getRemontageReducteur(token, ficheId)
        call.enqueue(callback)
    }

    fun getRemontageCC(token: String, ficheId: String, callback: Callback<RemontageCCResponse>) {
        var call = service.getRemontageCC(token, ficheId)
        var fiche: RemontageCourantC? = null
        call.enqueue(callback)
    }


    fun getRemontageTriphase(
        token: String,
        ficheId: String,
        callback: Callback<RemontageTriphaseResponse>
    ) {
        var call = service.getRemontageTriphase(token, ficheId)
        var fiche: RemontageTriphase? = null
        call.enqueue(callback)
    }

    fun patchRemontageTriphase(
        token: String,
        ficheId: String,
        fiche: RemontageTriphase,
        callback: Callback<RemontageTriphaseResponse>
    ) {
        var body = BodyRemontageTriphase(
            fiche.status!!.toInt(),
            fiche.dureeTotale,
            fiche.observations,
            fiche.remontageRoulement,
            fiche.collageRoulementPorteeArbre,
            fiche.collageRoulementFlasque,
            fiche.isolementPhaseMasse,
            fiche.isolementPhase,
            fiche.resistanceStatorU,
            fiche.resistanceStatorV,
            fiche.resistanceStatorW,
            fiche.isolementPhasePhaseU,
            fiche.isolementPhasePhaseV,
            fiche.isolementPhasePhaseW,
            fiche.vitesseU,
            fiche.puissanceU,
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
            fiche.tension,
            fiche.intensiteU,
            fiche.intensiteV,
            fiche.intensiteW,
            fiche.dureeEssai,
            fiche.photos
        )
        var call = service.patchRemontageTriphase(token, ficheId, body)
        var fiche: RemontageTriphase? = null
        call.enqueue(callback)
    }

    fun patchRemontage(
        token: String,
        ficheId: String,
        fiche: FicheRemontage,
        callback: Callback<RemontageResponse>
    ) {
        var body = BodyRemontage(
            fiche.status!!.toInt(),
            fiche.dureeTotale,
            fiche.observations,
            fiche.photos,
            fiche.subtype,
            fiche.remontageRoulement,
            fiche.collageRoulementPorteeArbre,
            fiche.collageRoulementFlasque,
            fiche.dureeEssai,
            fiche.isolementPhaseMasseU,
            fiche.isolementPhaseMasseV,
            fiche.isolementPhaseMasseW,
            fiche.isolementPhasePhaseU,
            fiche.isolementPhasePhaseV,
            fiche.isolementPhasePhaseW,
            fiche.resistanceStatorU,
            fiche.resistanceStatorV,
            fiche.resistanceStatorW,
            fiche.tension,
            fiche.intensiteU,
            fiche.intensiteV,
            fiche.intensiteW,
            fiche.isolementPhase,
            fiche.resistanceTravail,
            fiche.resistanceDemarrage,
            fiche.isolementMasseStatorPrincipalU,
            fiche.isolementMasseStatorPrincipalV,
            fiche.isolementMasseStatorPrincipalW,
            fiche.isolementMasseRotorPrincipal,
            fiche.isolementMasseStatorExcitation,
            fiche.isolementMasseRotorExcitation,
            fiche.resistanceRotorPrincipal,
            fiche.resistanceStatorExcitation,
            fiche.resistanceRotorExcitation,
            fiche.verificationFixationCouronne,
            fiche.isolementPorteBalais,
            fiche.isolementPhaseMasseRotorU,
            fiche.isolementPhaseMasseRotorV,
            fiche.isolementPhaseMasseRotorW,
            fiche.isolementPhaseRotorUV,
            fiche.isolementPhaseRotorVW,
            fiche.isolementPhaseRotorUW,
            fiche.isolementInduit,
            fiche.isolementPolePrincipal,
            fiche.isolementPoleAuxilliaire,
            fiche.isolementPoleCompensatoire,
            fiche.resistanceInduit,
            fiche.resistancePolePrincipal,
            fiche.resistancePoleAuxilliaire,
            fiche.resistancePoleCompensatoire,
            fiche.tensionInducteursU,
            fiche.tensionInducteursV,
            fiche.tensionInducteursW,
            fiche.intensitéInducteursU,
            fiche.intensitéInducteursV,
            fiche.intensitéInducteursW,
            fiche.tensionInduitU,
            fiche.tensionInduitV,
            fiche.tensionInduitW,
            fiche.intensiteInduit,
            fiche.tensionRotorU,
            fiche.tensionRotorV,
            fiche.tensionRotorW,
            fiche.tensionExcitation,
            fiche.intensiteExcitation,
            fiche.typeMotopompe,
            fiche.typeMotoreducteur
        )
        var call = service.patchRemontage(token, ficheId, body)
        var fiche: Remontage? = null
        call.enqueue(callback)
    }

    fun patchRemontageMotopompe(
        token: String,
        ficheId: String,
        fiche: RemontageMotopompe,
        callback: Callback<RemontageMotopompeResponse>
    ) {
        var body = BodyRemontageMotopompe(
            fiche.status!!.toInt(),
            fiche.observations,
            fiche.dureeTotale,
            fiche.photos,
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
//essais vibratoires
            fiche.vitesse1V,  // vitesse 1v
            fiche.acceleration1V,  //acceleration 1v
            fiche.vitesse2V,  // vitesse 2v
            fiche.acceleration2V,  //acceleration 2v
            fiche.vitesse1H,  // vitesse 1H
            fiche.acceleration1H,  //acceleration 1H
            fiche.vitesse2H,  // vitesse 2H
            fiche.acceleration2H,  //acceleration 2H
            fiche.vitesse2A,  // vitesse 2acceleration
            fiche.acceleration2A,
            fiche.typeMotopompe,
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
        var call = service.patchRemontageMotopompe(token, ficheId, body)
        call.enqueue(callback)
    }

    fun patchRemontageMotoreducteur(
        token: String,
        ficheId: String,
        fiche: RemontageMotoreducteur,
        callback: Callback<RemontageMotoreducteurResponse>
    ) {
        var body = BodyRemontageMotoreducteur(
            fiche.status!!.toInt(),
            fiche.observations,
            fiche.dureeTotale,
            fiche.photos,
            fiche.remontageRoulement,
            fiche.collageRoulementPorteeArbre,
            fiche.collageRoulementFlasque,
            fiche.typeMotoreducteur,
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
        var call = service.patchRemontageMotoreducteur(token, ficheId, body)
        call.enqueue(callback)
    }

    //dao remontage triphase
    suspend fun insertRemoTriLocalDatabase(remo: RemontageTriphase) {
        remontageTriphaseDao!!.insertAll(remo.toEntity())
    }

    suspend fun getAllRemontageTriLocalDatabase(): List<RemontageTriphaseEntity> {
        return remontageTriphaseDao!!.getAll()
    }

    suspend fun getByIdRemoTriLocalDatabse(id: String): RemontageTriphase? {
        try {
            if (remontageTriphaseDao!!.getById(id) !== null) {
                return remontageTriphaseDao!!.getById(id).toRTriphase()
            } else return null
        } catch (e: Error) {
            Log.i("e", e.message!!)
            return null
        }
    }

    suspend fun updateRemoTriLocalDatabse(remo: RemontageTriphaseEntity) {
        remontageTriphaseDao!!.update(remo)
    }

    suspend fun deleteRemontageTriphaseLocalDatabse(remo: RemontageTriphaseEntity) {
        remontageTriphaseDao!!.delete(remo)
    }

    //dao remontage
    suspend fun insertRemoLocalDatabase(remo: FicheRemontage) {
        remontageDao!!.insertAll(remo.toEntity())
    }

    suspend fun getAllRemontageLocalDatabase(): List<RemontageEntity> {
        return remontageDao!!.getAll()
    }

    suspend fun getByIdRemoLocalDatabse(id: String): FicheRemontage? {
        try {
            if (remontageDao!!.getById(id) !== null) {
                return remontageDao!!.getById(id).toFicheRemo()
            } else return null
        } catch (e: Error) {
            Log.i("e", e.message!!)
            return null
        }
    }

    suspend fun updateRemoLocalDatabse(remo: RemontageEntity) {
        remontageDao!!.update(remo)
    }

    suspend fun deleteRemontageLocalDatabse(remo: RemontageEntity) {
        remontageDao!!.delete(remo)
    }

    // dao remo courant continu
    suspend fun insertRemoCCLocalDatabase(remo: RemontageCourantC) {
        remontageCourantCDao!!.insertAll(remo.toEntity())
    }

    suspend fun getAllRemontageCCLocalDatabase(): List<RemontageCCEntity> {
        return remontageCourantCDao!!.getAll()
    }

    suspend fun getByIdRemoCCLocalDatabse(id: String): RemontageCourantC? {
        try {
            if (remontageCourantCDao!!.getById(id) !== null) {
                return remontageCourantCDao!!.getById(id).toRCourantC()
            } else return null
        } catch (e: Error) {
            Log.i("e", e.message!!)
            return null
        }
    }

    suspend fun updateRemoCCLocalDatabse(remo: RemontageCCEntity) {
        remontageCourantCDao!!.update(remo)
    }

    suspend fun deleteRemontageCCLocalDatabse(remo: RemontageCCEntity) {
        remontageCourantCDao!!.delete(remo)
    }

    suspend fun insertRemoMotopompeDatabase(demo: RemontageMotopompe) {
        remontageMotopompeDao!!.insertAll(demo.toEntity())
    }

    suspend fun getAllRemontageMotopompeLocalDatabase(): List<RemontageMotopompeEntity> {
        return remontageMotopompeDao!!.getAll()
    }

    suspend fun getByIdRemoMotopompeLocalDatabase(id: String): RemontageMotopompe? {
        try {
            if (remontageMotopompeDao!!.getById(id) !== null) {
                return remontageMotopompeDao!!.getById(id).toRemontageMotopompe()
            } else return null
        } catch (e: Error) {
            Log.i("e", e.message!!)
            return null
        }
    }

    suspend fun updateRemoMotoPompeLocalDatabase(demo: RemontageMotopompeEntity) {
        remontageMotopompeDao!!.update(demo)
    }

    suspend fun deleteRemontageMotoPompeLocalDatabse(demo: RemontageMotopompeEntity) {
        remontageMotopompeDao!!.delete(demo)
    }

    suspend fun insertRemoMotoreducteurDatabase(demo: RemontageMotoreducteur) {
        remontageMotoreducteurDao!!.insertAll(demo.toEntity())
    }

    suspend fun getAllRemontageMotoreducteurLocalDatabase(): List<RemontageMotoreducteurEntity> {
        return remontageMotoreducteurDao!!.getAll()
    }

    suspend fun getByIdRemoMotoreducteurLocalDatabase(id: String): RemontageMotoreducteur? {
        try {
            if (remontageMotoreducteurDao!!.getById(id) !== null) {
                return remontageMotoreducteurDao!!.getById(id).toRemontageMotoreducteur()
            } else return null
        } catch (e: Error) {
            Log.i("e", e.message!!)
            return null
        }
    }

    suspend fun updateRemoMotoreducteurLocalDatabase(demo: RemontageMotoreducteurEntity) {
        remontageMotoreducteurDao!!.update(demo)
    }

    suspend fun deleteRemontageMotoreducteurLocalDatabse(demo: RemontageMotoreducteurEntity) {
        remontageMotoreducteurDao!!.delete(demo)
    }

}