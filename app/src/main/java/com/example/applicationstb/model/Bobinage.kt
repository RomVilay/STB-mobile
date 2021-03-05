package com.example.applicationstb.model

import android.media.Image
import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDateTime

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

                    var sectionsFils = mapOf<Long,Int>();
                    var nbSpires = 0;
                    var Resistance = mapOf<String,Long>()
                    var Isolement1 = mapOf<String,Long>()
                    var Isolement2 = mapOf<String,Long>()
                    @RequiresApi(Build.VERSION_CODES.O)
                    var date = LocalDateTime.now() ;
                    var Observation = ""
                    var schema = arrayOf<Image>()



}