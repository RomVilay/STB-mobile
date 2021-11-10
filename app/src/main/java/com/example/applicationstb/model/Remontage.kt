package com.example.applicationstb.model

import com.example.applicationstb.localdatabase.RemontageCCEntity
import com.example.applicationstb.localdatabase.RemontageEntity
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
    open var isolementPorteBalaisV: Float?,
    open var isolementPorteBalaisOhm: Float?,
    // essais dynamiques
    open var tensionStator:Boolean?,
    open var tensionStatorU:Float?,
    open var tensionStatorV:Float?,
    open var tensionStatorW:Float?,
    open var tensionInducteurs: Boolean?,
    open var tensionInducteursU: Float?,
    open var tensionInducteursV: Float?,
    open var tensionInducteursW: Float?,
    open var intensiteStator:Boolean?,
    open var intensiteStatorU:Float?,
    open var intensiteStatorV:Float?,
    open var intensiteStatorW:Float?,
    open var intensiteInducteurs: Boolean?,
    open var intensiteInducteursU: Float?,
    open var intensiteInducteursV: Float?,
    open var intensiteInducteursW: Float?,
    open var tensionInduit:Boolean?,
    open var tensionInduitU:Float?,
    open var tensionInduitV:Float?,
    open var tensionInduitW:Float?,
    open var tensionRotor: Boolean?,
    open var tensionRotorU: Float?,
    open var tensionRotorV: Float?,
    open var tensionRotorW: Float?,
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
) {
    fun toRemoEntity():RemontageEntity{
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
            photo,
            typeFicheRemontage,
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
    isolementPorteBalaisV: Float?,
    isolementPorteBalaisOhm: Float?,
    tensionStator:Boolean?,
    tensionStatorU:Float?,
    tensionStatorV:Float?,
    tensionStatorW:Float?,
    tensionInducteurs: Boolean?,
    tensionInducteursU: Float?,
    tensionInducteursV: Float?,
    tensionInducteursW: Float?,
    intensiteStator:Boolean?,
    intensiteStatorU:Float?,
    intensiteStatorV:Float?,
    intensiteStatorW:Float?,
    intensiteInducteurs: Boolean?,
    intensiteInducteursU: Float?,
    intensiteInducteursV: Float?,
    intensiteInducteursW: Float?,
    tensionInduit:Boolean?,
    tensionInduitU:Float?,
    tensionInduitV:Float?,
    tensionInduitW:Float?,
    tensionRotor: Boolean?,
    tensionRotorU: Float?,
    tensionRotorV: Float?,
    tensionRotorW: Float?,
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
        photo,
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
    isolementPorteBalaisV: Float?,
    isolementPorteBalaisOhm: Float?,
    // essais dynamiques
    tensionStator:Boolean?,
    tensionStatorU:Float?,
    tensionStatorV:Float?,
    tensionStatorW:Float?,
    tensionInducteurs: Boolean?,
    tensionInducteursU: Float?,
    tensionInducteursV: Float?,
    tensionInducteursW: Float?,
    intensiteStator:Boolean?,
    intensiteStatorU:Float?,
    intensiteStatorV:Float?,
    intensiteStatorW:Float?,
    intensiteInducteurs: Boolean?,
    intensiteInducteursU: Float?,
    intensiteInducteursV: Float?,
    intensiteInducteursW: Float?,
    tensionInduit:Boolean?,
    tensionInduitU:Float?,
    tensionInduitV:Float?,
    tensionInduitW:Float?,
    tensionRotor: Boolean?,
    tensionRotorU: Float?,
    tensionRotorV: Float?,
    tensionRotorW: Float?,
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
            photo,
        remontageRoulement,
        collageRoulementPorteeArbre,
        collageRoulementFlasque,
        verificationFixationCouronne,
        verificationIsolementPorteBalais,
        isolementPorteBalaisV,
        isolementPorteBalaisOhm,
        // essais dynamiques
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