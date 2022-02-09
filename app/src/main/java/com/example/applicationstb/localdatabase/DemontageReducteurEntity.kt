package com.example.applicationstb.localdatabase

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.applicationstb.model.Client
import com.example.applicationstb.model.DemontageReducteur
import com.example.applicationstb.model.Joint
import com.example.applicationstb.model.Roulement
import java.util.*

@Entity(tableName = "demontage_reducteur")
data class DemontageReducteurEntity(
    @PrimaryKey var _id: String,
    var numDevis: String?,
    var numFiche: String?,
    var statut: Long?,
    var client: String,
    var contact: String?,
    var telContact: String?,
    var dateDebut: Date?,
    var dureeTotale: Long?,
    var observation: String?,
    var photos: Array<String>?,
    var typeFicheDemontage: Int?,
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
) {
    fun toReducteur(): DemontageReducteur {
        return DemontageReducteur(
            _id,
            numDevis,
            numFiche,
            2,
            statut,
            Client(client, null, null, null, null),
            contact,
            telContact,
            null,
            null,
            dateDebut,
            dureeTotale,
            observation,
            photos,
            8,
            marque,
            typeMoteur,
            numSerie,
            puissance,
            bride,
            vitesse,
            arbreSortantEntrant,
            accouplement,
            coteAccouplement,
            clavette,
            peinture,
            trMinute,
            modele,
            indiceReduction,
            typeHuile,
            quantiteHuile,
            roulements,
            joints
        )
    }
}
