package com.example.applicationstb.model

import com.example.applicationstb.localdatabase.*
import java.sql.Timestamp
import java.util.*

open class Remontage(
    idFiche: String,
    numDevis: String,
    numFiche: String,
    type: Long,
    statut: Long,
    client: Client,
    contact: String?,
    telContact: String?,
    techniciens: Array<User>?,
    resp: User?,
    dateDebut: Date?,
    dureeTotale: Long?,
    observation: String?,
    photos: Array<String>?,
    open var typeFicheRemontage: Int?,
    open var remontageRoulement: Int?,
    open var collageRoulementPorteeArbre: Int?,
    open var collageRoulementFlasque: Int?,
) : Fiche(
    idFiche,
    numDevis,
    numFiche,
    type,
    statut,
    client,
    contact,
    telContact,
    techniciens,
    resp,
    dateDebut,
    dureeTotale,
    observation,
    photos
) {
    fun toRemoEntity(): RemontageEntity {
        return RemontageEntity(
            _id,
            numDevis,
            numFiche,
            type,
            status,
            client!!._id,
            contact,
            telContact,
            dureeTotale,
            observations,
            photos,
            typeFicheRemontage,
            remontageRoulement,
            collageRoulementPorteeArbre,
            collageRoulementFlasque
        )
    }
}

class RemontageTriphase(
    idFiche: String,
    numDevis: String,
    numFiche: String,
    type: Long,
    statut: Long,
    client: Client,
    contact: String?,
    telContact: String?,
    techniciens: Array<User>?,
    resp: User?,
    dateDebut: Date?,
    dureeTotale: Long?,
    observation: String?,
    photos: Array<String>?,
    remontageRoulement: Int?,
    collageRoulementPorteeArbre: Int?,
    collageRoulementFlasque: Int?,
     var verificationFixationCouronne: Boolean?,
     var verificationIsolementPorteBalais: Boolean?,
     var isolementPorteBalaisV: Float?,
     var isolementPorteBalaisOhm: Float?,
    // essais dynamiques
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
//essais vibratoires
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
     var tensionU:Float?,
     var tensionV:Float?,
     var tensionW:Float?,
     var intensiteU:Float?,
     var intensiteV:Float?,
     var intensiteW:Float?,
     var dureeEssai: Float?
) : Remontage(
    idFiche,
    numDevis,
    numFiche,
    type,
    statut,
    client,
    contact,
    telContact,
    techniciens,
    resp,
    dateDebut,
    dureeTotale,
    observation,
    photos,
    1,
    remontageRoulement,
    collageRoulementPorteeArbre,
    collageRoulementFlasque
) {
    fun toEntity(): RemontageTriphaseEntity {
        return RemontageTriphaseEntity(
            _id,
            numDevis,
            numFiche,
            type,
            status,
            client!!._id,
            contact,
            telContact,
            dureeTotale,
            observations,
            photos,
            remontageRoulement,
            collageRoulementPorteeArbre,
            collageRoulementFlasque,
            verificationFixationCouronne,
            verificationIsolementPorteBalais,
            isolementPorteBalaisV,
            isolementPorteBalaisOhm,
            vitesse1V,  // vitesse 1v
            acceleration1V,  //accélération 1v
            vitesse2V,  // vitesse 2v
            acceleration2V,  //accélération 2v
            vitesse1H,  // vitesse 1H
            acceleration1H,  //accélération 1H
            vitesse2H,  // vitesse 2H
            acceleration2H,  //accélération 2H
            vitesse2A,  // vitesse 2A
            acceleration2A,  //accélération 2A
            isolementPhaseMasse,
            isolementPhase,
            resistanceStatorU,
            resistanceStatorV,
            resistanceStatorW,
            isolementPMStatorU,
            isolementPMStatorV,
            isolementPMStatorW,
            isolementPMRotorU,
            isolementPMRotorV,
            isolementPMRotorW,
            isolementPhaseStatorUV,
            isolementPhaseStatorVW,
            isolementPhaseStatorUW,
            isolementPhaseRotorUV,
            isolementPhaseRotorVW,
            isolementPhaseRotorUW,
            tensionU,
            tensionV,
            tensionW,
            intensiteU,
            intensiteV,
            intensiteW,
            dureeEssai,
        )

    }
}

