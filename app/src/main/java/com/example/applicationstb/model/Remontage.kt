package com.example.applicationstb.model

import com.example.applicationstb.localdatabase.RemontageCCEntity
import com.example.applicationstb.localdatabase.RemontageTriphaseEntity
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
    photo: Array<String>?,
    var typeFicheRemontage: Int?,
    open var remontageRoulement: Int?,
    open var collageRoulementPorteeArbre: Int?,
    open var collageRoulementFlasque: Int?,
    open var verificationFixationCouronne: Boolean?,
    open var verificationIsolementPorteBalais: Boolean?,
    open var isolementPorteBalaisV: Int?,
    open var isolementPorteBalaisOhm: Int?,
    // essais dynamiques
    open var tensionStatorInducteurs: Boolean?,
    open var tensionStatorInducteursU: Float?,
    open var tensionStatorInducteursV: Float?,
    open var tensionStatorInducteursW: Float?,
    open var intensiteStatorInducteurs: Boolean?,
    open var intensiteStatorInducteursU: Float?,
    open var intensiteStatorInducteursV: Float?,
    open var intensiteStatorInducteursW: Float?,
    open var tensionInduitRotor: Boolean?,
    open var tensionInduitRotorU: Float?,
    open var tensionInduitRotorV: Float?,
    open var tensionInduitRotorW: Float?,
    open var intensiteInduit: Boolean,
    open var intensiteInduitU: Float?,
    open var vitesseU: Float?,
    open var puissanceU: Float?,
    open var dureeEssai: Float?,
    open var sensRotation: Int?,

//essais vibratoires
    open var vitesse1V: Float?,  // vitesse 1v
    open var acceleration1V: Float?,  //accélération 1v
    open var vitesse2V: Float?,  // vitesse 2v
    open var acceleration2V: Float?,  //accélération 2v
    open var vitesse1H: Float?,  // vitesse 1H
    open var acceleration1H: Float?,  //accélération 1H
    open var vitesse2H: Float?,  // vitesse 2H
    open var acceleration2H: Float?,  //accélération 2H
    open var vitesse2A: Float?,  // vitesse 2A
    open var acceleration2A: Float?,  //accélération 2A
) : Fiche (
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
    photo
) {}

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
    photo: Array<String>?,
    remontageRoulement: Int?,
    collageRoulementPorteeArbre: Int?,
    collageRoulementFlasque: Int?,
    verificationFixationCouronne: Boolean?,
    verificationIsolementPorteBalais: Boolean?,
    isolementPorteBalaisV: Int?,
    isolementPorteBalaisOhm: Int?,
    tensionStatorInducteurs: Boolean?,
     tensionStatorInducteursU: Float?,
     tensionStatorInducteursV: Float?,
     tensionStatorInducteursW: Float?,
     intensiteStatorInducteurs: Boolean?,
     intensiteStatorInducteursU: Float?,
     intensiteStatorInducteursV: Float?,
     intensiteStatorInducteursW: Float?,
     tensionInduitRotor: Boolean?,
     tensionInduitRotorU: Float?,
     tensionInduitRotorV: Float?,
     tensionInduitRotorW: Float?,
     intensiteInduit: Boolean,
     intensiteInduitU: Float?,
     vitesseU: Float?,
     puissanceU: Float?,
     dureeEssai: Float?,
     sensRotation: Int?,
     vitesse1V: Float?,  // vitesse 1v
     acceleration1V: Float?,  //accélération 1v
     vitesse2V: Float?,  // vitesse 2v
     acceleration2V: Float?,  //accélération 2v
     vitesse1H: Float?,  // vitesse 1H
     acceleration1H: Float?,  //accélération 1H
     vitesse2H: Float?,  // vitesse 2H
     acceleration2H: Float?,  //accélération 2H
     vitesse2A: Float?,  // vitesse 2A
     acceleration2A: Float?,
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
    photo,
    1,
    remontageRoulement,
    collageRoulementPorteeArbre,
    collageRoulementFlasque,
    verificationFixationCouronne,
    verificationIsolementPorteBalais,
    isolementPorteBalaisV,
    isolementPorteBalaisOhm,
    tensionStatorInducteurs,
    tensionStatorInducteursU,
