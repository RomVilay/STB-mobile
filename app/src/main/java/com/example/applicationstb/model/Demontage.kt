package com.example.applicationstb.model

import android.net.Uri
import java.util.*

enum class Etat2 {
    A_controler, Ok, a_rebague
}
enum class Etat3 {
    BonEtat, Casse, Absent, A_Changer, Sortie_par_cables
}
enum class Etat{
    propre,sale,tres_sale
}
enum class Rotation {
    gauche,droite
}
enum class TypePompe{
    entrainement_vis,ressort_coax_conique,ressort_coax_cyl,soufflet
}
enum class Matiere{
    ceramique,carbone_silicium,carbone,tugstène
}
abstract class DemontageMoteur(
        numDevis: String,
        numChantier: String,
        client: Client,
        contact: String,
        telContact: Long,
        techniciens: Array<User>,
        resp: User,
): Fiche(numDevis, numChantier, client, contact, telContact, techniciens, resp) {
    /**
     * infos moteur
     */
    var marque: String? = null;
    var date: Date? = null;
    var numSerie: String ? = null;
    var type: String ? = null;
    var puissance: Int ? = null;
    var bride: Int ? = null;
    var vitesse : Int ? = null;
    /**
     * etat moteur
     */
    var arbre:Boolean? = null;
    var accouplement:Boolean? = null;
    var aspect:Etat? = null;
    var aspectI:Etat? = null;
    /**
     * partie mecanique
     */
    var couplage:String? = null;
    var flasqueAvant: String ? = null;
    var flasqueArriere: String ? = null;
    var porteerar: String ? = null;
    var porteeravt: String ? = null;
    var boutarbre: Boolean? = null;
    var rondelleE: Boolean? = null;
    var refRoulementAv: String? = null;
    var typeRoulementAv: String ? = null;
    var refRoulementAr: String? = null;
    var typeRoulementAr: String ? = null;
    var refJointAvant: String ? = null;
    var typeJointAvant: String ? = null;
    var refJointAr: String ? = null;
    var typeJointAr: String ? = null;
    var capotV: String ? = null;
    var ventilateur: String ? = null;
    var socleBoiteABorne : String ? = null;
    var capotBoiteABorne : String ? = null;
    var plaqueABorne : String ? = null;
    var pesenceBorne: Boolean ? = null;
    var equilibrage: Boolean ? = null;
    var peinture : Boolean ? = null;
    var photos : Array<Uri> ? = null
    var observations : String ? = null;

    override fun toString(): String {
        var s = "couplage: "+couplage+" - flasqueAvant: "+flasqueAvant+" - flasqueArriere: "+flasqueArriere+" - porteerar: "+
                porteerar+" - porteeravt: "+porteeravt+" - refRoulementAv: "+refRoulementAv+" - typeRoulementAv: "+typeRoulementAv+
                " - refRoulementAr: "+refRoulementAr+" - typeRoulementAr: "+typeRoulementAr+" - refJointAvant: "+refJointAvant+
                " - typeJointAvant: "+typeJointAvant+" - refJointAr: "+refJointAr+" - typeJointAr: "+typeJointAr+" - etat bout d'arbre: "+boutarbre+" - rondelle elastique: "+rondelleE+" "
        return s
    }
}

class DemontagePompe( numDevis: String,
                      numChantier: String,
                      client: Client,
                      contact: String,
                      telContact: Long,
                      techniciens: Array<User>,
                      resp: User,
): Fiche(numDevis, numChantier, client, contact, telContact, techniciens, resp) {
    var numSerie: String ? = null;
    var fluide: String ? = null;
    var sensRotation: Rotation ? = null;
    var type: TypePompe ? = null;
    var typeJoint: String ? = null;
    var matiere: Matiere ? = null;
    var diametreA: Int ? = null;
    var diametreExtPR:Int ? = null;
    var longueurNC:Int ? = null;
    var longueurC:Int ? = null;
    var longueurPRT:Int ? = null;
    var diametreEPF:Int ? = null;
    var epaisseurPF : Int ? = null;
}
class Monophase(
        numDevis: String,
        numChantier: String,
        client: Client,
        contact: String,
        telContact: Long,
        techniciens: Array<User>,
        resp: User,
):DemontageMoteur(numDevis, numChantier, client, contact, telContact, techniciens, resp) {
    var isoPM: Int ? = null; // isolement phase/masse
    var rt: Int ? = null;    // resistance travail
    var rd: Int ? = null;    //resistance démarrage
    var cnds : Int ? = null; //valeur condenseur
    var tension: Int ? = null;
    var Intensité : Int ? = null;
}