class RemontageMonophase(
    idFiche: String,
    numDevis: String,
    numFiche: String,
    type: Long,
    statut: Long,
    client: Client,
    contact: String?,
    telContact: String?,
    techniciens: Array<User>?,
    resp: User?,
    dateDebut: Date?,
    dureeTotale: Long?,
    observation: String?,
    photos: Array<String>?,
    remontageRoulement: Int?,
    collageRoulementPorteeArbre: Int?,
    collageRoulementFlasque: Int?,
    var verificationFixationCouronne: Boolean?,
    var verificationIsolementPorteBalais: Boolean?,
    var isolementPorteBalaisV: Float?,
    var isolementPorteBalaisOhm: Float?,
    // essais dynamiques
    var resistanceTravail: Float?,
    var resistanceDemarage:Float?,
    var valeurCondensateur:Float?,
    var tension:Float?,
    var intensite:Float?,
//essais vibratoires
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
) : Remontage(
    idFiche,
    numDevis,
    numFiche,
    type,
    statut,
    client,
    contact,
    telContact,
    techniciens,
    resp,
    dateDebut,
    dureeTotale,
    observation,
    photos,
    2,
    remontageRoulement,
    collageRoulementPorteeArbre,
    collageRoulementFlasque
) {
    fun toEntity(): RemontageMonophaseEntity {
        return RemontageMonophaseEntity(
            _id,
            numDevis,
            numFiche,
            type,
            status,
            client!!._id,
            contact,
            telContact,
            dureeTotale,
            observations,
            photos,
            remontageRoulement,
            collageRoulementPorteeArbre,
            collageRoulementFlasque,
            verificationFixationCouronne,
            verificationIsolementPorteBalais,
            isolementPorteBalaisV,
            isolementPorteBalaisOhm,
            resistanceTravail,
            resistanceDemarage,
            valeurCondensateur,
            tension,
            intensite,
            vitesse1V,  // vitesse 1v
            acceleration1V,  //accélération 1v
            vitesse2V,  // vitesse 2v
            acceleration2V,  //accélération 2v
            vitesse1H,  // vitesse 1H
            acceleration1H,  //accélération 1H
            vitesse2H,  // vitesse 2H
            acceleration2H,  //accélération 2H
            vitesse2A,  // vitesse 2A
            acceleration2A,  //accélération 2A
        )

    }
}

