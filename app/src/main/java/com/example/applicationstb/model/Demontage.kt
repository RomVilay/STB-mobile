package com.example.applicationstb.model

import android.net.Uri
import android.util.Log
import com.example.applicationstb.localdatabase.DemoPompeEntity
import com.example.applicationstb.localdatabase.DemontageCCEntity
import com.example.applicationstb.localdatabase.DemontageTriphaseEntity
import kotlinx.serialization.json.Json
import java.util.*

enum class Etat2 {
    A_controler, Ok, a_rebague
}

enum class Etat3 {
    BonEtat, Casse, Absent, A_Changer, Sortie_par_cables
}

enum class Etat {
    propre, sale, tres_sale
}

enum class Rotation {
    gauche, droite
}

enum class TypePompe {
    entrainement_vis, ressort_coax_conique, ressort_coax_cyl, soufflet
}

enum class Matiere {
    ceramique, carbone_silicium, carbone, tugstène
}

open class DemontageMoteur(
    idFiche: String,
    numDevis: String?,
    numFiche: String?,
    type: Long?,
    statut: Long?,
    client: Client?,
    contact: String?,
    telContact: String?,
    techniciens: Array<User>?,
    resp: User?,
    dateDebut: Date?,
    dureeTotale: Long?,
    observation: String?,
    photo: Array<String>?,
    var typeFicheDemontage: Int?,
    open var marque: String?,
    open var numSerie: Int?,
    open var puissance: Float?,
    open var bride: Float?,
    open var vitesse : Float?,
    open var arbreSortantEntrant:Boolean?, //arbre sortant ou rentrant
    open var accouplement:Boolean?,
    open var coteAccouplement:String?,
    open var clavette: Boolean?,
    open var aspect:Int?,
    open var aspectInterieur:Int?,
    open var couplage:String?,
    open var flasqueAvant: Int?,
    open var flasqueArriere: Int?,
    open var porteeRAvant: Int?,
    open var porteeRArriere:  Int?,
    open var boutArbre: Boolean?,
    open var rondelleElastique: Boolean?,
    open var refRoulementAvant: String?,
    open var refRoulementArriere: String?,
    open var typeRoulementAvant: String ?,
    open var typeRoulementArriere: String ?,
    open var refJointAvant: String?,
    open var refJointArriere: String?,
    open var typeJointAvant: Boolean?,
    open var typeJointArriere: Boolean?,
    open var ventilateur: Int?,
    open var capotV: Int?,
    open var socleBoiteABorne : Int?,
    open var capotBoiteABorne : Int?,
    open var plaqueABorne : Int?,
    open var presenceSondes: Boolean ?,
    open var typeSondes: String?,
    open var equilibrage: Boolean?,
    open var peinture : String?,
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
) {
}

