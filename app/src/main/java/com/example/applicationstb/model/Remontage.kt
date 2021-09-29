package com.example.applicationstb.model

import java.sql.Timestamp
import java.util.*

abstract class Remontage(
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
    var remontageRoulement: Int?,
    var collageRoulementPorteeArbre: Int?,
    var collageRoulementPorteeFlasque: Int?,
    var verificationFixationCouronne: Boolean?,
    var verificationIsolementPorteBalais: Boolean?,
    var isolementPorteBalaisV: Int?,
    var isolementPorteBalaisOhm: Int?,
    // essais dynamiques
    var tensionStatorAVide: Boolean?,
    var tensionInducAVide: Boolean?,
    var intensiteStatorAVide: Boolean?,
    var intensiteInducAVide: Boolean?,
    var tensionInduitAVide: Boolean?,
    var tensionRotorVide: Boolean?,
    var tensionSIU: Float?,
    var tensionSIV: Float?,
    var tensionSIW: Float?,
    var intensiteStatorAVide: Float?,
    var intensiteInducteursAVide: Float?,
    var intensiteU: Float?,
    var intensiteV: Float?,
    var intensiteW: Float?,
    var tensionInduitAVideU: Float?,
    var tensionInduitAVideV: Float?,
    var tensionInduitAVideW: Float?,
    var tensionRotoOuvertU: Float?,
    var tensionRotoOuvertV: Float?,
    var tensionRotoOuvertW: Float?,
    var tensionIRU: Float?,
    var tensionIRV: Float?,
    var tensionIRW: Float?,
    var intensiteInduit: Boolean,
    var intensiteU: Float?,
    var vitesseAVide: Float?,
    var puissanceAVide: Float?,
    var dureeEssai: Float?,
    var sensRotation: Int?,

//essais vibratoires
    var V1V: Float?,  // vitesse 1v
    var A1V: Float?,  //accélération 1v
    var V2V: Float?,  // vitesse 2v
    var A2V: Float?,  //accélération 2v
    var V1H: Float?,  // vitesse 1H
    var A1H: Float?,  //accélération 1H
    var V2H: Float?,  // vitesse 2H
    var A2H: Float?,  //accélération 2H
    var V2A: Float?,  // vitesse 2A
    var A2A: Float?,  //accélération 2A
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
    collageRoulementPorteeFlasque: Int?,
    verificationFixationCouronne: Boolean?,
    verificationIsolementPorteBalais: Boolean?,
    isolementPorteBalaisV: Int?,
    isolementPorteBalaisOhm: Int?,
    // essais dynamiques
    tensionStatorAVide: Boolean?,
    tensionInducAVide: Boolean?,
    intensiteStatorAVideB: Boolean?,
    intensiteInducAVide: Boolean?,
    tensionInduitAVide: Boolean?,
    tensionRotorVide: Boolean?,
    tensionSIU: Float?,
    tensionSIV: Float?,
    tensionSIW: Float?,
    intensiteStatorAVide: Float?,
    intensiteInducteursAVide: Float?,
    intensiteU: Float?,
    intensiteV: Float?,
    intensiteW: Float?,
    tensionInduitAVideU: Float?,
    tensionInduitAVideV: Float?,
    tensionInduitAVideW: Float?,
    tensionRotoOuvertU: Float?,
    tensionRotoOuvertV: Float?,
    tensionRotoOuvertW: Float?,
    tensionIRU: Float?,
    tensionIRV: Float?,
    tensionIRW: Float?,
    intensiteInduit: Boolean,
    intensiteU: Float?,
    vitesseAVide: Float?,
    puissanceAVide: Float?,
    dureeEssai: Float?,
    sensRotation: Int?,
    //essais vibratoires
    V1V: Float?,  // vitesse 1v
    A1V: Float?,  //accélération 1v
    V2V: Float?,  // vitesse 2v
    A2V: Float?,  //accélération 2v
    V1H: Float?,  // vitesse 1H
    A1H: Float?,  //accélération 1H
    V2H: Float?,  // vitesse 2H
    A2H: Float?,  //accélération 2H
    V2A: Float?,  // vitesse 2A
    A2A: Float?,  //accélération 2A
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
    photoremontageRoulement,
    collageRoulementPorteeArbre,
    collageRoulementPorteeFlasque,
    verificationFixationCouronne,
    verificationIsolementPorteBalais,
    isolementPorteBalaisV,
    isolementPorteBalaisOhm,
    tensionStatorAVide,
    tensionInducAVide,
    intensiteStatorAVide,
    intensiteInducAVide,
    tensionInduitAVide,
    tensionRotorVide,
    tensionSIU,
    tensionSIV,
    tensionSIW,
    intensiteStatorAVide,
    intensiteInducteursAVide,
    intensiteU,
    intensiteV,
    intensiteW,
    tensionInduitAVideU,
    tensionInduitAVideV,
    tensionInduitAVideW,
    tensionRotoOuvertU,
    tensionRotoOuvertV,
    tensionRotoOuvertW,
    tensionIRU,
    tensionIRV,
    tensionIRW,
    intensiteInduit,
    intensiteU,
    vitesseAVide,
    puissanceAVide,
    dureeEssai,
    sensRotation,
//essais vibratoires
    V1V,  // vitesse 1v
    A1V,  //accélération 1v
    V2V,  // vitesse 2v
    A2V,  //accélération 2v
    V1H,  // vitesse 1H
    A1H,  //accélération 1H
    V2H,  // vitesse 2H
    A2H,  //accélération 2H
    V2A,  // vitesse 2A
    A2A,  //accélération 2A
) {}

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
    collageRoulementPorteeFlasque: Int?,
    verificationFixationCouronne: Boolean?,
    verificationIsolementPorteBalais: Boolean?,
    isolementPorteBalaisV: Int?,
    isolementPorteBalaisOhm: Int?,
    // essais dynamiques
    tensionStatorAVide: Boolean?,
    tensionInducAVide: Boolean?,
    intensiteStatorAVide: Boolean?,
    intensiteInducAVide: Boolean?,
    tensionInduitAVide: Boolean?,
    tensionRotorVide: Boolean?,
    tensionSIU: Float?,
    tensionSIV: Float?,
    tensionSIW: Float?,
    intensiteStatorAVide: Float?,
    intensiteInducteursAVide: Float?,
    intensiteU: Float?,
    intensiteV: Float?,
    intensiteW: Float?,
    tensionInduitAVideU: Float?,
    tensionInduitAVideV: Float?,
    tensionInduitAVideW: Float?,
    tensionRotoOuvertU: Float?,
    tensionRotoOuvertV: Float?,
    tensionRotoOuvertW: Float?,
    tensionIRU: Float?,
    tensionIRV: Float?,
    tensionIRW: Float?,
    intensiteInduit: Boolean,
    intensiteU: Float?,
    vitesseAVide: Float?,
    puissanceAVide: Float?,
    dureeEssai: Float?,
    sensRotation: Int?,
    //essais vibratoires
    V1V: Float?,  // vitesse 1v
    A1V: Float?,  //accélération 1v
    V2V: Float?,  // vitesse 2v
    A2V: Float?,  //accélération 2v
    V1H: Float?,  // vitesse 1H
    A1H: Float?,  //accélération 1H
    V2H: Float?,  // vitesse 2H
    A2H: Float?,  //accélération 2H
    V2A: Float?,  // vitesse 2A
    A2A: Float?,  //accélération 2A
    var resistanceInducteurs: Float?,
    var resistanceInduit: Float?,
    var isolementInducteursMasse: Float?,
    var isolementInduitMasse: Float?,
    var isolementInduitInducteurs: Float?,
    var releveIsoInducteursMasse: Float?,
    var releveIsoInduitMasse: Float?,
    var releveIsoInduitInducteurs: Float?,
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
    remontageRoulement,
    collageRoulementPorteeArbre,