class RemontageCourantC(
    idFiche: String,
    numDevis: String,
    numFiche: String,
    type: Long,
    statut: Long,
    client: Client,
    contact: String?,
    telContact: String?,
    techniciens: Array<User>?,
    resp: User?,
    dateDebut: Date?,
    dureeTotale: Long?,
    observation: String?,
    photos: Array<String>?,
    remontageRoulement: Int?,
    collageRoulementPorteeArbre: Int?,
    collageRoulementFlasque: Int?,
    var verificationFixationCouronne: Boolean?,
    var verificationIsolementPorteBalais: Boolean?,
    var isolementPorteBalaisV: Float?,
    var isolementPorteBalaisOhm: Float?,
    //essais vibratoires
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
    var isolementPolePrincipal: Float?,
    var isolementPoleAuxilliaire:Float?,
    var isolementPoleCompensatoire:Float?,
    var isolementBoiteBalais:Float?,
    var resistanceInduit:Float?,
    var resistancePolePrincipal:Float?,
    var resistancePoleAuxilliaire:Float?,
    var resistancePoleCompensatoire:Float?,
    var tensionInduit:Float?,
    var tensionExcit:Float?,
    var intensiteInduit:Float?,
    var intensiteExcit:Float?
) : Remontage(
    idFiche,
    numDevis,
    numFiche,
    type,
    statut,
    client,
    contact,
    telContact,
    techniciens,
    resp,
    dateDebut,
    dureeTotale,
    observation,
    photos,
    2,
    remontageRoulement,
    collageRoulementPorteeArbre,
    collageRoulementFlasque
) {
    fun toEntity(): RemontageCCEntity {
        return RemontageCCEntity(
            _id,
            numDevis,
            numFiche,
            type,
            status,
            client!!._id,
            contact,
            telContact,
            dureeTotale,
            observations,
            photos,
            remontageRoulement,
            collageRoulementPorteeArbre,
            collageRoulementFlasque,
            verificationFixationCouronne,
            verificationIsolementPorteBalais,
            isolementPorteBalaisV,
            isolementPorteBalaisOhm,
            // essais dynamiques
            vitesse1V,  // vitesse 1v
            acceleration1V,  //accélération 1v
            vitesse2V,  // vitesse 2v
            acceleration2V,  //accélération 2v
            vitesse1H,  // vitesse 1H
            acceleration1H,  //accélération 1H
            vitesse2H,  // vitesse 2H
            acceleration2H,  //accélération 2H
            vitesse2A,  // vitesse 2A
            acceleration2A,  //accélération 2A
            isolementPolePrincipal,
            isolementPoleAuxilliaire,
            isolementPoleCompensatoire,
            isolementBoiteBalais,
            resistanceInduit,
            resistancePolePrincipal,
            resistancePoleAuxilliaire,
            resistancePoleCompensatoire,
            tensionInduit,
            tensionExcit,
            intensiteInduit,
            intensiteExcit
        )

    }
}

class RemontageMotopompe(
    idFiche: String,
    numDevis: String,
    numFiche: String,
    type: Long,
    statut: Long,
    client: Client,
    contact: String?,
    telContact: String?,
    techniciens: Array<User>?,
    resp: User?,
    dateDebut: Date?,
    dureeTotale: Long?,
    observation: String?,
    photos: Array<String>?,
    remontageRoulement: Int?,
    collageRoulementPorteeArbre: Int?,
    collageRoulementFlasque: Int?,
    var verificationFixationCouronne: Boolean?,
    var verificationIsolementPorteBalais: Boolean?,
    var isolementPorteBalaisV: Float?,
    var isolementPorteBalaisOhm: Float?,
    // essais dynamiques
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

//essais vibratoires
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
) : Remontage(
    idFiche,
    numDevis,
    numFiche,
    type,
    statut,
    client,
    contact,
    telContact,
    techniciens,
    resp,
    dateDebut,
    dureeTotale,
    observation,
    photos,
    7,
    remontageRoulement,
    collageRoulementPorteeArbre,
    collageRoulementFlasque,
) {
    fun toEntity(): RemontageMotopompeEntity {
        return RemontageMotopompeEntity(
            _id,
            numDevis,
            numFiche,
            status,
            client!!._id,
            contact,
            telContact,
            dateDebut,
            dureeTotale,
            observations,
            photos,
            remontageRoulement,
            collageRoulementPorteeArbre,
            collageRoulementFlasque,
            verificationFixationCouronne,
            verificationIsolementPorteBalais,
            isolementPorteBalaisV,
            isolementPorteBalaisOhm,
            tensionStator,
            tensionStatorU,
            tensionStatorV,
            tensionStatorW,
            tensionInducteurs,
            tensionInducteursU,
            tensionInducteursV,
            tensionInducteursW,
            intensiteStator,
            intensiteStatorU,
            intensiteStatorV,
            intensiteStatorW,
            intensiteInducteurs,
            intensiteInducteursU,
            intensiteInducteursV,
            intensiteInducteursW,
            tensionInduit,
            tensionInduitU,
            tensionInduitV,
            tensionInduitW,
            tensionRotor,
            tensionRotorU,
            tensionRotorV,
            tensionRotorW,
            intensiteInduit,
            intensiteInduitU,
            vitesseU,
            puissanceU,
            dureeEssai,
            sensRotation,
//essais vibratoires
            vitesse1V,  // vitesse 1v
            acceleration1V,  //accélération 1v
            vitesse2V,  // vitesse 2v
            acceleration2V,  //accélération 2v
            vitesse1H,  // vitesse 1H
            acceleration1H,  //accélération 1H
            vitesse2H,  // vitesse 2H
            acceleration2H,  //accélération 2H
            vitesse2A,  // vitesse 2acceleration
            acceleration2A,
            typeMotopompe,
            isolementPhaseMasse,
            isolementPhase,
            resistanceStatorU,
            resistanceStatorV,
            resistanceStatorW,
            isolementPMStatorU,
            isolementPMStatorV,
            isolementPMStatorW,
            isolementPMRotorU,
            isolementPMRotorV,
            isolementPMRotorW,
            isolementPhaseStatorUV,
            isolementPhaseStatorVW,
            isolementPhaseStatorUW,
            isolementPhaseRotorUV,
            isolementPhaseRotorVW,
            isolementPhaseRotorUW
        )
    }
}