class Triphase(
        numDevis: String,
        numChantier: String,
        client: Client,
        contact: String,
        telContact: Long,
        techniciens: Array<User>,
        resp: User,
):DemontageMoteur(numDevis, numChantier, client, contact, telContact, techniciens, resp) {
    var isoPM: Array<Int> ? = null; //iso phase/masse UM/VM/WM
    var isoPP: Array<Int> ? = null; //iso phase/phase UV/VW/UW
    var resistanceStator: Array<Int> ? = null; // resistance stator UVW
    var tensions: Array<Int> ? = null; // tensions UVW
    var intensité: Array<Int> ? = null; // intensité UVW
    var dureeEssai: Int ? = null;
}

class RotorBobine (numDevis: String,
numChantier: String,
client: Client,
contact: String,
telContact: Long,
techniciens: Array<User>,
resp: User,
):DemontageMoteur(numDevis, numChantier, client, contact, telContact, techniciens, resp) {
    //partie statique
    var isoPMS : Array<Int> ? = null; //iso phase/masse stator UT VT WT
    var isoPMR: Array<Int> ? = null;  //iso phase/masse rotor B1T B2T B3T
    var isoPB : Int ? = null;         //iso porte balais
    var isoPPS : Array<Int> ? = null; //iso phase/phase stator
    var resS : Array<Int> ? = null;   //resistance stator UVW
    var resR: Array<Int> ? = null;    //resistance rotor B1/B2 B2/B2 B1/B3
    //partie dynamique
    var tension: Array<Int> ? = null; //tension UVW
    var intensité:Array<Int> ? = null; //intensité UVW rotor
    var dureeEssai:Int ? = null;
}
class CourantContinu (numDevis: String,
                   numChantier: String,
                   client: Client,
                   contact: String,
                   telContact: Long,
                   techniciens: Array<User>,
                   resp: User,
):DemontageMoteur(numDevis, numChantier, client, contact, telContact, techniciens, resp) {
    var isoMass :Array<Int> ? = null; // iso masse induit/poles principaux / poles auxilliaires / poles compensatoires / porte balais
    var resistances : Array<Int> ? = null; // resistance induit, poles principaux, poles aux, poles complémentaires
    var ti : Int ? = null;  //tension/induit
    var ii: Int ? = null;   // intensité induit
    var te : Int ? = null;  //tension exitaiton
    var ie : Int ? = null;  //intensité exitation
}

class Alternateur (numDevis: String,
                   numChantier: String,
                   client: Client,
                   contact: String,
                   telContact: Long,
                   techniciens: Array<User>,
                   resp: User,
):DemontageMoteur(numDevis, numChantier, client, contact, telContact, techniciens, resp) {
    var imsp :Array<Int> ? = null; //iso masse stator
    var imrp :Array<Int> ? = null; //iso masse rotor
    var imse : Int ? = null; //iso masse stator exitation
    var imre : Int ? = null; //iso masse rotor exitation
    var rsp : Array<Int> ? = null; // res stator principal
    var rrp : Int ? = null; // res rotor principal
    var rse : Int ? = null; // res stator extitation
    var rre: Int ?  = null; // res rotor exitation
    var ipps: Array<Int> ? = null; //iso phase phase stator
    var testDiode: Boolean ? = null;
    //essais dynamiques
    var tensions : Array<Int> ? = null; //tensions U,V,W
    var intensite : Array<Int> ? = null; //intensité U,V,V
    var tensionE : Array<Int> ? = null; //tension exitation U,V,W
    var intensiteE : Array<Int> ? = null; // intensité exitation U,V,W
}