class DemontagePompe(
    idFiche: String,
    numDevis: String?,
    numFiche: String?,
    type: Long?,
    statut: Long?,
    client: Client?,
    contact: String?,
    telContact: String?,
    techniciens: Array<User>?,
    resp: User?,
    dateDebut: Date?,
    dureeTotale: Long?,
    observation: String?,
    photo: Array<String>?,
    marque: String?,
    numSerie: Int?,
    puissance: Float?,
    bride: Float?,
    vitesse: Float?,
    arbreSortantEntrant: Boolean?, //arbre sortant ou rentrant
    accouplement: Boolean?,
    coteAccouplement: String?,
    clavette: Boolean?,
    aspect: Int?,
    aspectInterieur: Int?,
    couplage: String?,
    flasqueAvant: Int?,
    flasqueArriere: Int?,
    porteeRAvant: Int?,
    porteeRArriere: Int?,
    boutArbre: Boolean?,
    rondelleElastique: Boolean?,
    refRoulementAvant: String?,
    refRoulementArriere: String?,
    typeRoulementAvant: String?,
    typeRoulementArriere: String?,
    refJointAvant: String?,
    refJointArriere: String?,
    typeJointAvant: Boolean?,
    typeJointArriere: Boolean?,
    ventilateur: Int?,
    capotV: Int?,
    socleBoiteABorne: Int?,
    capotBoiteABorne: Int?,
    plaqueABorne: Int?,
    presenceSondes: Boolean?,
    typeSondes: String?,
    equilibrage: Boolean?,
    peinture: String?,
    var fluide: String?,
    var sensRotation: Boolean?,
    var typeRessort: Int?,
    var typeJoint: String?,
    var matiere: Int?,
    var diametreArbre:Float?,
    var diametreExtPR:Float?,
    var diametreExtPF:Float?,
    var epaisseurPF:Float?,
    var longueurRotativeNonComprimee:Float?,
    var longueurRotativeComprimee:Float?,
    var longueurRotativeTravail:Float?
) : DemontageMoteur(
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
    marque,
    numSerie,
    puissance,
    bride,
    vitesse,
    arbreSortantEntrant, //arbre sortant ou rentrant
    accouplement,
    coteAccouplement,
    clavette,
    aspect,
    aspectInterieur,
    couplage,
    flasqueAvant,
    flasqueArriere,
    porteeRAvant,
    porteeRArriere,
    boutArbre,
    rondelleElastique,
    refRoulementAvant,
    refRoulementArriere,
    typeRoulementAvant,
    typeRoulementArriere,
    refJointAvant,
    refJointArriere,
    typeJointAvant,
    typeJointArriere,
    ventilateur,
    capotV,
    socleBoiteABorne,
    capotBoiteABorne,
    plaqueABorne,
    presenceSondes,
    typeSondes,
    equilibrage,
    peinture
)
    {
    fun toEntity(): DemoPompeEntity{
        return DemoPompeEntity(
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
            1,
            marque,
            numSerie,
            fluide,
            sensRotation,
            typeRessort,
            typeJoint,
            matiere,
            diametreArbre,
            diametreExtPR,
            diametreExtPF,
            epaisseurPF,
            longueurRotativeNonComprimee,
            longueurRotativeComprimee,
            longueurRotativeTravail

        )
    }
}

/*class Monophase(
    idFiche: String,
    numDevis: String,
    numFiche: String,
    statut: Long,
    type: Long,
    client: Client,
    contact: String?,
    telContact: String?,
    techniciens: Array<User>?,
    resp: User?,
    dateDebut: Date?,
    dureeTotale: Long?,
    observation: String?,
    photo: Array<String>?,
    typeFicheDemontage: Int,
    marque: String?,
    numSerie: Int?,
    puissance: Float?,
    bride: Float?,
    vitesse: Float?,
    arbreSortantEntrant: Boolean?, //arbre sortant ou rentrant
    accouplement: Boolean?,
    coteAccouplement: String?,
    clavette: Boolean?,
    aspect: Int?,
    aspectInterieur: Int?,
    couplage: String?,
    flasqueAvant: Int?,
    flasqueArriere: Int?,
    porteeRAvant: Int?,
    porteeRArriere: Int?,
    boutArbre: Boolean?,
    rondelleElastique: Boolean?,
    refRoulementAvant: String?,
    refRoulementArriere: String?,
    typeRoulementAvant: String?,
    typeRoulementArriere: String?,
    refJointAvant: String?,
    refJointArriere: String?,
    typeJointAvant: Boolean?,
    typeJointArriere: Boolean?,
    ventilateur: Int?,
    capotV: Int?,
    socleBoiteABorne: Int?,
    capotBoiteABorne: Int?,
    plaqueABorne: Int?,
    presenceSondes: Boolean?,
    typeSondes: String?,
    equilibrage: Boolean?,
    peinture: String?,
) : DemontageMoteur(
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
    typeFicheDemontage,
    marque,
    numSerie,
    puissance,
    bride,
    vitesse,
    arbreSortantEntrant, //arbre sortant ou rentrant
    accouplement,
    coteAccouplement,
    clavette,
    aspect,
    aspectInterieur,
    couplage,
    flasqueAvant,
    flasqueArriere,
    porteeRAvant,
    porteeRArriere,
    boutArbre,
    rondelleElastique,
    refRoulementAvant,
    refRoulementArriere,
    typeRoulementAvant,
    typeRoulementArriere,
    refJointAvant,
    refJointArriere,
    typeJointAvant,
    typeJointArriere,
    ventilateur,
    capotV,
    socleBoiteABorne,
    capotBoiteABorne,
    plaqueABorne,
    presenceSondes,
    typeSondes,
    equilibrage,
    peinture
) {
    var isoPM: Int? = null; // isolement phase/masse
    var rt: Int? = null;    // resistance travail
    var rd: Int? = null;    //resistance démarrage
    var cnds: Int? = null; //valeur condenseur
    var tension: Int? = null;
    var Intensité: Int? = null;
}*/