collageRoulementPorteeFlasque,
verificationFixationCouronne,
verificationIsolementPorteBalais,
isolementPorteBalaisV,
isolementPorteBalaisOhm,
tensionStatorAVide,
tensionInducAVide,
intensiteStatorAVide,
intensiteInducAVide,
tensionInduitAVide,
tensionRotorVide,
tensionSIU,
tensionSIV,
tensionSIW,
intensiteStatorAVide,
intensiteInducteursAVide,
intensiteU,
intensiteV,
intensiteW,
tensionInduitAVideU,
tensionInduitAVideV,
tensionInduitAVideW,
tensionRotoOuvertU,
tensionRotoOuvertV,
tensionRotoOuvertW,
tensionIRU,
tensionIRV,
tensionIRW,
intensiteInduit,
intensiteU,
vitesseAVide,
puissanceAVide,
dureeEssai,
sensRotation,
//essais vibratoires
V1V,  // vitesse 1v
A1V,  //accélération 1v
V2V,  // vitesse 2v
A2V,  //accélération 2v
V1H,  // vitesse 1H
A1H,  //accélération 1H
V2H,  // vitesse 2H
A2H,  //accélération 2H
V2A,  // vitesse 2A
A2A,  //accélération 2A
) {

}
//essais dyna et vibratoires à rajouter