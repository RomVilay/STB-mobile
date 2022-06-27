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
                          var dureeTotale : Long?,
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
        parcel.readLong(),
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
        parcel.writeLong(dureeTotale!!)
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
class ListFicheDemontageResponse(
    var data: Array<FicheDemontage>
)
class DemontageRepository(var service: APIstb, var db: LocalDatabase) {
    var demontageDao = db!!.demontageDao()

    fun getFicheDemontage(token: String, ficheId: String, callback: Callback<FicheDemontageResponse>) {
        val call = service.getFicheDemontage(token, ficheId)
        call.enqueue(callback)
    }

    suspend fun getFicheForRemontage(token: String,numDevis:String) = service.getFichesDemontages(token,numDevis)

    fun patchFicheDemontage(token: String,
                            ficheId: String,
                            demontage: FicheDemontage,
                            callback: Callback<FicheDemontageResponse>){
        var body = BodyFicheDemontage(
            demontage.status!!.toInt(),
            demontage.subtype,
            demontage.dureeTotale,
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

    //dao demontage
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
}