class Triphase(
    idFiche: String,
    numDevis: String?,
    numFiche: String?,
    type: Long?,
    statut: Long?,
    client: Client?,
    contact: String?,
    telContact: String?,
    techniciens: Array<User>?,
    resp: User?,
    dateDebut: Date?,
    dureeTotale: Long?,
    observation: String?,
    photo: Array<String>?,
    marque: String?,
    numSerie: Int?,
    puissance: Float?,
    bride: Float?,
    vitesse: Float?,
    arbreSortantEntrant: Boolean?, //arbre sortant ou rentrant
    accouplement: Boolean?,
    coteAccouplement: String?,
    clavette: Boolean?,
    aspect: Int?,
    aspectInterieur: Int?,
    couplage: String?,
    flasqueAvant: Int?,
    flasqueArriere: Int?,
    porteeRAvant: Int?,
    porteeRArriere: Int?,
    boutArbre: Boolean?,
    rondelleElastique: Boolean?,
    refRoulementAvant: String?,
    refRoulementArriere: String?,
    typeRoulementAvant: String?,
    typeRoulementArriere: String?,
    refJointAvant: String?,
    refJointArriere: String?,
    typeJointAvant: Boolean?,
    typeJointArriere: Boolean?,
    ventilateur: Int?,
    capotV: Int?,
    socleBoiteABorne: Int?,
    capotBoiteABorne: Int?,
    plaqueABorne: Int?,
    presenceSondes: Boolean?,
    typeSondes: String?,
    equilibrage: Boolean?,
    peinture: String?,
    var isolementPhaseMasseStatorUM: Int?,
    var isolementPhaseMasseStatorVM: Int?,
    var isolementPhaseMasseStatorWM: Int?,
    var isolementPhasePhaseStatorUV: Int?,
    var isolementPhasePhaseStatorVW: Int?,
    var isolementPhasePhaseStatorUW: Int?,
    var resistanceStatorU: Int?,
    var resistanceStatorV: Int?,
    var resistanceStatorW: Int?,
    var tensionU: Int?,
    var tensionV: Int?,
    var tensionW: Int?,
    var intensiteU: Int?,
    var intensiteV: Int?,
    var intensiteW: Int?,
    var dureeEssai: Int?,
) : DemontageMoteur(
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
    6,
    marque,
    numSerie,
    puissance,
    bride,
    vitesse,
    arbreSortantEntrant, //arbre sortant ou rentrant
    accouplement,
    coteAccouplement,
    clavette,
    aspect,
    aspectInterieur,
    couplage,
    flasqueAvant,
    flasqueArriere,
    porteeRAvant,
    porteeRArriere,
    boutArbre,
    rondelleElastique,
    refRoulementAvant,
    refRoulementArriere,
    typeRoulementAvant,
    typeRoulementArriere,
    refJointAvant,
    refJointArriere,
    typeJointAvant,
    typeJointArriere,
    ventilateur,
    capotV,
    socleBoiteABorne,
    capotBoiteABorne,
    plaqueABorne,
    presenceSondes,
    typeSondes,
    equilibrage,
    peinture
) {
    override fun toString(): String {
        return "${_id} - ${numDevis} + - ${
            numFiche}  - ${
            status}  - ${
            client!!._id}  - ${
            contact}  - ${
            telContact}  - ${
            dateDebut}  - ${
            dureeTotale}  - ${
            observations}  - ${
            6}  - ${
            marque}  - ${
            numSerie}  - ${
            puissance}  - ${
            bride}  - ${
            vitesse}  - ${
            arbreSortantEntrant}  - ${
            accouplement}  - ${
            coteAccouplement}  - ${
            clavette}  - ${
            aspect}  - ${
            aspectInterieur}  - ${
            couplage}  - ${
            flasqueAvant}  - ${
            flasqueArriere}  - ${
            porteeRArriere}  - ${
            porteeRAvant}  - ${
            boutArbre}  - ${
            rondelleElastique}  - ${
            refRoulementAvant}  - ${
            refRoulementArriere}  - ${
            typeRoulementAvant}  - ${
            typeRoulementArriere}  - ${
            refJointAvant}  - ${
            refJointArriere}  - ${
            typeJointAvant}  - ${
            typeJointArriere}  - ${
            ventilateur}  - ${
            capotV}  - ${
            socleBoiteABorne}  - ${
            capotBoiteABorne}  - ${
            plaqueABorne}  - ${
            presenceSondes}  - ${
            typeSondes}  - ${
            equilibrage}  - ${
            peinture}  - ${
            isolementPhaseMasseStatorUM}  - ${
            isolementPhaseMasseStatorVM}  - ${
            isolementPhaseMasseStatorWM}  - ${
            isolementPhasePhaseStatorUV}  - ${
            isolementPhasePhaseStatorVW}  - ${
            isolementPhasePhaseStatorUW}  - ${
            resistanceStatorU}  - ${
            resistanceStatorV}  - ${
            resistanceStatorW}  - ${
            tensionU}  - ${
            tensionV}  - ${
            tensionW}  - ${
            intensiteU}  - ${
            intensiteV}  - ${
            intensiteW}  - ${
            dureeEssai}"
    }
    fun toEntity() : DemontageTriphaseEntity{
        return DemontageTriphaseEntity(
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
            6,
            marque,
            numSerie,
            puissance,
            bride,
            vitesse,
            arbreSortantEntrant,
            accouplement,
            coteAccouplement,
            clavette,
            aspect,
            aspectInterieur,
            couplage,
            flasqueAvant,
            flasqueArriere,
            porteeRArriere,
            porteeRAvant,
            boutArbre,
            rondelleElastique,
            refRoulementAvant,
            refRoulementArriere,
            typeRoulementAvant,
            typeRoulementArriere,
            refJointAvant,
            refJointArriere,
            typeJointAvant,
            typeJointArriere,
            ventilateur,
            capotV,
            socleBoiteABorne,
            capotBoiteABorne,
            plaqueABorne,
            presenceSondes,
            typeSondes,
            equilibrage,
            peinture,
            isolementPhaseMasseStatorUM,
            isolementPhaseMasseStatorVM,
            isolementPhaseMasseStatorWM,
            isolementPhasePhaseStatorUV,
            isolementPhasePhaseStatorVW,
            isolementPhasePhaseStatorUW,
            resistanceStatorU,
            resistanceStatorV,
            resistanceStatorW,
            tensionU,
            tensionV,
            tensionW,
            intensiteU,
            intensiteV,
            intensiteW,
            dureeEssai
        )
    }
}