class RemontageMotoreducteur(
    idFiche: String,
    numDevis: String,
    numFiche: String,
    type: Long,
    statut: Long,
    client: Client,
    contact: String?,
    telContact: String?,
    techniciens: Array<User>?,
    resp: User?,
    dateDebut: Date?,
    dureeTotale: Long?,
    observation: String?,
    photos: Array<String>?,
    typeFicheRemontage: Int?,
    remontageRoulement: Int?,
    collageRoulementPorteeArbre: Int?,
    collageRoulementFlasque: Int?,
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
) : Remontage(
    idFiche,
    numDevis,
    numFiche,
    type,
    statut,
    client,
    contact,
    telContact,
    techniciens,
    resp,
    dateDebut,
    dureeTotale,
    observation,
    photos,
    8,
    remontageRoulement,
    collageRoulementPorteeArbre,
    collageRoulementFlasque
) {
    fun toEntity(): RemontageMotoreducteurEntity {
        return RemontageMotoreducteurEntity(
            _id,
            numDevis,
            numFiche,
            status,
            client!!._id,
            contact,
            telContact,
            dateDebut,
            dureeTotale,
            observations,
            photos,
            typeFicheRemontage,
            remontageRoulement,
            collageRoulementPorteeArbre,
            collageRoulementFlasque,
            typeMotoreducteur,
            isolementPhaseMasse,
            isolementPhase,
            resistanceStatorU,
            resistanceStatorV,
            resistanceStatorW,
            isolementPMStatorU,
            isolementPMStatorV,
            isolementPMStatorW,
            isolementPMRotorU,
            isolementPMRotorV,
            isolementPMRotorW,
            isolementPhaseStatorUV,
            isolementPhaseStatorVW,
            isolementPhaseStatorUW,
            isolementPhaseRotorUV,
            isolementPhaseRotorVW,
            isolementPhaseRotorUW
        )
    }
}

class RemontageReducteur(
    idFiche: String,
    numDevis: String,
    numFiche: String,
    type: Long,
    statut: Long,
    client: Client,
    contact: String?,
    telContact: String?,
    techniciens: Array<User>?,
    resp: User?,
    dateDebut: Date?,
    dureeTotale: Long?,
    observation: String?,
    photos: Array<String>?,
    typeFicheRemontage: Int?,
    remontageRoulement: Int?,
    collageRoulementPorteeArbre: Int?,
    collageRoulementFlasque: Int?
    ) : Remontage(
    idFiche,
    numDevis,
    numFiche,
    type,
    statut,
    client,
    contact,
    telContact,
    techniciens,
    resp,
    dateDebut,
    dureeTotale,
    observation,
    photos,
    8,
    remontageRoulement,
    collageRoulementPorteeArbre,
    collageRoulementFlasque
) {
    fun toEntity(): RemontageEntity {
        return RemontageEntity(
            _id,
            numDevis!!,
            numFiche,
            type,
            status,
            client!!._id,
            contact,
            telContact,
            dureeTotale,
            observations,
            photos,
            typeFicheRemontage,
            remontageRoulement,
            collageRoulementPorteeArbre,
            collageRoulementFlasque,
        )
    }
}