tensionStatorInducteursV,
tensionStatorInducteursW,
intensiteStatorInducteurs,
intensiteStatorInducteursU,
intensiteStatorInducteursV,
intensiteStatorInducteursW,
tensionInduitRotor,
tensionInduitRotorU,
tensionInduitRotorV,
tensionInduitRotorW,
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
    vitesse2A,  // vitesse 2A
    acceleration2A,  //accélération 2A
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
        remontageRoulement,
        collageRoulementPorteeArbre,
        collageRoulementFlasque,
        verificationFixationCouronne,
        verificationIsolementPorteBalais,
        isolementPorteBalaisV,
        isolementPorteBalaisOhm,
        tensionStatorInducteurs,
        tensionStatorInducteursU,
        tensionStatorInducteursV,
        tensionStatorInducteursW,
        intensiteStatorInducteurs,
        intensiteStatorInducteursU,
        intensiteStatorInducteursV,
        intensiteStatorInducteursW,
        tensionInduitRotor,
        tensionInduitRotorU,
        tensionInduitRotorV,
        tensionInduitRotorW,
        intensiteInduit,
        intensiteInduitU,
        vitesseU,
        puissanceU,
        dureeEssai,
        sensRotation,
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
         isolementPhaseRotorUW)

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
    photo: Array<String>?,
    remontageRoulement: Int?,
    collageRoulementPorteeArbre: Int?,
    collageRoulementFlasque: Int?,
    verificationFixationCouronne: Boolean?,
    verificationIsolementPorteBalais: Boolean?,
    isolementPorteBalaisV: Int?,
    isolementPorteBalaisOhm: Int?,
    // essais dynamiques
    tensionStatorInducteurs: Boolean?,
    tensionStatorInducteursU: Float?,
    tensionStatorInducteursV: Float?,
    tensionStatorInducteursW: Float?,
    intensiteStatorInducteurs: Boolean?,
    intensiteStatorInducteursU: Float?,
    intensiteStatorInducteursV: Float?,
    intensiteStatorInducteursW: Float?,
    tensionInduitRotor: Boolean?,
    tensionInduitRotorU: Float?,
    tensionInduitRotorV: Float?,
    tensionInduitRotorW: Float?,
    intensiteInduit: Boolean,
    intensiteInduitU: Float?,
    vitesseU: Float?,
    puissanceU: Float?,
    dureeEssai: Float?,
    sensRotation: Int?,
    //essais vibratoires
    vitesse1V: Float?,  // vitesse 1v
    acceleration1V: Float?,  //accélération 1v
    vitesse2V: Float?,  // vitesse 2v
    acceleration2V: Float?,  //accélération 2v
    vitesse1H: Float?,  // vitesse 1H
    acceleration1H: Float?,  //accélération 1H
    vitesse2H: Float?,  // vitesse 2H
    acceleration2H: Float?,  //accélération 2H
    vitesse2A: Float?,  // vitesse 2A
    acceleration2A: Float?,
    var resistanceInducteurs: Float?,
    var resistanceInduit: Float?,
    var isolementInducteursMasse: Float?,
    var isolementInduitMasse: Float?,
    var isolementInduitInducteurs: Float?,
    var releveIsoInducteursMasse: Float?,
    var releveIsoInduitMasse: Float?,
    var releveIsoInduitInducteurs: Float?
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
    photo,
    2,
    remontageRoulement,
    collageRoulementPorteeArbre,
    collageRoulementFlasque,
    verificationFixationCouronne,
    verificationIsolementPorteBalais,
    isolementPorteBalaisV,
    isolementPorteBalaisOhm,
    tensionStatorInducteurs,
    tensionStatorInducteursU,
    tensionStatorInducteursV,
    tensionStatorInducteursW,
    intensiteStatorInducteurs,
    intensiteStatorInducteursU,
    intensiteStatorInducteursV,
    intensiteStatorInducteursW,
    tensionInduitRotor,
    tensionInduitRotorU,
    tensionInduitRotorV,
    tensionInduitRotorW,
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
    vitesse2A,  // vitesse 2A
    acceleration2A,  //accélération 2A
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
        remontageRoulement,
        collageRoulementPorteeArbre,
        collageRoulementFlasque,
        verificationFixationCouronne,
        verificationIsolementPorteBalais,
        isolementPorteBalaisV,
        isolementPorteBalaisOhm,
        // essais dynamiques
        tensionStatorInducteurs,
        tensionStatorInducteursU,
        tensionStatorInducteursV,
        tensionStatorInducteursW,
        intensiteStatorInducteurs,
        intensiteStatorInducteursU,
        intensiteStatorInducteursV,
        intensiteStatorInducteursW,
        tensionInduitRotor,
        tensionInduitRotorU,
        tensionInduitRotorV,
        tensionInduitRotorW,
        intensiteInduit,
        intensiteInduitU,
        vitesseU,
        puissanceU,
        dureeEssai,
        sensRotation,
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
        resistanceInducteurs,
        resistanceInduit,
        isolementInducteursMasse,
        isolementInduitMasse,
        isolementInduitInducteurs,
        releveIsoInducteursMasse,
        releveIsoInduitMasse,
        releveIsoInduitInducteurs)

    }
}
//essais dyna et vibratoires à rajouter