/*class RotorBobine(
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
    typeFicheDemontage: Int,
    marque: String?,
    numSerie: Int?,
    puissance: Float?,
    bride: Float?,
    vitesse: Float?,
    arbreSortantEntrant: Boolean?, //arbre sortant ou rentrant
    accouplement: Boolean?,
    coteAccouplement: String?,
    clavette: Boolean?,
    aspect: Int?,
    aspectInterieur: Int?,
    couplage: String?,
    flasqueAvant: Int?,
    flasqueArriere: Int?,
    porteeRAvant: Int?,
    porteeRArriere: Int?,
    boutArbre: Boolean?,
    rondelleElastique: Boolean?,
    refRoulementAvant: String?,
    refRoulementArriere: String?,
    typeRoulementAvant: String?,
    typeRoulementArriere: String?,
    refJointAvant: String?,
    refJointArriere: String?,
    typeJointAvant: Boolean?,
    typeJointArriere: Boolean?,
    ventilateur: Int?,
    capotV: Int?,
    socleBoiteABorne: Int?,
    capotBoiteABorne: Int?,
    plaqueABorne: Int?,
    presenceSondes: Boolean?,
    typeSondes: String?,
    equilibrage: Boolean?,
    peinture: String?,
) : DemontageMoteur(
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
    typeFicheDemontage,
    marque,
    numSerie,
    puissance,
    bride,
    vitesse,
    arbreSortantEntrant, //arbre sortant ou rentrant
    accouplement,
    coteAccouplement,
    clavette,
    aspect,
    aspectInterieur,
    couplage,
    flasqueAvant,
    flasqueArriere,
    porteeRAvant,
    porteeRArriere,
    boutArbre,
    rondelleElastique,
    refRoulementAvant,
    refRoulementArriere,
    typeRoulementAvant,
    typeRoulementArriere,
    refJointAvant,
    refJointArriere,
    typeJointAvant,
    typeJointArriere,
    ventilateur,
    capotV,
    socleBoiteABorne,
    capotBoiteABorne,
    plaqueABorne,
    presenceSondes,
    typeSondes,
    equilibrage,
    peinture
) {
    //partie statique
    var isoPMS: Array<Int>? = null; //iso phase/masse stator UT VT WT
    var isoPMR: Array<Int>? = null;  //iso phase/masse rotor B1T B2T B3T
    var isoPB: Int? = null;         //iso porte balais
    var isoPPS: Array<Int>? = null; //iso phase/phase stator
    var resS: Array<Int>? = null;   //resistance stator UVW
    var resR: Array<Int>? = null;    //resistance rotor B1/B2 B2/B2 B1/B3

    //partie dynamique
    var tension: Array<Int>? = null; //tension UVW
    var intensité: Array<Int>? = null; //intensité UVW rotor
    var dureeEssai: Int? = null;
}*/

