package com.example.applicationstb.repository

import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.applicationstb.localdatabase.*
import com.example.applicationstb.model.*
import retrofit2.Callback

class BodyFicheDemontage(
                          var status: Int?,
                          var subtype: Int,
                          var observations: String?,
                          var photos: Array<String>?,
                          var marque: String?,
                          var typeMoteur: String?,
                          var numSerie: String?,
                          var arbreSortantEntrant: Boolean?,
                          var accouplement: Boolean?,
                          var coteAccouplement: String?,
                          var clavette: Boolean?,
                          var aspect: Int?,
                          var aspectInterieur: Int?,
                          var flasqueAvant:Int?,
                          var flasqueArriere: Int?,
                          var porteeRAvant: Int?,
                          var porteeRArriere: Int?,
                          var boutArbre: Boolean?,
                          var refRoulementAvant: Array<String>?,
                          var refRoulementArriere: Array<String>?,
                          var typeRoulementAvant: Array<String>?,
                          var typeRoulementArriere: Array<String>?,
                          var refJointAvant: String?,
                          var refJointArriere: String?,
                          var typeJointAvant: Boolean?,
                          var typeJointArriere: Boolean?,
    // fiches 2,3,4,5,6,7
                          var puissance: String?,
                          var bride: String?,
                          var vitesse: String?,
                          var couplage: String?,
                          var rondelleElastique: Boolean?,
                          var ventilateur: Int?,
                          var capotV: Int?,
                          var socleBoiteABorne: Int?,
                          var capotBoiteABorne: Int?,
                          var plaqueABorne: Int?,
                          var presenceSondes: Boolean?,
                          var typeSondes: String?,
                          var equilibrage: Boolean?,
                          var peinture: String?,
                          var tensionU: String?,
                          var tensionV: String?,
                          var tensionW: String?,
    // fiches 3,4,5,6,7
                          var isolementPhase: String?,
    //fiches 1
                          var fluide: String?,
                          var sensRotation: Boolean?,
                          var typeRessort: Int?,
                          var typeJoint: String?,
                          var matiere: Int?,
                          var diametreArbre: String?,
                          var diametreExtPR: String?,
                          var diametreExtPF: String?,
                          var epaisseurPF: String?,
                          var longueurRotativeNonComprimee: String?,
                          var longueurRotativeComprimee: String?,
                          var longueurRotativeTravail: String?,
                          var typeJoint2: String?,
                          var matiere2: Int?,
                          var diametreArbre2: String?,
                          var diametreExtPR2: String?,
                          var diametreExtPF2: String?,
                          var epaisseurPF2: String?,
                          var longueurRotativeNonComprimee2: String?,
                          var longueurRotativeComprimee2: String?,
                          var longueurRotativeTravail2: String?,
    // fiches 2
                          var resistanceTravail: String?,
                          var resistanceDemarrage: String?,
                          var valeurCondensateur: String?,
    // fiches 3
                          var isolementMasseStatorPrincipalU: String?,
                          var isolementMasseStatorPrincipalV: String?,
                          var isolementMasseStatorPrincipalW: String?,
                          var isolementMasseRotorPrincipal: String?,
                          var isolementMasseStatorExcitation: String?,
                          var isolementMasseRotorExcitation: String?,
                          var resistanceRotorPrincipal: String?,
                          var resistanceStatorExcitation: String?,
                          var resistanceRotorExcitation: String?,
                          var testDiode: Boolean?,
    // fiche 5
                          var isolementMasseInduit: String?,
                          var isolementMassePolesPrincipaux: String?,
                          var isolementMassePolesAuxilliaires: String?,
                          var isolementMassePolesCompensatoires: String?,
                          var isolementMassePorteBalais: String?,
                          var resistanceInduit: String?,
                          var resistancePP: String?,
                          var resistancePA: String?,
                          var resistancePC: String?,
    // fiche 6
                          var isolementPhaseMasseStatorUM: String?,
                          var isolementPhaseMasseStatorVM: String?,
                          var isolementPhaseMasseStatorWM: String?,
                          var isolementPhasePhaseStatorUV: String?,
                          var isolementPhasePhaseStatorVW: String?,
                          var isolementPhasePhaseStatorUW: String?,
                          var resistanceStatorU: String?,
                          var resistanceStatorV: String?,
                          var resistanceStatorW: String?,
    //fiche 7
                          var typeMotopompe: Int?,
    // fiche 8
                          var trMinute: String?,
                          var modele: String?,
                          var indiceReduction: String?,
                          var typeHuile: String?,
                          var quantiteHuile: String?,
                          var roulements: MutableList<Roulement>?,
                          var joints: MutableList<Joint>?,
                          var typeMotoreducteur: Int?) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString(),
        arrayOf<String>().apply {
            parcel.readArray(String::class.java.classLoader)
        },
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
        parcel.readString(),
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
        parcel.createStringArray(),
        parcel.createStringArray(),
        parcel.createStringArray(),
        parcel.createStringArray(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
        parcel.readString(),
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
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
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
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
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        mutableListOf<Roulement>().apply {
            parcel.readList(this, Roulement::class.java.classLoader)
        },
        mutableListOf<Joint>().apply {
            parcel.readList(this, com.example.applicationstb.model.Joint::class.java.classLoader)
        },
        parcel.readValue(Int::class.java.classLoader) as? Int
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(status!!)
        parcel.writeInt(subtype)
        parcel.writeString(observations)
        arrayOf<String>().apply {
            parcel.readArray(String::class.java.classLoader)
        }
        parcel.writeString(marque)
        parcel.writeString(typeMoteur)
        parcel.writeString(numSerie)
        parcel.writeValue(arbreSortantEntrant)
        parcel.writeValue(accouplement)
        parcel.writeString(coteAccouplement)
        parcel.writeValue(clavette)
        parcel.writeValue(aspect)
        parcel.writeValue(aspectInterieur)
        parcel.writeValue(flasqueAvant)
        parcel.writeValue(flasqueArriere)
        parcel.writeValue(porteeRAvant)
        parcel.writeValue(porteeRArriere)
        parcel.writeValue(boutArbre)
        parcel.writeStringArray(refRoulementAvant)
        parcel.writeStringArray(refRoulementArriere)
        parcel.writeStringArray(typeRoulementAvant)
        parcel.writeStringArray(typeRoulementArriere)
        parcel.writeString(refJointAvant)
        parcel.writeString(refJointArriere)
        parcel.writeValue(typeJointAvant)
        parcel.writeValue(typeJointArriere)
        parcel.writeString(puissance)
        parcel.writeString(bride)
        parcel.writeString(vitesse)
        parcel.writeString(couplage)
        parcel.writeValue(rondelleElastique)
        parcel.writeValue(ventilateur)
        parcel.writeValue(capotV)
        parcel.writeValue(socleBoiteABorne)
        parcel.writeValue(capotBoiteABorne)
        parcel.writeValue(plaqueABorne)
        parcel.writeValue(presenceSondes)
        parcel.writeString(typeSondes)
        parcel.writeValue(equilibrage)
        parcel.writeString(peinture)
        parcel.writeString(tensionU)
        parcel.writeString(tensionV)
        parcel.writeString(tensionW)
        parcel.writeString(isolementPhase)
        parcel.writeString(fluide)
        parcel.writeValue(sensRotation)
        parcel.writeValue(typeRessort)
        parcel.writeString(typeJoint)
        parcel.writeValue(matiere)
        parcel.writeString(diametreArbre)
        parcel.writeString(diametreExtPR)
        parcel.writeString(diametreExtPF)
        parcel.writeString(epaisseurPF)
        parcel.writeString(longueurRotativeNonComprimee)
        parcel.writeString(longueurRotativeComprimee)
        parcel.writeString(longueurRotativeTravail)
        parcel.writeString(typeJoint2)
        parcel.writeValue(matiere2)
        parcel.writeString(diametreArbre2)
        parcel.writeString(diametreExtPR2)
        parcel.writeString(diametreExtPF2)
        parcel.writeString(epaisseurPF2)
        parcel.writeString(longueurRotativeNonComprimee2)
        parcel.writeString(longueurRotativeComprimee2)
        parcel.writeString(longueurRotativeTravail2)
        parcel.writeString(resistanceTravail)
        parcel.writeString(resistanceDemarrage)
        parcel.writeString(valeurCondensateur)
        parcel.writeString(isolementMasseStatorPrincipalU)
        parcel.writeString(isolementMasseStatorPrincipalV)
        parcel.writeString(isolementMasseStatorPrincipalW)
        parcel.writeString(isolementMasseRotorPrincipal)
        parcel.writeString(isolementMasseStatorExcitation)
        parcel.writeString(isolementMasseRotorExcitation)
        parcel.writeString(resistanceRotorPrincipal)
        parcel.writeString(resistanceStatorExcitation)
        parcel.writeString(resistanceRotorExcitation)
        parcel.writeValue(testDiode)
        parcel.writeString(isolementMasseInduit)
        parcel.writeString(isolementMassePolesPrincipaux)
        parcel.writeString(isolementMassePolesAuxilliaires)
        parcel.writeString(isolementMassePolesCompensatoires)
        parcel.writeString(isolementMassePorteBalais)
        parcel.writeString(resistanceInduit)
        parcel.writeString(resistancePP)
        parcel.writeString(resistancePA)
        parcel.writeString(resistancePC)
        parcel.writeString(isolementPhaseMasseStatorUM)
        parcel.writeString(isolementPhaseMasseStatorVM)
        parcel.writeString(isolementPhaseMasseStatorWM)
        parcel.writeString(isolementPhasePhaseStatorUV)
        parcel.writeString(isolementPhasePhaseStatorVW)
        parcel.writeString(isolementPhasePhaseStatorUW)
        parcel.writeString(resistanceStatorU)
        parcel.writeString(resistanceStatorV)
        parcel.writeString(resistanceStatorW)
        parcel.writeValue(typeMotopompe)
        parcel.writeString(trMinute)
        parcel.writeString(modele)
        parcel.writeString(indiceReduction)
        parcel.writeString(typeHuile)
        parcel.writeString(quantiteHuile)
        mutableListOf<Roulement>().apply {
            parcel.writeList(this)
        }
        mutableListOf<Joint>().apply {
            parcel.writeList(this)
        }
        parcel.writeValue(typeMotoreducteur)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BodyFicheDemontage> {
        override fun createFromParcel(parcel: Parcel): BodyFicheDemontage {
            return BodyFicheDemontage(parcel)
        }

        override fun newArray(size: Int): Array<BodyFicheDemontage?> {
            return arrayOfNulls(size)
        }
    }

}

