package com.example.applicationstb.model

import android.media.Image
import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDateTime

class Section (var brins:Int, var longueur: Double){
}

class Bobinage( numDevis: String,
                numChantier: String,
                client: Client,
                contact: String,
                telContact: Long,
                techniciens: Array<User>,
                resp: User,
                var marque:String,
                val type:String,
                val puissance:Long,
                var vitesse: Long,
                var tension: Long,
                var phases:String,
                var frequence:String,
                var courant:String,
                val callage:Boolean) : Fiche(numDevis, numChantier, client, contact, telContact, techniciens, resp){
                    var sectionsFils = arrayListOf<Section>();
                    var nbSpires = 0;
                    var Resistance = mutableMapOf<String,Long>()
                    var Isolement1 = mutableMapOf<String,Long>()
                    var Isolement2 = mutableMapOf<String,Long>()
                    @RequiresApi(Build.VERSION_CODES.O)
                    var date = LocalDateTime.now() ;
                    var Observation = ""
                    var schema = arrayOf<Image>()
                fun addSection(brins: Int, longueur: Double){
                    sectionsFils.add(Section(brins,longueur))
                }
                fun getSectionFils(): ArrayList<Section> {
                    return sectionsFils
                }



}