package com.example.applicationstb.model

import java.sql.Timestamp
import java.util.*

abstract class Remontage (
    idFiche:String,
    numDevis: String,
    numFiche: String,
    type:Long,
    statut: Long,
    client: Client,
    contact: String?,
    telContact: String?,
    techniciens: Array<User>?,
    resp: User?,
    dateDebut: Date?,
    dureeTotale:Long?,
    observation: String?,
    photo:Array<String>?
        ) :Fiche(idFiche, numDevis, numFiche, type, statut, client, contact, telContact, techniciens, resp, dateDebut, dureeTotale, observation, photo){
            var remontageRoulement: Int? = null;
            var collageRoulementPorteeArbre : Int? = null;
            var collageRoulementPorteeFlasque : Int? = null;
            var verificationFixationCouronne : Boolean? = null;
            var verificationIsolementPorteBalais : Boolean? = null;
            var isolementPorteBalaisV : Int? = null;
            var isolementPorteBalaisOhm : Int? = null;
            // essais dynamiques
            var tensionStatorAVide: Boolean? = null;
            var tensionInducAVide: Boolean? = null;
            var intensiteStatorAVide: Boolean? = null;
            var intensiteInducAVide: Boolean? = null;
            var tensionInduitAVide: Boolean? = null;
            var tensionRotorVide: Boolean? = null;
            var TSV : Int ? = null;         //tension stator à vide
            var TInductV : Int? = null;          //tension inducteur à vide
            var ISV : Int? = null;          // intensité stator à vide
            var IIV : Int? = null;          //intensité inducteur à vide
            var TInduitV : Int? = null;          //tension induit à vide
            var TRO : Int? = null;          //tension rotor ouvert
            var II : Int? = null;           //Intensité Induit
            var VV :Int? = null;            //vitesse à vide
            var PV : Int? = null;           //Puissance à vide
            var Duree : Timestamp? = null;        //Duree de l'essai
            var SR : Boolean? = null;       //sens de rotation
            //essais vibratoires
            var V1V : Int? = null;  // vitesse 1v
            var A1V : Int? = null;  //accélération 1v
            var V2V : Int? = null;  // vitesse 2v
            var A2V : Int? = null;  //accélération 2v
            var V1H : Int? = null;  // vitesse 1H
            var A1H : Int? = null;  //accélération 1H
            var V2H : Int? = null;  // vitesse 2H
            var A2H : Int? = null;  //accélération 2H
            var V2A : Int? = null;  // vitesse 2A
            var A2A : Int? = null;  //accélération 2A

}
class RemontageTriphase (
    idFiche:String,
    numDevis: String,
    numFiche: String,
    type:Long,
    statut: Long,
    client: Client,
    contact: String?,
    telContact: String?,
    techniciens: Array<User>?,
    resp: User?,
    dateDebut: Date?,
    dureeTotale:Long?,
    observation: String?,
    photo:Array<String>?) : Remontage(idFiche, numDevis, numFiche, type, statut, client, contact, telContact, techniciens, resp, dateDebut, dureeTotale, observation, photo) {
        var isoPM : Int? = null;
        var isoP : Int? = null;
        var resStatorU : Int? = null;
        var resStatorV : Int? = null;
        var resStatorW : Int? = null;
        var isoPMSU : Int? = null;
        var isoPMSV : Int? = null;
        var isoPMSW : Int? = null;
        var isoPMRU : Int? = null;
        var isoPMRV : Int? = null;
        var isoPMRW : Int? = null;
        var isoPSUV : Int? = null;
        var isoPSVW : Int? = null;
        var isoPSUW : Int? = null;
        var isoPRUV : Int? = null;
        var isoPRVW : Int? = null;
        var isoPRUW : Int? = null;
        var ResU : Int? = null;
        var ResV : Int? = null;
        var ResW : Int? = null;
                 }
class RemontageCourantC (
    idFiche:String,
    numDevis: String,
    numFiche: String,
    type:Long,
    statut: Long,
    client: Client,
    contact: String?,
    telContact: String?,
    techniciens: Array<User>?,
    resp: User?,
    dateDebut: Date?,
    dureeTotale:Long?,
    observation: String?,
    photo:Array<String>?) : Remontage(idFiche, numDevis, numFiche, type, statut, client, contact, telContact, techniciens, resp, dateDebut, dureeTotale, observation, photo) {
        var resInducst :Int? = null;
        var resIndui : Int? = null;
        var isoInducMasse : Int? = null;
        var isoInduitMasse : Int? = null;
        var isoInduitInduc : Int? = null;
        var relIsoInduMasse: Int? = null;
        var relIsoInduitMasse : Int? = null;
        var relIsoInduitInduct: Int ? = null;

    }
//essais dyna et vibratoires à rajouter