class CourantContinu(
    idFiche: String,
    numDevis: String?,
    numFiche: String?,
    type: Long?,
    statut: Long?,
    client: Client?,
    contact: String?,
    telContact: String?,
    techniciens: Array<User>?,
    resp: User?,
    dateDebut: Date?,
    dureeTotale: Long?,
    observation: String?,
    photo: Array<String>?,
    marque: String?,
    numSerie: Int?,
    puissance: Float?,
    bride: Float?,
    vitesse: Float?,
    arbreSortantEntrant: Boolean?, //arbre sortant ou rentrant
    accouplement: Boolean?,
    coteAccouplement: String?,
    clavette: Boolean?,
    aspect: Int?,
    aspectInterieur: Int?,
    couplage: String?,
    flasqueAvant: Int?,
    flasqueArriere: Int?,
    porteeRAvant: Int?,
    porteeRArriere: Int?,
    boutArbre: Boolean?,
    rondelleElastique: Boolean?,
    refRoulementAvant: String?,
    refRoulementArriere: String?,
    typeRoulementAvant: String?,
    typeRoulementArriere: String?,
    refJointAvant: String?,
    refJointArriere: String?,
    typeJointAvant: Boolean?,
    typeJointArriere: Boolean?,
    ventilateur: Int?,
    capotV: Int?,
    socleBoiteABorne: Int?,
    capotBoiteABorne: Int?,
    plaqueABorne: Int?,
    presenceSondes: Boolean?,
    typeSondes: String?,
    equilibrage: Boolean?,
    peinture: String?,
    var isolationMasseInduit: Int?,
    var isolationMassePolesPrincipaux: Int?,
    var isolationMassePolesAuxilliaires: Int?,
    var isolationMassePolesCompensatoires: Int?,
    var isolationMassePorteBalais: Int?,
    var resistanceInduit: Int?,
    var resistancePP: Int?,
    var resistancePA: Int?,
    var resistancePC: Int?,
    /* essais dynamiques */
    var tensionInduit: Int?,
    var intensiteInduit: Int?,
    var tensionExcitation: Int?,
    var intensiteExcitation: Int?,
    ) : DemontageMoteur(
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
    5,
    marque,
    numSerie,
    puissance,
    bride,
    vitesse,
    arbreSortantEntrant, //arbre sortant ou rentrant
    accouplement,
    coteAccouplement,
    clavette,
    aspect,
    aspectInterieur,
    couplage,
    flasqueAvant,
    flasqueArriere,
    porteeRAvant,
    porteeRArriere,
    boutArbre,
    rondelleElastique,
    refRoulementAvant,
    refRoulementArriere,
    typeRoulementAvant,
    typeRoulementArriere,
    refJointAvant,
    refJointArriere,
    typeJointAvant,
    typeJointArriere,
    ventilateur,
    capotV,
    socleBoiteABorne,
    capotBoiteABorne,
    plaqueABorne,
    presenceSondes,
    typeSondes,
    equilibrage,
    peinture
) {
        fun toEntity(): DemontageCCEntity{
            return DemontageCCEntity (
                _id,
                numDevis,
                numFiche,
                2,
                client!!._id,
                contact,
                telContact,
                dateDebut,
                dureeTotale,
                observations,
                5,
                marque,
                numSerie,
                puissance,
                bride,
                vitesse,
                arbreSortantEntrant,
                accouplement,
                coteAccouplement,
                clavette,
                aspect,
                aspectInterieur,
                couplage,
                flasqueAvant,
                flasqueArriere,
                porteeRArriere,
                porteeRAvant,
                boutArbre,
                rondelleElastique,
                refRoulementAvant,
                refRoulementArriere,
                typeRoulementAvant,
                typeRoulementArriere,
                refJointAvant,
                refJointArriere,
                typeJointAvant,
                typeJointArriere,
                ventilateur,
                capotV,
                socleBoiteABorne,
                capotBoiteABorne,
                plaqueABorne,
                presenceSondes,
                typeSondes,
                equilibrage,
                peinture,
                isolationMasseInduit,
                isolationMassePolesPrincipaux,
                isolationMassePolesAuxilliaires,
                isolationMassePolesCompensatoires,
                isolationMassePorteBalais,
                resistanceInduit,
                resistancePP,
                resistancePA,
                resistancePC,
                /* essais dynamiques */
                tensionInduit,
                intensiteInduit,
                tensionExcitation,
                intensiteExcitation
            )
        }
    }