class FicheDemontageResponse(
    var data: FicheDemontage?
)

class BodyDemontageTriphase(
    var status: Long?,
    var typeMoteur: String?,
    var marque: String?,
    var numSerie: String?,
    var puissance: Float?,
    var bride: Float?,
    var vitesse: Float?,
    var arbreSortantEntrant: Boolean?, //arbre sortant ou rentrant
    var accouplement: Boolean?,
    var coteAccouplement: String?,
    var clavette: Boolean?,
    var aspect: Int?,
    var aspectInterieur: Int?,
    var couplage: String?,
    var flasqueAvant: Int?,
    var flasqueArriere: Int?,
    var porteeRAvant: Int?,
    var porteeRArriere: Int?,
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
    var socleBoiteABorne: Int?,
    var capotBoiteABorne: Int?,
    var plaqueABorne: Int?,
    var presenceSondes: Boolean?,
    var typeSondes: String?,
    var equilibrage: Boolean?,
    var peinture: String?,
    var isolementPhaseMasseStatorUM: Float?,
    var isolementPhaseMasseStatorVM: Float?,
    var isolementPhaseMasseStatorWM: Float?,
    var isolementPhasePhaseStatorUV: Float?,
    var isolementPhasePhaseStatorVW: Float?,
    var isolementPhasePhaseStatorUW: Float?,
    var resistanceStatorU: Float?,
    var resistanceStatorV: Float?,
    var resistanceStatorW: Float?,
    var tensionU: Float?,
    var tensionV: Float?,
    var tensionW: Float?,
    var intensiteU: Float?,
    var intensiteV: Float?,
    var intensiteW: Float?,
    var dureeEssai: Float?,
    var dureeTotale: Long?,
    var observations: String?,
    var photos: Array<String>?
) : Parcelable {
    @RequiresApi(Build.VERSION_CODES.Q)
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
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
        parcel.readLong(),
        parcel.readString(),
        arrayOf<String>().apply {
            parcel.readArray(String::class.java.classLoader)
        }
    ) {
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(status!!)
        parcel.writeString(typeMoteur!!)
        parcel.writeString(marque!!)
        parcel.writeString(numSerie!!)
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
        parcel.writeFloat(isolementPhasePhaseStatorUV!!)
        parcel.writeFloat(isolementPhasePhaseStatorVW!!)
        parcel.writeFloat(isolementPhasePhaseStatorUW!!)
        parcel.writeFloat(resistanceStatorU!!)
        parcel.writeFloat(resistanceStatorV!!)
        parcel.writeFloat(resistanceStatorW!!)
        parcel.writeFloat(tensionU!!)
        parcel.writeFloat(tensionV!!)
        parcel.writeFloat(tensionW!!)
        parcel.writeFloat(intensiteU!!)
        parcel.writeFloat(intensiteV!!)
        parcel.writeFloat(intensiteW!!)
        parcel.writeFloat(dureeEssai!!)
        parcel.writeLong(dureeTotale!!)
        parcel.writeString(observations!!)
        arrayOf<String>().apply {
            parcel.writeArray(photos)
        }
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

class BodyDemontageCC(
    var status: Long?,
    var typeMoteur: String?,
    var marque: String?,
    var numSerie: String?,
    var puissance: Float?,
    var bride: Float?,
    var vitesse: Float?,
    var arbreSortantEntrant: Boolean?, //arbre sortant ou rentrant
    var accouplement: Boolean?,
    var coteAccouplement: String?,
    var clavette: Boolean?,
    var aspect: Int?,
    var aspectInterieur: Int?,
    var couplage: String?,
    var flasqueAvant: Int?,
    var flasqueArriere: Int?,
    var porteeRAvant: Int?,
    var porteeRArriere: Int?,
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
    var socleBoiteABorne: Int?,
    var capotBoiteABorne: Int?,
    var plaqueABorne: Int?,
    var presenceSondes: Boolean?,
    var typeSondes: String?,
    var equilibrage: Boolean?,
    var peinture: String?,
    var isolementMasseInduit: Float?,
    var isolementMassePolesPrincipaux: Float?,
    var isolementMassePolesAuxilliaires: Float?,
    var isolementMassePolesCompensatoires: Float?,
    var isolementMassePorteBalais: Float?,
    var resistanceInduit: Float?,
    var resistancePP: Float?,
    var resistancePA: Float?,
    var resistancePC: Float?,
    var tensionInduit: Float?,
    var intensiteInduit: Float?,
    var tensionExcitation: Float?,
    var intensiteExcitation: Float?,
    var dureeTotale: Long?,
    var observations: String?,
    var photos: Array<String>?
) : Parcelable {
    @RequiresApi(Build.VERSION_CODES.Q)
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
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
        parcel.readLong(),
        parcel.readString(),
        arrayOf<String>().apply {
            parcel.readArray(String::class.java.classLoader)
        }
    ) {
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(status!!)
        parcel.writeString(typeMoteur!!)
        parcel.writeString(marque!!)
        parcel.writeString(numSerie!!)
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
        parcel.writeFloat(isolementMasseInduit!!)
        parcel.writeFloat(isolementMassePolesPrincipaux!!)
        parcel.writeFloat(isolementMassePolesAuxilliaires!!)
        parcel.writeFloat(isolementMassePolesCompensatoires!!)
        parcel.writeFloat(isolementMassePorteBalais!!)
        parcel.writeFloat(resistanceInduit!!)
        parcel.writeFloat(resistancePP!!)
        parcel.writeFloat(resistancePA!!)
        parcel.writeFloat(resistancePC!!)
        parcel.writeFloat(tensionInduit!!)
        parcel.writeFloat(intensiteInduit!!)
        parcel.writeFloat(tensionExcitation!!)
        parcel.writeFloat(intensiteExcitation!!)
        parcel.writeLong(dureeTotale!!)
        parcel.writeString(observations!!)
        arrayOf<String>().apply {
            parcel.writeArray(photos)
        }
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

class BodyDemontageAlternateur(
    var observations: String?,
    var typeMoteur: String?,
    var status: Long?,
    var marque: String?,
    var numSerie: String?,
    var puissance: Float?,
    var bride: Float?,
    var vitesse: Float?,
    var arbreSortantEntrant: Boolean?, //arbre sortant ou rentrant
    var accouplement: Boolean?,
    var coteAccouplement: String?,
    var clavette: Boolean?,
    var aspect: Int?,
    var aspectInterieur: Int?,
    var couplage: String?,
    var flasqueAvant: Int?,
    var flasqueArriere: Int?,
    var porteeRAvant: Int?,
    var porteeRArriere: Int?,
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
    var socleBoiteABorne: Int?,
    var capotBoiteABorne: Int?,
    var plaqueABorne: Int?,
    var presenceSondes: Boolean?,
    var typeSondes: String?,
    var equilibrage: Boolean?,
    var peinture: String?,
    var isolementMasseStatorPrincipalU: Float?,
    var isolementMasseStatorPrincipalV: Float?,
    var isolementMasseStatorPrincipalW: Float?,
    var isolementMasseRotorPrincipal: Float?,
    var isolementMasseStatorExcitation: Float?,
    var isolementMasseRotorExcitation: Float?,
    var resistanceStatorPrincipalU: Float?,
    var resistanceStatorPrincipalV: Float?,
    var resistanceStatorPrincipalW: Float?,
    var resistanceRotorPrincipal: Float?,
    var resistanceStatorExcitation: Float?,
    var resistanceRotorExcitation: Float?,
    var isolementPhasePhaseStatorPrincipalUV: Float?,
    var isolementPhasePhaseStatorPrincipalVW: Float?,
    var isolementPhasePhaseStatorPrincipalUW: Float?,
    var testDiode: Boolean?,
    var tensionU: Float?,
    var tensionV: Float?,
    var tensionW: Float?,
    var intensiteU: Float?,
    var intensiteV: Float?,
    var intensiteW: Float?,
    var tensionExcitation: Float?,
    var intensiteExcitation: Float?,
    var dureeTotale: Int?,
    var photos: Array<String>?
) : Parcelable {
    @RequiresApi(Build.VERSION_CODES.Q)
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readLong(),
        parcel.readString(),
        parcel.readString(),
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
        arrayOf<String>().apply {
            parcel.readArray(String::class.java.classLoader)
        },
    ) {
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(observations!!)
        parcel.writeString(typeMoteur!!)
        parcel.writeLong(status!!)
        parcel.writeString(marque!!)
        parcel.writeString(numSerie!!)
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
        arrayOf<String>().apply {
            parcel.writeArray(photos)
        }
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

class BodyDemontageRotorBobine(
    var observations: String?,
    var typeMoteur: String?,
    var status: Long?,
    var marque: String?,
    var numSerie: String?,
    var puissance: Float?,
    var bride: Float?,
    var vitesse: Float?,
    var arbreSortantEntrant: Boolean?, //arbre sortant ou rentrant
    var accouplement: Boolean?,
    var coteAccouplement: String?,
    var clavette: Boolean?,
    var aspect: Int?,
    var aspectInterieur: Int?,
    var couplage: String?,
    var flasqueAvant: Int?,
    var flasqueArriere: Int?,
    var porteeRAvant: Int?,
    var porteeRArriere: Int?,
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
    var socleBoiteABorne: Int?,
    var capotBoiteABorne: Int?,
    var plaqueABorne: Int?,
    var presenceSondes: Boolean?,
    var typeSondes: String?,
    var equilibrage: Boolean?,
    var peinture: String?,
    var isolementPhaseMasseStatorUM: Float?,
    var isolementPhaseMasseStatorVM: Float?,
    var isolementPhaseMasseStatorWM: Float?,
    var isolementPhaseMasseRotorB1M: Float?,
    var isolementPhaseMasseRotorB2M: Float?,
    var isolementPhaseMasseRotorB3M: Float?,
    var isolementPhaseMassePorteBalaisM: Float?,
    var isolementPhasePhaseStatorUV: Float?,
    var isolementPhasePhaseStatorVW: Float?,
    var isolementPhasePhaseStatorUW: Float?,
    var resistanceStatorU: Float?,
    var resistanceStatorV: Float?,
    var resistanceStatorW: Float?,
    var resistanceRotorB1B2: Float?,
    var resistanceRotorB2B2: Float?,
    var resistanceRotorB1B3: Float?,
    var tensionU: Float?,
    var tensionV: Float?,
    var tensionW: Float?,
    var tensionRotor: Float?,
    var intensiteU: Float?,
    var intensiteV: Float?,
    var intensiteW: Float?,
    var intensiteRotor: Float?,
    var dureeEssai: Int?,
    var dureeTotale: Int?,
    var photos: Array<String>?
) : Parcelable {
    @RequiresApi(Build.VERSION_CODES.Q)
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readLong(),
        parcel.readString(),
        parcel.readString(),
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
        arrayOf<String>().apply {
            parcel.readArray(String::class.java.classLoader)
        }
    ) {
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(observations!!)
        parcel.writeString(typeMoteur!!)
        parcel.writeLong(status!!)
        parcel.writeString(marque!!)
        parcel.writeString(numSerie!!)
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
        arrayOf<String>().apply {
            parcel.writeArray(refRoulementAvant)
        }
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

class BodyDemontageMonophase(
    var observations: String?,
    var typeMoteur: String?,
    var status: Long?,
    var marque: String?,
    var numSerie: String?,
    var puissance: Float?,
    var bride: Float?,
    var vitesse: Float?,
    var arbreSortantEntrant: Boolean?, //arbre sortant ou rentrant
    var accouplement: Boolean?,
    var coteAccouplement: String?,
    var clavette: Boolean?,
    var aspect: Int?,
    var aspectInterieur: Int?,
    var couplage: String?,
    var flasqueAvant: Int?,
    var flasqueArriere: Int?,
    var porteeRAvant: Int?,
    var porteeRArriere: Int?,
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
    var socleBoiteABorne: Int?,
    var capotBoiteABorne: Int?,
    var plaqueABorne: Int?,
    var presenceSondes: Boolean?,
    var typeSondes: String?,
    var equilibrage: Boolean?,
    var peinture: String?,
    var isolementPhaseMasse: Float?,
    var resistanceTravail: Float?,
    var resistanceDemarrage: Float?,
    var valeurCondensateur: Float?,
    var tension: Float?,
    var intensite: Float?,
    var dureeTotale: Int?,
    var photos: Array<String>?
) : Parcelable {
    @RequiresApi(Build.VERSION_CODES.Q)
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readLong(),
        parcel.readString(),
        parcel.readString(),
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
        arrayOf<String>().apply {
            parcel.readArray(String::class.java.classLoader)
        },
    ) {
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(typeMoteur!!)
        parcel.writeString(observations!!)
        parcel.writeLong(status!!)
        parcel.writeString(marque!!)
        parcel.writeString(numSerie!!)
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
        arrayOf<String>().apply {
            parcel.writeArray(photos)
        }
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
    var numSerie: String?,
    var fluide: String?,
    var sensRotation: Boolean?,
    var typeRessort: Int?,
    var typeJoint: String?,
    var matiere: Int?,
    var diametreArbre: String?,
    var diametreExtPR: String?,
    var diametreExtPF: String?,
    var epaisseurPF: String?,
    var longueurRotativeNonComprimee: String?,
    var longueurRotativeComprimee: String?,
    var longueurRotativeTravail: String?,
    var observations: String?,
    var dureeTotale: Long?,
    var photos: Array<String>?,
    var refRoulementAvant: Array<String>?,
    var refRoulementArriere: Array<String>?,
    var typeRoulementAvant: Array<String>?,
    var typeRoulementArriere: Array<String>?,
    var refJointAvant: String?,
    var refJointArriere: String?,
    var typeJointAvant: Boolean?,
    var typeJointArriere: Boolean?,
    var typeJoint2: String?,
    var matiere2: Int?,
    var diametreArbre2: String?,
    var diametreExtPR2: String?,
    var diametreExtPF2: String?,
    var epaisseurPF2: String?,
    var longueurRotativeNonComprimee2: String?,
    var longueurRotativeComprimee2: String?,
    var longueurRotativeTravail2: String?,
) : Parcelable {
    @RequiresApi(Build.VERSION_CODES.Q)
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readBoolean(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readLong(),
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
        arrayOf<String>().apply {
            parcel.readArray(String::class.java.classLoader)
        },
        parcel.readString(),
        parcel.readString(),
        parcel.readBoolean(),
        parcel.readBoolean(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
    ) {
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(status!!)
        parcel.writeString(typeMoteur!!)
        parcel.writeString(marque!!)
        parcel.writeString(numSerie!!)
        parcel.writeString(fluide!!)
        parcel.writeBoolean(sensRotation!!)
        parcel.writeInt(typeRessort!!)
        parcel.writeString(typeJoint!!)
        parcel.writeInt(matiere!!)
        parcel.writeString(diametreArbre!!)
        parcel.writeString(diametreExtPR!!)
        parcel.writeString(diametreExtPF!!)
        parcel.writeString(epaisseurPF!!)
        parcel.writeString(longueurRotativeNonComprimee!!)
        parcel.writeString(longueurRotativeComprimee!!)
        parcel.writeString(longueurRotativeTravail!!)
        parcel.writeString(observations!!)
        parcel.writeLong(dureeTotale!!)
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
        parcel.writeString(typeJoint2!!)
        parcel.writeInt(matiere2!!)
        parcel.writeString(diametreArbre2!!)
        parcel.writeString(diametreExtPR2!!)
        parcel.writeString(diametreExtPF2!!)
        parcel.writeString(epaisseurPF2!!)
        parcel.writeString(longueurRotativeNonComprimee2!!)
        parcel.writeString(longueurRotativeComprimee2!!)
        parcel.writeString(longueurRotativeTravail2!!)
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

class BodyDemoMotoPompe(
    var status: Long?,
    var dureeTotale: Int?,
    var observations: String?,
    var photos: Array<String>?,
    var typeMoteur: String?,
    var marque: String?,
    var numSerie: String?,
    var puissance: Float?,
    var bride: Float?,
    var vitesse: Float?,
    var arbreSortantEntrant: Boolean?, //arbre sortant ou rentrant
    var accouplement: Boolean?,
    var coteAccouplement: String?,
    var clavette: Boolean?,
    var aspect: Int?,
    var aspectInterieur: Int?,
    var couplage: String?,
    var flasqueAvant: Int?,
    var flasqueArriere: Int?,
    var porteeRAvant: Int?,
    var porteeRArriere: Int?,
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
    var socleBoiteABorne: Int?,
    var capotBoiteABorne: Int?,
    var plaqueABorne: Int?,
    var presenceSondes: Boolean?,
    var typeSondes: String?,
    var equilibrage: Boolean?,
    var peinture: String?,
    var typeMotopompe: String?,
    var fluide: String?,
    var sensRotation: Boolean?,
    var typeRessort: Int?,
    var typeJoint: String?,
    var matiere: Int?,
    var diametreArbre: String?,
    var diametreExtPR: String?,
    var diametreExtPF: String?,
    var epaisseurPF: String?,
    var longueurRotativeNonComprimee: String?,
    var longueurRotativeComprimee: String?,
    var longueurRotativeTravail: String?,
    var isolementPhaseMasseStatorUM: Float?,
    var isolementPhaseMasseStatorVM: Float?,
    var isolementPhaseMasseStatorWM: Float?,
    var isolementPhasePhaseStatorUV: Float?,
    var isolementPhasePhaseStatorVW: Float?,
    var isolementPhasePhaseStatorUW: Float?,
    var resistanceStatorU: Float?,
    var resistanceStatorV: Float?,
    var resistanceStatorW: Float?,
    var tensionU: Float?,
    var tensionV: Float?,
    var tensionW: Float?,
    var intensiteU: Float?,
    var intensiteV: Float?,
    var intensiteW: Float?,
    var dureeEssai: Float?,
    var isolementPhaseMasse: Float?,
    var resistanceTravail: Float?,
    var resistanceDemarrage: Float?,
    var valeurCondensateur: Float?,
    var tension: Float?,
    var intensite: Float?,
    var typeJoint2: String?,
    var matiere2: Int?,
    var diametreArbre2: String?,
    var diametreExtPR2: String?,
    var diametreExtPF2: String?,
    var epaisseurPF2: String?,
    var longueurRotativeNonComprimee2: String?,
    var longueurRotativeComprimee2: String?,
    var longueurRotativeTravail2: String?
) : Parcelable {
    @RequiresApi(Build.VERSION_CODES.Q)
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readInt(),
        parcel.readString(),
        arrayOf<String>().apply {
            parcel.readArray(String::class.java.classLoader)
        },
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
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
        parcel.readString(),
        parcel.readString(),
        parcel.readBoolean(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readInt(),
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
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
    ) {
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(status!!)
        parcel.writeInt(dureeTotale!!)
        parcel.writeString(observations!!)
        arrayOf<String>().apply {
            parcel.writeArray(photos)
        }
        parcel.writeString(typeMoteur!!)
        parcel.writeString(marque!!)
        parcel.writeString(numSerie!!)
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
        parcel.writeString(typeMotopompe)
        parcel.writeString(fluide!!)
        parcel.writeBoolean(sensRotation!!)
        parcel.writeInt(typeRessort!!)
        parcel.writeString(typeJoint!!)
        parcel.writeInt(matiere!!)
        parcel.writeString(diametreArbre!!)
        parcel.writeString(diametreExtPR!!)
        parcel.writeString(diametreExtPF!!)
        parcel.writeString(epaisseurPF!!)
        parcel.writeString(longueurRotativeNonComprimee!!)
        parcel.writeString(longueurRotativeComprimee!!)
        parcel.writeString(longueurRotativeTravail!!)
        parcel.writeFloat(isolementPhaseMasseStatorUM!!)
        parcel.writeFloat(isolementPhaseMasseStatorVM!!)
        parcel.writeFloat(isolementPhaseMasseStatorWM!!)
        parcel.writeFloat(isolementPhasePhaseStatorUV!!)
        parcel.writeFloat(isolementPhasePhaseStatorVW!!)
        parcel.writeFloat(isolementPhasePhaseStatorUW!!)
        parcel.writeFloat(resistanceStatorU!!)
        parcel.writeFloat(resistanceStatorV!!)
        parcel.writeFloat(resistanceStatorW!!)
        parcel.writeFloat(tensionU!!)
        parcel.writeFloat(tensionV!!)
        parcel.writeFloat(tensionW!!)
        parcel.writeFloat(intensiteU!!)
        parcel.writeFloat(intensiteV!!)
        parcel.writeFloat(intensiteW!!)
        parcel.writeFloat(dureeEssai!!)
        parcel.writeFloat(isolementPhaseMasse!!)
        parcel.writeFloat(resistanceTravail!!)
        parcel.writeFloat(resistanceDemarrage!!)
        parcel.writeFloat(valeurCondensateur!!)
        parcel.writeFloat(tension!!)
        parcel.writeFloat(intensite!!)
        parcel.writeString(typeJoint2!!)
        parcel.writeInt(matiere2!!)
        parcel.writeString(diametreArbre2!!)
        parcel.writeString(diametreExtPR2!!)
        parcel.writeString(diametreExtPF2!!)
        parcel.writeString(epaisseurPF2!!)
        parcel.writeString(longueurRotativeNonComprimee2!!)
        parcel.writeString(longueurRotativeComprimee2!!)
        parcel.writeString(longueurRotativeTravail2!!)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BodyDemoMotoPompe> {
        @RequiresApi(Build.VERSION_CODES.Q)
        override fun createFromParcel(parcel: Parcel): BodyDemoMotoPompe {
            return BodyDemoMotoPompe(parcel)
        }

        override fun newArray(size: Int): Array<BodyDemoMotoPompe?> {
            return arrayOfNulls(size)
        }
    }
}

class BodyDemoMotoReducteur(
    var status: Long?,
    var dureeTotale: Int?,
    var observations: String?,
    var photos: Array<String>?,
    var typeMoteur: String?,
    var marque: String?,
    var numSerie: String?,
    var puissance: Float?,
    var bride: Float?,
    var vitesse: Float?,
    var arbreSortantEntrant: Boolean?, //arbre sortant ou rentrant
    var accouplement: Boolean?,
    var coteAccouplement: String?,
    var clavette: Boolean?,
    var aspect: Int?,
    var aspectInterieur: Int?,
    var couplage: String?,
    var flasqueAvant: Int?,
    var flasqueArriere: Int?,
    var porteeRAvant: Int?,
    var porteeRArriere: Int?,
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
    var socleBoiteABorne: Int?,
    var capotBoiteABorne: Int?,
    var plaqueABorne: Int?,
    var presenceSondes: Boolean?,
    var typeSondes: String?,
    var equilibrage: Boolean?,
    var peinture: String?,
    var typeMotoreducteur: String?,
    var trMinute: Float?,
    var modele: String?,
    var indiceReduction: String?,
    var typeHuile: String?,
    var quantiteHuile: Float?,
    var roulements: MutableList<Roulement>?,
    var joints: MutableList<Joint>?,
    var isolementPhaseMasseStatorUM: Float?,
    var isolementPhaseMasseStatorVM: Float?,
    var isolementPhaseMasseStatorWM: Float?,
    var isolementPhasePhaseStatorUV: Float?,
    var isolementPhasePhaseStatorVW: Float?,
    var isolementPhasePhaseStatorUW: Float?,
    var resistanceStatorU: Float?,
    var resistanceStatorV: Float?,
    var resistanceStatorW: Float?,
    var tensionU: Float?,
    var tensionV: Float?,
    var tensionW: Float?,
    var intensiteU: Float?,
    var intensiteV: Float?,
    var intensiteW: Float?,
    var dureeEssai: Float?,
    var isolementPhaseMasse: Float?,
    var resistanceTravail: Float?,
    var resistanceDemarrage: Float?,
    var valeurCondensateur: Float?,
    var tension: Float?,
    var intensite: Float?,
) : Parcelable {
    @RequiresApi(Build.VERSION_CODES.Q)
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readInt(),
        parcel.readString(),
        arrayOf<String>().apply {
            parcel.readArray(String::class.java.classLoader)
        },
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
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
        parcel.readString(),
        parcel.readFloat(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readFloat(),
        mutableListOf<Roulement>().apply {
            parcel.readList(this, Roulement::class.java.classLoader)
        },
        mutableListOf<Joint>().apply {
            parcel.readList(this, Joint::class.java.classLoader)
        },
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
        parcel.readFloat()
    ) {
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(status!!)
        parcel.writeInt(dureeTotale!!)
        parcel.writeString(observations!!)
        arrayOf<String>().apply {
            parcel.writeArray(photos)
        }
        parcel.writeString(typeMoteur!!)
        parcel.writeString(marque!!)
        parcel.writeString(numSerie!!)
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
        parcel.writeString(typeMotoreducteur)
        parcel.writeFloat(trMinute!!)
        parcel.writeString(modele!!)
        parcel.writeString(indiceReduction!!)
        parcel.writeString(typeHuile!!)
        parcel.writeFloat(quantiteHuile!!)
        mutableListOf<Roulement>().apply {
            parcel.writeList(this)
        }
        mutableListOf<Joint>().apply {
            parcel.writeList(this)
        }
        parcel.writeFloat(isolementPhaseMasseStatorUM!!)
        parcel.writeFloat(isolementPhaseMasseStatorVM!!)
        parcel.writeFloat(isolementPhaseMasseStatorWM!!)
        parcel.writeFloat(isolementPhasePhaseStatorUV!!)
        parcel.writeFloat(isolementPhasePhaseStatorVW!!)
        parcel.writeFloat(isolementPhasePhaseStatorUW!!)
        parcel.writeFloat(resistanceStatorU!!)
        parcel.writeFloat(resistanceStatorV!!)
        parcel.writeFloat(resistanceStatorW!!)
        parcel.writeFloat(tensionU!!)
        parcel.writeFloat(tensionV!!)
        parcel.writeFloat(tensionW!!)
        parcel.writeFloat(intensiteU!!)
        parcel.writeFloat(intensiteV!!)
        parcel.writeFloat(intensiteW!!)
        parcel.writeFloat(dureeEssai!!)
        parcel.writeFloat(isolementPhaseMasse!!)
        parcel.writeFloat(resistanceTravail!!)
        parcel.writeFloat(resistanceDemarrage!!)
        parcel.writeFloat(valeurCondensateur!!)
        parcel.writeFloat(tension!!)
        parcel.writeFloat(intensite!!)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BodyDemoMotoReducteur> {
        @RequiresApi(Build.VERSION_CODES.Q)
        override fun createFromParcel(parcel: Parcel): BodyDemoMotoReducteur {
            return BodyDemoMotoReducteur(parcel)
        }

        override fun newArray(size: Int): Array<BodyDemoMotoReducteur?> {
            return arrayOfNulls(size)
        }
    }
}

class BodyDemontageReducteur(
    var status: Long?,
    var dureeTotale: Int?,
    var observations: String?,
    var photos: Array<String>?,
    var marque: String?,
    var typeMoteur: String?,
    var numSerie: String?,
    var puissance: Float?,
    var bride: Float?,
    var vitesse: Float?,
    var arbreSortantEntrant: Boolean?, //arbre sortant ou rentrant
    var accouplement: Boolean?,
    var coteAccouplement: String?,
    var clavette: Boolean?,
    var peinture: String?,
    var trMinute: Float?,
    var modele: String?,
    var indiceReduction: String?,
    var typeHuile: String?,
    var quantiteHuile: Float?,
    var roulements: MutableList<Roulement>?,
    var joints: MutableList<Joint>?

) : Parcelable {
    @RequiresApi(Build.VERSION_CODES.Q)
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readInt(),
        parcel.readString(),
        arrayOf<String>().apply {
            parcel.readArray(String::class.java.classLoader)
        },
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readBoolean(),
        parcel.readBoolean(),
        parcel.readString(),
        parcel.readBoolean(),
        parcel.readString(),
        parcel.readFloat(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readFloat(),
        mutableListOf<Roulement>().apply {
            parcel.readList(this, Roulement::class.java.classLoader)
        },
        mutableListOf<Joint>().apply {
            parcel.readList(this, Joint::class.java.classLoader)
        }
    ) {
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(status!!)
        parcel.writeInt(dureeTotale!!)
        parcel.writeString(observations!!)
        arrayOf<String>().apply {
            parcel.writeArray(photos)
        }
        parcel.writeString(marque!!)
        parcel.writeString(typeMoteur!!)
        parcel.writeString(numSerie!!)
        parcel.writeFloat(puissance!!)
        parcel.writeFloat(bride!!)
        parcel.writeFloat(vitesse!!)
        parcel.writeBoolean(arbreSortantEntrant!!)
        parcel.writeBoolean(accouplement!!)
        parcel.writeString(coteAccouplement!!)
        parcel.writeBoolean(clavette!!)
        parcel.writeString(peinture!!)
        parcel.writeFloat(trMinute!!)
        parcel.writeString(modele!!)
        parcel.writeString(indiceReduction!!)
        parcel.writeString(typeHuile!!)
        parcel.writeFloat(quantiteHuile!!)
        mutableListOf<Roulement>().apply {
            parcel.writeList(this)
        }
        mutableListOf<Joint>().apply {
            parcel.writeList(this)
        }
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BodyDemontageReducteur> {
        @RequiresApi(Build.VERSION_CODES.Q)
        override fun createFromParcel(parcel: Parcel): BodyDemontageReducteur {
            return BodyDemontageReducteur(parcel)
        }

        override fun newArray(size: Int): Array<BodyDemontageReducteur?> {
            return arrayOfNulls(size)
        }
    }
}

class DemontageRepository(var service: APIstb, var db: LocalDatabase) {
    var demontageTriphaseDao = db!!.demontageTriphaseDao()
    var demontageCCDao = db!!.demontageCCDao()
    var demontagePDao = db!!.demontagePDao()
    var demontageMonoDao = db!!.demontageMonophaseDao()
    var demontageAlterDao = db!!.demontageAlternateurDao()
    var demontageRBDao = db!!.demontageRotorBobineDao()
    var demontageMotopompeDao = db!!.demontageMotopompeDao()
    var demontageMotoreducteurDao = db!!.demontageMotoreducteurDao()
    var demontageReducteurDao = db!!.demontageReducteurDao()
    var demontageDao = db!!.demontageDao()

    fun getFicheDemontage(token: String, ficheId: String, callback: Callback<FicheDemontageResponse>) {
        val call = service.getFicheDemontage(token, ficheId)
        call.enqueue(callback)
    }

    fun patchFicheDemontage(token: String,
                            ficheId: String,
                            demontage: FicheDemontage,
                            callback: Callback<FicheDemontageResponse>){
        var body = BodyFicheDemontage(
            demontage.status!!.toInt(),
            demontage.subtype,
            demontage.observations,
            demontage.photos,
            demontage.marque,
            demontage.typeMoteur,
            demontage.numSerie,
            demontage.arbreSortantEntrant,
            demontage.accouplement,
            demontage.coteAccouplement,
            demontage.clavette,
            demontage.aspect,
            demontage.aspectInterieur,
            demontage.flasqueAvant,
            demontage.flasqueArriere,
            demontage.porteeRAvant,
            demontage.porteeRArriere,
            demontage.boutArbre,
            demontage.refRoulementAvant,
            demontage.refRoulementArriere,
            demontage.typeRoulementAvant,
            demontage.typeRoulementArriere,
            demontage.refJointAvant,
            demontage.refJointArriere,
            demontage.typeJointAvant,
            demontage.typeJointArriere,
            demontage.puissance,
            demontage.bride,
            demontage.vitesse,
            demontage.couplage,
            demontage.rondelleElastique,
            demontage.ventilateur,
            demontage.capotV,
            demontage.socleBoiteABorne,
            demontage.capotBoiteABorne,
            demontage.plaqueABorne,
            demontage.presenceSondes,
            demontage.typeSondes,
            demontage.equilibrage,
            demontage.peinture,
            demontage.tensionU,
            demontage.tensionV,
            demontage.tensionW,
            demontage.isolementPhase,
            demontage.fluide,
            demontage.sensRotation,
            demontage.typeRessort,
            demontage.typeJoint,
            demontage.matiere,
            demontage.diametreArbre,
            demontage.diametreExtPR,
            demontage.diametreExtPF,
            demontage.epaisseurPF,
            demontage.longueurRotativeNonComprimee,
            demontage.longueurRotativeComprimee,
            demontage.longueurRotativeTravail,
            demontage.typeJoint2,
            demontage.matiere2,
            demontage.diametreArbre2,
            demontage.diametreExtPR2,
            demontage.diametreExtPF2,
            demontage.epaisseurPF2,
            demontage.longueurRotativeNonComprimee2,
            demontage.longueurRotativeComprimee2,
            demontage.longueurRotativeTravail2,
            demontage.resistanceTravail,
            demontage.resistanceDemarrage,
            demontage.valeurCondensateur,
            demontage.isolementMasseStatorPrincipalU,
            demontage.isolementMasseStatorPrincipalV,
            demontage.isolementMasseStatorPrincipalW,
            demontage.isolementMasseRotorPrincipal,
            demontage.isolementMasseStatorExcitation,
            demontage.isolementMasseRotorExcitation,
            demontage.resistanceRotorPrincipal,
            demontage.resistanceStatorExcitation,
            demontage.resistanceRotorExcitation,
            demontage.testDiode,
            demontage.isolementMasseInduit,
            demontage.isolementMassePolesPrincipaux,
            demontage.isolementMassePolesAuxilliaires,
            demontage.isolementMassePolesCompensatoires,
            demontage.isolementMassePorteBalais,
            demontage.resistanceInduit,
            demontage.resistancePP,
            demontage.resistancePA,
            demontage.resistancePC,
            demontage.isolementPhaseMasseStatorUM,
            demontage.isolementPhaseMasseStatorVM,
            demontage.isolementPhaseMasseStatorWM,
            demontage.isolementPhasePhaseStatorUV,
            demontage.isolementPhasePhaseStatorVW,
            demontage.isolementPhasePhaseStatorUW,
            demontage.resistanceStatorU,
            demontage.resistanceStatorV,
            demontage.resistanceStatorW,
            demontage.typeMotopompe,
            demontage.trMinute,
            demontage.modele,
            demontage.indiceReduction,
            demontage.typeHuile,
            demontage.quantiteHuile,
            demontage.roulements,
            demontage.joints,
            demontage.typeMotoreducteur
        )
        var call = service.patchFicheDemontage(token, ficheId, body)
        call.enqueue(callback)

    }


    fun getDemontage(token: String, ficheId: String, callback: Callback<DemontageResponse>) {
        val call = service.getDemontage(token, ficheId)
        var fiche: DemontageMoteur? = null
        call.enqueue(callback)
    }

    fun getDemontageReducteur(
        token: String,
        ficheId: String,
        callback: Callback<DemontageReducteurResponse>
    ) {
        val call = service.getDemontageReducteur(token, ficheId)
        call.enqueue(callback)
    }

    fun getDemontageMotoreducteur(
        token: String,
        ficheId: String,
        callback: Callback<DemontageMotoreducteurResponse>
    ) {
        val call = service.getDemontageMotoreducteur(token, ficheId)
        call.enqueue(callback)
    }

    fun getDemontageMotopompe(
        token: String,
        ficheId: String,
        callback: Callback<DemontageMotopompeResponse>
    ) {
        val call = service.getDemontageMotopompe(token, ficheId)
        call.enqueue(callback)
    }

    fun getDemontageMono(
        token: String,
        ficheId: String,
        callback: Callback<DemontageMonophaseResponse>
    ) {
        val call = service.getDemontageMonophase(token, ficheId)
        var fiche: DemontageMonophase? = null
        call.enqueue(callback)
    }

    fun getDemontageAlternateur(
        token: String,
        ficheId: String,
        callback: Callback<DemontageAlternateurResponse>
    ) {
        val call = service.getDemontageAlternateur(token, ficheId)
        var fiche: DemontageAlternateur? = null
        call.enqueue(callback)
    }

    fun getDemontageRB(
        token: String,
        ficheId: String,
        callback: Callback<DemontageRotorBobineResponse>
    ) {
        val call = service.getDemontageRotorBobine(token, ficheId)
        var fiche: DemontageRotorBobine? = null
        call.enqueue(callback)
    }

    fun getDemontageTriphase(
        token: String,
        ficheId: String,
        callback: Callback<DemontageTriphaseResponse>
    ) {
        var call = service.getDemontageTriphase(token, ficheId)
        var fiche: Triphase? = null
        call.enqueue(callback)
    }

    fun getDemontagePompe(
        token: String,
        ficheId: String,
        callback: Callback<DemontagePompeResponse>
    ) {
        var call = service.getDemoPompe(token, ficheId)
        var fiche: DemontagePompe? = null
        call.enqueue(callback)
    }

    fun patchDemontageTriphase(
        token: String,
        ficheId: String,
        triphase: Triphase,
        callback: Callback<DemontageTriphaseResponse>
    ) {
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
            triphase.dureeTotale!!,
            triphase.observations,
            triphase.photos
        )
        var call = service.patchDemontageTriphase(token, ficheId, body)
        var fiche: Triphase? = null
        call.enqueue(callback)
    }

    fun patchDemontageMotopompe(
        token: String,
        ficheId: String,
        motopompe: DemontageMotopompe,
        callback: Callback<DemontageMotopompeResponse>
    ) {
        var body = BodyDemoMotoPompe(
            motopompe.status,
            motopompe.dureeTotale!!.toInt(),
            motopompe.observations,
            motopompe.photos,
            motopompe.typeMoteur,
            motopompe.marque,
            motopompe.numSerie,
            motopompe.puissance,
            motopompe.bride,
            motopompe.vitesse,
            motopompe.arbreSortantEntrant,
            motopompe.accouplement,
            motopompe.coteAccouplement,
            motopompe.clavette,
            motopompe.aspect,
            motopompe.aspectInterieur,
            motopompe.couplage,
            motopompe.flasqueAvant,
            motopompe.flasqueArriere,
            motopompe.porteeRArriere,
            motopompe.porteeRAvant,
            motopompe.boutArbre,
            motopompe.rondelleElastique,
            motopompe.refRoulementAvant,
            motopompe.refRoulementArriere,
            motopompe.typeRoulementAvant,
            motopompe.typeRoulementArriere,
            motopompe.refJointAvant,
            motopompe.refJointArriere,
            motopompe.typeJointAvant,
            motopompe.typeJointArriere,
            motopompe.ventilateur,
            motopompe.capotV,
            motopompe.socleBoiteABorne,
            motopompe.capotBoiteABorne,
            motopompe.plaqueABorne,
            motopompe.presenceSondes,
            motopompe.typeSondes,
            motopompe.equilibrage,
            motopompe.peinture,
            motopompe.typeMotopompe,
            motopompe.fluide,
            motopompe.sensRotation,
            motopompe.typeRessort,
            motopompe.typeJoint,
            motopompe.matiere,
            motopompe.diametreArbre,
            motopompe.diametreExtPR,
            motopompe.diametreExtPF,
            motopompe.epaisseurPF,
            motopompe.longueurRotativeNonComprimee,
            motopompe.longueurRotativeComprimee,
            motopompe.longueurRotativeTravail,
            motopompe.isolementPhaseMasseStatorUM,
            motopompe.isolementPhaseMasseStatorVM,
            motopompe.isolementPhaseMasseStatorWM,
            motopompe.isolementPhasePhaseStatorUV,
            motopompe.isolementPhasePhaseStatorVW,
            motopompe.isolementPhasePhaseStatorUW,
            motopompe.resistanceStatorU,
            motopompe.resistanceStatorV,
            motopompe.resistanceStatorW,
            motopompe.tensionU,
            motopompe.tensionV,
            motopompe.tensionW,
            motopompe.intensiteU,
            motopompe.intensiteV,
            motopompe.intensiteW,
            motopompe.dureeEssai,
            motopompe.isolementPhaseMasse,
            motopompe.resistanceTravail,
            motopompe.resistanceDemarrage,
            motopompe.valeurCondensateur,
            motopompe.tension,
            motopompe.intensite,
            motopompe.typeJoint2,
            motopompe.matiere2,
            motopompe.diametreArbre2,
            motopompe.diametreExtPR2,
            motopompe.diametreExtPF2,
            motopompe.epaisseurPF2,
            motopompe.longueurRotativeNonComprimee2,
            motopompe.longueurRotativeComprimee2,
            motopompe.longueurRotativeTravail2,
        )
        var call = service.patchDemontageMotopompe(token, ficheId, body)
        call.enqueue(callback)
    }

    fun patchDemontageReducteur(
        token: String,
        ficheId: String,
        reducteur: DemontageReducteur,
        callback: Callback<DemontageReducteurResponse>
    ) {
        var body = BodyDemontageReducteur(
            reducteur.status,
            reducteur.dureeTotale!!.toInt(),
            reducteur.observations,
            reducteur.photos,
            reducteur.marque,
            reducteur.typeMoteur,
            reducteur.numSerie,
            reducteur.puissance,
            reducteur.bride,
            reducteur.vitesse,
            reducteur.arbreSortantEntrant,
            reducteur.accouplement,
            reducteur.coteAccouplement,
            reducteur.clavette,
            reducteur.peinture,
            reducteur.trMinute,
            reducteur.modele,
            reducteur.indiceReduction,
            reducteur.typeHuile,
            reducteur.quantiteHuile,
            reducteur.roulements,
            reducteur.joints
        )
        var call = service.patchDemontageReducteur(token, ficheId, body)
        call.enqueue(callback)
    }

    fun patchDemontageMotoreducteur(
        token: String,
        ficheId: String,
        motoreducteur: DemontageMotoreducteur,
        callback: Callback<DemontageMotoreducteurResponse>
    ) {
        var body = BodyDemoMotoReducteur(
            motoreducteur.status,
            motoreducteur.dureeTotale!!.toInt(),
            motoreducteur.observations,
            motoreducteur.photos,
            motoreducteur.typeMoteur,
            motoreducteur.marque,
            motoreducteur.numSerie,
            motoreducteur.puissance,
            motoreducteur.bride,
            motoreducteur.vitesse,
            motoreducteur.arbreSortantEntrant,
            motoreducteur.accouplement,
            motoreducteur.coteAccouplement,
            motoreducteur.clavette,
            motoreducteur.aspect,
            motoreducteur.aspectInterieur,
            motoreducteur.couplage,
            motoreducteur.flasqueAvant,
            motoreducteur.flasqueArriere,
            motoreducteur.porteeRArriere,
            motoreducteur.porteeRAvant,
            motoreducteur.boutArbre,
            motoreducteur.rondelleElastique,
            motoreducteur.refRoulementAvant,
            motoreducteur.refRoulementArriere,
            motoreducteur.typeRoulementAvant,
            motoreducteur.typeRoulementArriere,
            motoreducteur.refJointAvant,
            motoreducteur.refJointArriere,
            motoreducteur.typeJointAvant,
            motoreducteur.typeJointArriere,
            motoreducteur.ventilateur,
            motoreducteur.capotV,
            motoreducteur.socleBoiteABorne,
            motoreducteur.capotBoiteABorne,
            motoreducteur.plaqueABorne,
            motoreducteur.presenceSondes,
            motoreducteur.typeSondes,
            motoreducteur.equilibrage,
            motoreducteur.peinture,
            motoreducteur.typeMotoreducteur,
            motoreducteur.trMinute,
            motoreducteur.modele,
            motoreducteur.indiceReduction,
            motoreducteur.typeHuile,
            motoreducteur.quantiteHuile,
            motoreducteur.roulements,
            motoreducteur.joints,
            motoreducteur.isolementPhaseMasseStatorUM,
            motoreducteur.isolementPhaseMasseStatorVM,
            motoreducteur.isolementPhaseMasseStatorWM,
            motoreducteur.isolementPhasePhaseStatorUV,
            motoreducteur.isolementPhasePhaseStatorVW,
            motoreducteur.isolementPhasePhaseStatorUW,
            motoreducteur.resistanceStatorU,
            motoreducteur.resistanceStatorV,
            motoreducteur.resistanceStatorW,
            motoreducteur.tensionU,
            motoreducteur.tensionV,
            motoreducteur.tensionW,
            motoreducteur.intensiteU,
            motoreducteur.intensiteV,
            motoreducteur.intensiteW,
            motoreducteur.dureeEssai,
            motoreducteur.isolementPhaseMasse,
            motoreducteur.resistanceTravail,
            motoreducteur.resistanceDemarrage,
            motoreducteur.valeurCondensateur,
            motoreducteur.tension,
            motoreducteur.intensite
        )
        var call = service.patchDemontageMotoreducteur(token, ficheId, body)
        call.enqueue(callback)
    }

    fun patchDemontageMeca(
        token: String,
        ficheId: String,
        triphase: Triphase,
        callback: Callback<DemontageTriphaseResponse>
    ) {
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
            triphase.dureeTotale!!,
            triphase.observations,
            triphase.photos
        )
        var call = service.patchDemontageTriphase(token, ficheId, body)
        var fiche: Triphase? = null
        call.enqueue(callback)
    }

    fun getDemontageCC(token: String, ficheId: String, callback: Callback<DemontageCCResponse>) {
        var call = service.getDemontageCC(token, ficheId)
        var fiche: CourantContinu? = null
        call.enqueue(callback)
    }

    fun patchDemontageCC(
        token: String,
        ficheId: String,
        fiche: CourantContinu,
        callback: Callback<DemontageCCResponse>
    ) {
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
            fiche.isolementMasseInduit,
            fiche.isolementMassePolesPrincipaux,
            fiche.isolementMassePolesAuxilliaires,
            fiche.isolementMassePolesCompensatoires,
            fiche.isolementMassePorteBalais,
            fiche.resistanceInduit,
            fiche.resistancePP,
            fiche.resistancePA,
            fiche.resistancePC,
            fiche.tensionInduit,
            fiche.intensiteInduit,
            fiche.tensionExcitation,
            fiche.intensiteExcitation,
            fiche.dureeTotale!!,
            fiche.observations,
            fiche.photos
        )
        var call = service.patchDemontageCC(token, ficheId, body)
        var fiche: CourantContinu? = null
        call.enqueue(callback)
    }


    fun patchDemontagePompe(
        token: String,
        ficheId: String,
        fiche: DemontagePompe,
        callback: Callback<DemontagePompeResponse>
    ) {
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
            fiche.dureeTotale,
            fiche.photos,
            fiche.refRoulementAvant,
            fiche.refRoulementArriere,
            fiche.typeRoulementAvant,
            fiche.typeRoulementArriere,
            fiche.refJointAvant,
            fiche.refJointArriere,
            fiche.typeJointAvant,
            fiche.typeJointArriere,
            fiche.typeJoint2,
            fiche.matiere2,
            fiche.diametreArbre2,
            fiche.diametreExtPR2,
            fiche.diametreExtPF2,
            fiche.epaisseurPF2,
            fiche.longueurRotativeNonComprimee2,
            fiche.longueurRotativeComprimee2,
            fiche.longueurRotativeTravail2,
        )
        var call = service.patchDemontagePompe(token, ficheId, body)
        var fiche: DemontagePompe? = null
        call.enqueue(callback)

    }

    fun patchDemontageMono(
        token: String,
        ficheId: String,
        fiche: DemontageMonophase,
        callback: Callback<DemontageMonophaseResponse>
    ) {
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
            fiche.dureeTotale!!.toInt(),
            fiche.photos
        )
        var call = service.patchDemontageMonophase(token, ficheId, body)
        var fiche: DemontageMonophase? = null
        call.enqueue(callback)

    }

    fun patchDemontageAlter(
        token: String,
        ficheId: String,
        fiche: DemontageAlternateur,
        callback: Callback<DemontageAlternateurResponse>
    ) {
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
            fiche.dureeTotale!!.toInt(),
            fiche.photos
        )
        var call = service.patchDemontageAlternateur(token, ficheId, body)
        var fiche: DemontageAlternateur? = null
        call.enqueue(callback)

    }

    fun patchDemontageRotor(
        token: String,
        ficheId: String,
        fiche: DemontageRotorBobine,
        callback: Callback<DemontageRotorBobineResponse>
    ) {
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
            fiche.isolementPhaseMasseStatorUM,
            fiche.isolementPhaseMasseStatorVM,
            fiche.isolementPhaseMasseStatorWM,
            fiche.isolementPhaseMasseRotorB1M,
            fiche.isolementPhaseMasseRotorB2M,
            fiche.isolementPhaseMasseRotorB3M,
            fiche.isolementPhaseMassePorteBalaisM,
            fiche.isolementPhasePhaseStatorUV,
            fiche.isolementPhasePhaseStatorVW,
            fiche.isolementPhasePhaseStatorUW,
            fiche.resistanceStatorU,
            fiche.resistanceStatorV,
            fiche.resistanceStatorW,
            fiche.resistanceRotorB1B2,
            fiche.resistanceRotorB2B2,
            fiche.resistanceRotorB1B3,
            fiche.tensionU,
            fiche.tensionV,
            fiche.tensionW,
            fiche.tensionRotor,
            fiche.intensiteU,
            fiche.intensiteV,
            fiche.intensiteW,
            fiche.intensiteRotor,
            fiche.dureeEssai,
            fiche.dureeTotale!!.toInt(),
            fiche.photos
        )
        var call = service.patchDemontageRotorBobine(token, ficheId, body)
        var fiche: DemontageRotorBobine? = null
        call.enqueue(callback)

    }

    //dao demontage triphase
    suspend fun insertDemontageLocalDatabase(demo: FicheDemontage) {
        demontageDao!!.insertAll(demo.toEntity())
    }

    suspend fun getAllDemontageLocalDatabase(): List<DemontageEntity> {
        return demontageDao!!.getAll()
    }

    suspend fun getByIdDemontageLocalDatabse(id: String): FicheDemontage? {
        try {
            if (demontageDao!!.getById(id) !== null) {
                return demontageDao!!.getById(id).toFicheDemontage()
            } else return null
        } catch (e: Error) {
            Log.i("e", e.message!!)
            return null
        }
    }

    suspend fun updateDemontageLocalDatabse(demo: DemontageEntity) {
        demontageDao!!.update(demo)
    }

    suspend fun deleteDemontageLocalDatabse(demo: DemontageEntity) {
        demontageDao!!.delete(demo)
    }

    //dao demontage triphase
    suspend fun insertDemoTriLocalDatabase(demo: Triphase) {
        demontageTriphaseDao!!.insertAll(demo.toEntity())
    }

    suspend fun getAllDemontageTriLocalDatabase(): List<DemontageTriphaseEntity> {
        return demontageTriphaseDao!!.getAll()
    }

    suspend fun getByIdDemoTriLocalDatabse(id: String): Triphase? {
        try {
            if (demontageTriphaseDao!!.getById(id) !== null) {
                return demontageTriphaseDao!!.getById(id).toTriphase()
            } else return null
        } catch (e: Error) {
            Log.i("e", e.message!!)
            return null
        }
    }

    suspend fun updateDemoTriLocalDatabse(demo: DemontageTriphaseEntity) {
        demontageTriphaseDao!!.update(demo)
    }

    suspend fun deleteDemontageTriphaseLocalDatabse(demo: DemontageTriphaseEntity) {
        demontageTriphaseDao!!.delete(demo)
    }

    // dao demo courant continu
    suspend fun insertDemoCCLocalDatabase(demo: CourantContinu) {
        demontageCCDao!!.insertAll(demo.toEntity())
    }

    suspend fun getAllDemontageCCLocalDatabase(): List<DemontageCCEntity> {
        return demontageCCDao!!.getAll()
    }

    suspend fun getByIdDemoCCLocalDatabse(id: String): CourantContinu? {
        try {
            if (demontageCCDao!!.getById(id) !== null) {
                return demontageCCDao!!.getById(id).toCContinu()
            } else return null
        } catch (e: Error) {
            Log.i("e", e.message!!)
            return null
        }
    }

    suspend fun updateDemoCCLocalDatabse(demo: DemontageCCEntity) {
        demontageCCDao!!.update(demo)
    }

    suspend fun deleteDemontageCCLocalDatabse(demo: DemontageCCEntity) {
        demontageCCDao!!.delete(demo)
    }

    //demo pompes
    suspend fun insertDemoPompeLocalDatabase(demo: DemontagePompe) {
        demontagePDao!!.insertAll(demo.toEntity())
    }

    suspend fun getAllDemontagePompeLocalDatabase(): List<DemoPompeEntity> {
        return demontagePDao!!.getAll()
    }

    suspend fun getByIdDemoPompeLocalDatabse(id: String): DemontagePompe? {
        try {
            if (demontagePDao!!.getById(id) !== null) {
                return demontagePDao!!.getById(id).toDemoPompe()
            } else return null
        } catch (e: Error) {
            Log.i("e", e.message!!)
            return null
        }
    }

    suspend fun updateDemoPompeLocalDatabse(demo: DemoPompeEntity) {
        demontagePDao!!.update(demo)
    }

    suspend fun deleteDemontagePompeLocalDatabse(demo: DemoPompeEntity) {
        demontagePDao!!.delete(demo)
    }

    //demo mono
    suspend fun insertDemoMonoLocalDatabase(demo: DemontageMonophase) {
        demontageMonoDao!!.insertAll(demo.toEntity())
    }

    suspend fun getAllDemontageMonoLocalDatabase(): List<DemontageMonophaseEntity> {
        return demontageMonoDao!!.getAll()
    }

    suspend fun getByIdDemoMonoLocalDatabse(id: String): DemontageMonophase? {
        try {
            if (demontageMonoDao!!.getById(id) !== null) {
                return demontageMonoDao!!.getById(id).toMonophase()
            } else return null
        } catch (e: Error) {
            Log.i("e", e.message!!)
            return null
        }
    }

    suspend fun updateDemoMonoLocalDatabse(demo: DemontageMonophaseEntity) {
        demontageMonoDao!!.update(demo)
    }

    suspend fun deleteDemontageMonoLocalDatabse(demo: DemontageMonophaseEntity) {
        demontageMonoDao!!.delete(demo)
    }

    //demo Alternateur
    suspend fun insertDemoAlterLocalDatabase(demo: DemontageAlternateur) {
        demontageAlterDao!!.insertAll(demo.toEntity())
    }

    suspend fun getAllDemontageAlterLocalDatabase(): List<DemontageAlternateurEntity> {
        return demontageAlterDao!!.getAll()
    }

    suspend fun getByIdDemoAlterLocalDatabse(id: String): DemontageAlternateur? {
        try {
            if (demontageAlterDao!!.getById(id) !== null) {
                return demontageAlterDao!!.getById(id).toDemontageAlternateur()
            } else return null
        } catch (e: Error) {
            Log.i("e", e.message!!)
            return null
        }
    }

    suspend fun updateDemoAlterLocalDatabse(demo: DemontageAlternateurEntity) {
        demontageAlterDao!!.update(demo)
    }

    suspend fun deleteDemontageAlterLocalDatabse(demo: DemontageAlternateurEntity) {
        demontageAlterDao!!.delete(demo)
    }

    //demo Rotor
    suspend fun insertDemoRBLocalDatabase(demo: DemontageRotorBobine) {
        demontageRBDao!!.insertAll(demo.toEntity())
    }

    suspend fun getAllDemontageRBLocalDatabase(): List<DemontageRotorBEntity> {
        return demontageRBDao!!.getAll()
    }

    suspend fun getByIdDemoRBLocalDatabse(id: String): DemontageRotorBobine? {
        try {
            if (demontageRBDao!!.getById(id) !== null) {
                return demontageRBDao!!.getById(id).toDemoRotorB()
            } else return null
        } catch (e: Error) {
            Log.i("e", e.message!!)
            return null
        }
    }

    suspend fun updateDemoRBLocalDatabse(demo: DemontageRotorBEntity) {
        demontageRBDao!!.update(demo)
    }

    suspend fun deleteDemontageRBLocalDatabse(demo: DemontageRotorBEntity) {
        demontageRBDao!!.delete(demo)
    }

    suspend fun insertDemoMotopompeDatabase(demo: DemontageMotopompe) {
        demontageMotopompeDao!!.insertAll(demo.toEntity())
    }

    suspend fun getAllDemontageMotopompeLocalDatabase(): List<DemontageMotopompeEntity> {
        return demontageMotopompeDao!!.getAll()
    }

    suspend fun getByIdDemoMotopompeLocalDatabase(id: String): DemontageMotopompe? {
        try {
            if (demontageMotopompeDao!!.getById(id) !== null) {
                return demontageMotopompeDao!!.getById(id).toMotoPompe()
            } else return null
        } catch (e: Error) {
            Log.i("e", e.message!!)
            return null
        }
    }

    suspend fun updateDemoMotoPompeLocalDatabase(demo: DemontageMotopompeEntity) {
        demontageMotopompeDao!!.update(demo)
    }

    suspend fun deleteDemontageMotoPompeLocalDatabse(demo: DemontageMotopompeEntity) {
        demontageMotopompeDao!!.delete(demo)
    }

    suspend fun insertDemoMotoreducteurDatabase(demo: DemontageMotoreducteur) {
        demontageMotoreducteurDao!!.insertAll(demo.toEntity())
    }

    suspend fun getAllDemontageMotoreducteurLocalDatabase(): List<DemontageMotoreducteurEntity> {
        return demontageMotoreducteurDao!!.getAll()
    }

    suspend fun getByIdDemoMotoreducteurLocalDatabase(id: String): DemontageMotoreducteur? {
        try {
            if (demontageMotoreducteurDao!!.getById(id) !== null) {
                return demontageMotoreducteurDao!!.getById(id).toDemontageMotoreducteur()
            } else return null
        } catch (e: Error) {
            Log.i("e", e.message!!)
            return null
        }
    }

    suspend fun updateDemoMotoreducteurLocalDatabase(demo: DemontageMotoreducteurEntity) {
        demontageMotoreducteurDao!!.update(demo)
    }

    suspend fun deleteDemontageMotoreducteurLocalDatabse(demo: DemontageMotoreducteurEntity) {
        demontageMotoreducteurDao!!.delete(demo)
    }

    suspend fun insertDemoReducteurDatabase(demo: DemontageReducteur) {
        demontageReducteurDao!!.insertAll(demo.toEntity())
    }

    suspend fun getAllDemontageReducteurLocalDatabase(): List<DemontageReducteurEntity> {
        return demontageReducteurDao!!.getAll()
    }

    suspend fun getByIdDemoReducteurLocalDatabase(id: String): DemontageReducteur? {
        try {
            if (demontageReducteurDao!!.getById(id) !== null) {
                return demontageReducteurDao!!.getById(id).toReducteur()
            } else return null
        } catch (e: Error) {
            Log.i("e", e.message!!)
            return null
        }
    }

    suspend fun updateDemoReducteurLocalDatabase(demo: DemontageReducteurEntity) {
        demontageReducteurDao!!.update(demo)
    }

    suspend fun deleteDemontageReducteurLocalDatabse(demo: DemontageReducteurEntity) {
        demontageReducteurDao!!.delete(demo)
    }

}