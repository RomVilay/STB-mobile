package com.example.applicationstb.model

import android.media.Image
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDateTime
import java.util.*

class Section (var brins:Int, var longueur: Double){
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
               var marque:String,
               val typeb:String,
               val puissance:Long,
               var vitesse: Long,
               var tension: Long,
               var phases:String,
               var frequence:String,
               var courant:String,
               val callage:Boolean) : Fiche(idFiche, numDevis, numFiche, type, statut, client, contact, telContact, techniciens, resp, dateDebut, dureeTotale, observation, photo ){
                    var sectionsFils : MutableList<Section> = mutableListOf();
                    var nbSpires = 0;
                    var Resistance = mutableMapOf<String,Long>()
                    var Isolement1 = mutableMapOf<String,Long>()
                    var Isolement2 = mutableMapOf<String,Long>()
                    @RequiresApi(Build.VERSION_CODES.O)
                    var schemas : MutableList<Uri> = mutableListOf();
                fun addSection(brins: Int, longueur: Double){
                    sectionsFils.add(Section(brins,longueur))
                }
                fun addSchema (uri: Uri){
                    schemas.add(uri)
                }
                fun getSectionFils(): List<Section> {
                    return sectionsFils
                }
                fun getSection(index: Int): String {
                    return "nb brins:"+sectionsFils[index].brins+" - longueur: "+sectionsFils[index].longueur
                }



}