/*class Alternateur(
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
    typeFicheDemontage: Int,
    marque: String?,
    numSerie: Int?,
    puissance: Float?,
    bride: Float?,
    vitesse: Float?,
    arbreSortantEntrant: Boolean?, //arbre sortant ou rentrant
    accouplement: Boolean?,
    coteAccouplement: String?,
    clavette: Boolean?,
    aspect: Int?,
    aspectInterieur: Int?,
    couplage: String?,
    flasqueAvant: Int?,
    flasqueArriere: Int?,
    porteeRAvant: Int?,
    porteeRArriere: Int?,
    boutArbre: Boolean?,
    rondelleElastique: Boolean?,
    refRoulementAvant: String?,
    refRoulementArriere: String?,
    typeRoulementAvant: String?,
    typeRoulementArriere: String?,
    refJointAvant: String?,
    refJointArriere: String?,
    typeJointAvant: Boolean?,
    typeJointArriere: Boolean?,
    ventilateur: Int?,
    capotV: Int?,
    socleBoiteABorne: Int?,
    capotBoiteABorne: Int?,
    plaqueABorne: Int?,
    presenceSondes: Boolean?,
    typeSondes: String?,
    equilibrage: Boolean?,
    peinture: String?,
) : DemontageMoteur(
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
    typeFicheDemontage,
    marque,
    numSerie,
    puissance,
    bride,
    vitesse,
    arbreSortantEntrant, //arbre sortant ou rentrant
    accouplement,
    coteAccouplement,
    clavette,
    aspect,
    aspectInterieur,
    couplage,
    flasqueAvant,
    flasqueArriere,
    porteeRAvant,
    porteeRArriere,
    boutArbre,
    rondelleElastique,
    refRoulementAvant,
    refRoulementArriere,
    typeRoulementAvant,
    typeRoulementArriere,
    refJointAvant,
    refJointArriere,
    typeJointAvant,
    typeJointArriere,
    ventilateur,
    capotV,
    socleBoiteABorne,
    capotBoiteABorne,
    plaqueABorne,
    presenceSondes,
    typeSondes,
    equilibrage,
    peinture
) {
    var imsp: Array<Int>? = null; //iso masse stator
    var imrp: Array<Int>? = null; //iso masse rotor
    var imse: Int? = null; //iso masse stator exitation
    var imre: Int? = null; //iso masse rotor exitation
    var rsp: Array<Int>? = null; // res stator principal
    var rrp: Int? = null; // res rotor principal
    var rse: Int? = null; // res stator extitation
    var rre: Int? = null; // res rotor exitation
    var ipps: Array<Int>? = null; //iso phase phase stator
    var testDiode: Boolean? = null;

    //essais dynamiques
    var tensions: Array<Int>? = null; //tensions U,V,W
    var intensite: Array<Int>? = null; //intensité U,V,V
    var tensionE: Array<Int>? = null; //tension exitation U,V,W
    var intensiteE: Array<Int>? = null; // intensité exitation U,V,W
}*/