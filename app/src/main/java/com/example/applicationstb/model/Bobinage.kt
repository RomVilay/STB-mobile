package com.example.applicationstb.model

import android.media.Image
import android.net.Uri
import android.os.Build
import android.os.Parcelable
import androidx.annotation.RequiresApi
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime
import java.util.*
@Parcelize
class Section (var nbBrins:Long, var longueur: Double, var diametre: Double): Parcelable{
}

class Bobinage(idFiche:String,
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
               photo:Array<String>?,
               var marqueMoteur:String,
               var typeBobinage:String,
               var puissance:Long,
               var vitesse: Long,
               var phases:Long,
               var tension: Long,
               var frequences:Long,
               var courant:Long,
               var calageEncoches:Boolean,
               var sectionsFils : MutableList<Section>,
               var nbSpires : Long,
               var resistanceU : Long,
               var resistanceV : Long,
               var resistanceW : Long,
               var tensionUT : Long,
               var tensionVT : Long,
               var tensionWT : Long,
               var tensionUV : Long,
               var tensionUW : Long,
               var tensionVW : Long,
               var schemas : MutableList<String>
                ) : Fiche(idFiche, numDevis, numFiche, type, statut, client, contact, telContact, techniciens, resp, dateDebut, dureeTotale, observation, photo ){

                    @RequiresApi(Build.VERSION_CODES.O)
                fun addSection(nbBrins: Long, longueur: Double, diametre: Double){
                    sectionsFils.add(Section(nbBrins,longueur,diametre))
                }
                fun addSchema (uri: Uri){
                    schemas.add(uri.toString())
                }
                fun getSectionFils(): List<Section> {
                    return sectionsFils
                }
                fun getSection(index: Int): String {
                    return "nb brins:"+sectionsFils[index].nbBrins+" - longueur: "+sectionsFils[index].longueur
                }



}