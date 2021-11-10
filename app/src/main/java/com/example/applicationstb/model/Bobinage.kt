package com.example.applicationstb.model

import android.media.Image
import android.net.Uri
import android.os.Build
import android.os.Parcelable
import androidx.annotation.RequiresApi
import com.example.applicationstb.localdatabase.BobinageEntity
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import java.time.LocalDateTime
import java.util.*
@Parcelize
@Serializable
class Section (var nbBrins:Long, var diametre: Double): Parcelable{
    override fun toString(): String {
        return "{\"nbBrins\":${nbBrins},\"diametre\":${diametre}}"
    }
}

class Bobinage(idFiche:String,
               numDevis: String?,
               numFiche: String?,
               type:Long?,
               statut: Long?,
               client: Client?,
               contact: String?,
               telContact: String?,
               techniciens: Array<User>?,
               resp: User?,
               dateDebut: Date?,
               dureeTotale:Long?,
               observation: String?,
               photo:Array<String>?,
               var marqueMoteur:String?,
               var typeBobinage:String?,
               var puissance:Float?,
               var vitesse: Float?,
               var phases:Long?,
               var frequences:Float?,
               var courant:Float?,
               var calageEncoches:Boolean?,
               var sectionsFils : MutableList<Section>?,
               var nbSpires : Long?,
               var resistanceU : Float?,
               var resistanceV : Float?,
               var resistanceW : Float?,
               var isolementUT : Float?,
               var isolementVT : Float?,
               var isolementWT : Float?,
               var isolementUV : Float?,
               var isolementUW : Float?,
               var isolementVW : Float?,
               var schemas : MutableList<String>?,
               var poids : Float?,
               var tension: Long?
                ) : Fiche(idFiche, numDevis, numFiche, type, statut, client, contact, telContact, techniciens, resp, dateDebut, dureeTotale, observation, photo ){

                    @RequiresApi(Build.VERSION_CODES.O)
                fun addSection(nbBrins: Long, diametre: Double){
                    sectionsFils!!.add(Section(nbBrins,diametre))
                }
                fun addSchema (uri: Uri){
                    schemas!!.add(uri.toString())
                }
                fun getSectionFils(): List<Section> {
                    return sectionsFils!!.toList()
                }
    fun toEntity() : BobinageEntity {
        return BobinageEntity(_id,
            numDevis,
            numFiche,
            status!!,
            client!!._id,
            contact,
            telContact,
            dateDebut,
            dureeTotale!!,
            observations,
            photo,
            marqueMoteur,
            typeBobinage,
            vitesse,
            puissance,
            phases,
            frequences,
            courant,
            calageEncoches,
            sectionsFils!!,
            nbSpires,
            resistanceU,
            resistanceV,
            resistanceW,
            isolementUT,
            isolementVT,
            isolementWT,
            isolementUV,
            isolementUW,
            isolementVW,
            poids,
            tension)
    }
              /*  fun getSection(index: Int): String {
                    return "nb brins:"+sectionsFils[index].nbBrins+" - diamètre: "+sectionsFils[index].diametre
                }*/



}