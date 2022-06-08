package com.example.applicationstb.repository

import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.applicationstb.localdatabase.*
import com.example.applicationstb.model.*
import retrofit2.Callback

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


class RemontageResponse(
    var data: FicheRemontage?
)

class RemontageRepository(var service: APIstb, var db: LocalDatabase) {
    var remontageDao = db!!.remontageDao()

    fun getRemontage(token: String, ficheId: String, callback: Callback<RemontageResponse>) {
        val call = service.getRemontage(token, ficheId)
        var fiche: Remontage? = null
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
            fiche.intensiteInducteursU,
            fiche.intensiteInducteursV,
            fiche.intensiteInducteursW,
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
        call.enqueue(callback)
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

}