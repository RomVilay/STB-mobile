package com.example.applicationstb.repository

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.applicationstb.localdatabase.*
import com.example.applicationstb.model.*
import com.squareup.moshi.*
import kotlinx.parcelize.Parcelize
import org.json.JSONArray
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.text.SimpleDateFormat
import java.util.*
import kotlin.coroutines.coroutineContext

class BodyLogin(var username: String?, var password: String?): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(username)
        parcel.writeString(password)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BodyLogin> {
        override fun createFromParcel(parcel: Parcel): BodyLogin {
            return BodyLogin(parcel)
        }

        override fun newArray(size: Int): Array<BodyLogin?> {
            return arrayOfNulls(size)
        }
    }
}

class BodyChantier(var materiel: String?, var objet: String?, var observations: String?, var status: Long?): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readLong()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(materiel)
        parcel.writeString(objet)
        parcel.writeString(observations)
        parcel.writeLong(status!!)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BodyChantier> {
        override fun createFromParcel(parcel: Parcel): BodyChantier {
            return BodyChantier(parcel)
        }

        override fun newArray(size: Int): Array<BodyChantier?> {
            return arrayOfNulls(size)
        }
    }
}

class BodyBobinage(var marqueMoteur : String?,
    var typeBobinage: String?,
    var vitesse:Long?,
    var puissance:Long?,
    var phases:Long?,
    var frequences: Long?,
    var courant: Long?,
    var nbSpires: Long?,
    var resistanceU: Long?,
    var resistanceV: Long,
    var resistanceW: Long,
    var isolementUT: Long,
    var isolementVT: Long,
    var isolementWT: Long,
    var isolementUV: Long,
    var isolementUW: Long,
    var isolementVW: Long,
    var status: Long,
    var calageEncoches: Boolean,
    var sectionsFils: List<Section>? ,
    var observations: String?,
    var poids:Long,
    var tension:Long
                  ): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readLong(),
        parcel.readLong(),
        parcel.readLong(),
        parcel.readLong(),
        parcel.readLong(),
        parcel.readLong(),
        parcel.readLong(),
        parcel.readLong(),
        parcel.readLong(),
        parcel.readLong(),
        parcel.readLong(),
        parcel.readLong(),
        parcel.readLong(),
        parcel.readLong(),
        parcel.readLong(),
        parcel.readLong(),
        parcel.readBoolean(),
        listOf<Section>().apply {
            parcel.readList(this,Section::class.java.classLoader)
        },
        parcel.readString(),
        parcel.readLong(),
        parcel.readLong()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(marqueMoteur!!)
        parcel.writeString(typeBobinage!!)
        parcel.writeLong(vitesse!!)
        parcel.writeLong(puissance!!)
        parcel.writeLong(phases!!)
        parcel.writeLong(frequences!!)
        parcel.writeLong(courant!!)
        parcel.writeLong(nbSpires!!)
        parcel.writeLong(resistanceU!!)
        parcel.writeLong(resistanceV!!)
        parcel.writeLong(resistanceW!!)
        parcel.writeLong(isolementUT!!)
        parcel.writeLong(isolementVT!!)
        parcel.writeLong(isolementWT!!)
        parcel.writeLong(isolementUV!!)
        parcel.writeLong(isolementUW!!)
        parcel.writeLong(isolementVW!!)
        parcel.writeLong(status!!)
        parcel.writeBoolean(calageEncoches!!)
        listOf<Section>().apply {
            parcel.writeList(this)
        }
        parcel.writeString(observations!!)
        parcel.writeLong(poids)
        parcel.writeLong(tension)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BodyBobinage> {
        override fun createFromParcel(parcel: Parcel): BodyBobinage {
            return BodyBobinage(parcel)
        }

        override fun newArray(size: Int): Array<BodyBobinage?> {
            return arrayOfNulls(size)
        }
    }
}

class LoginResponse(
    @field:Json(name = "auth-token")
    var token:String?,
    var user:User?,
    var error: String?
)
class FichesResponse(
    var fiches:Array<Fiche>?
)
class ChantierResponse(
    var fiche:Chantier?
)
class BobinageResponse(
    var fiche:Bobinage?
)
class VehiculesResponse(
    var vehicule:Vehicule?
)

class CustomDateAdapter : JsonAdapter <Date>() {
    private val dateFormat = SimpleDateFormat(SERVER_FORMAT, Locale.getDefault())

    @FromJson
    override fun fromJson(reader: JsonReader ): Date? {
        return try {
            val dateAsString = reader.nextString()
            synchronized(dateFormat) {
                dateFormat.parse(dateAsString)
            }
        } catch (e: Exception) {
            null
        }
    }
    @ToJson
    override fun toJson(writer: JsonWriter, value: Date?) {
        if (value != null) {
            synchronized(dateFormat) {
                writer.value(value.toString())
            }
        }
    }
    companion object {
        const val SERVER_FORMAT = ("yyyy-MM-dd'T'HH:mm") // define your server format here
    }
}
class Repository (var context:Context) {
    private val moshiBuilder = Moshi.Builder().add(CustomDateAdapter())
    val url = "http://195.154.107.195:4000"
    val retrofit = Retrofit.Builder()
        .baseUrl(url)
        .addConverterFactory(MoshiConverterFactory.create(moshiBuilder.build()))
        .build()
    val service : APIstb by lazy {  retrofit.create(APIstb::class.java) }
    var db : LocalDatabase? = null;
    var chantierDao : ChantierDao? = null;
    var bobinageDao : BobinageDao? = null;

    fun logUser(username:String,psw:String,callback: Callback<LoginResponse>) {
        var body = BodyLogin(username,psw)
        var call = service.loginUser(body)
        var user: User? = null;
        call.enqueue(callback)
    }
    fun getFiches(token:String, callback:Callback<FichesResponse>) {
        var call = service.getFiches(token)
        var fiches: Array<Fiche>? = null
        call.enqueue(callback)
    }
    fun getFichesUser(token:String, userid:String, callback:Callback<FichesResponse>) {
        var call = service.getFichesUser(token,userid)
        var fiches: Array<Fiche>? = null
        call.enqueue(callback)
    }
    fun getChantier(token:String,ficheId:String, callback:Callback<ChantierResponse>){
        var call = service.getChantier(token,ficheId)
        var fiche:Chantier? = null
        call.enqueue(callback)
    }
    fun getBobinage(token:String,ficheId:String, callback:Callback<BobinageResponse>){
        var call = service.getBobinage(token,ficheId)
        var fiche:Bobinage? = null
        call.enqueue(callback)
    }
    fun patchBobinage(token:String,ficheId:String, bobinage:Bobinage, callback:Callback<BobinageResponse>){
        var body = BodyBobinage(
            bobinage.marqueMoteur,
            bobinage.typeBobinage,
            bobinage.vitesse,
            bobinage.puissance,
            bobinage.phases,
            bobinage.frequences,
            bobinage.courant,
            bobinage.nbSpires,
            bobinage.resistanceU,
            bobinage.resistanceV!!,
            bobinage.resistanceW!!,
            bobinage.isolementUT!!,
            bobinage.isolementVT!!,
            bobinage.isolementWT!!,
            bobinage.isolementUV!!,
            bobinage.isolementUW!!,
            bobinage.isolementVW!!,
            bobinage.status!!,
            bobinage.calageEncoches!!,
            bobinage.sectionsFils!!.toList(),
            bobinage.observations!!,
            0,
            0)
        var call = service.patchBobinage(token,ficheId,body)
        var fiche:Bobinage? = null
        call.enqueue(callback)
    }
    fun patchChantier(token:String,ficheId:String, chantier:Chantier, callback:Callback<ChantierResponse>){
        var body = BodyChantier(chantier.materiel, chantier.objet, chantier.observations, chantier.status)
        var call = service.patchChantier(token,ficheId,body)
        var fiche:Chantier? = null
        call.enqueue(callback)
    }
    fun getVehiculeById(token:String, vehiculeId:String, callback:Callback<VehiculesResponse>) {
        var call = service.getVehiculeById(token,vehiculeId)
        var vehicule: Vehicule? = null
        call.enqueue(callback)
    }
    suspend fun createDb(){
      db = Room.databaseBuilder(context, LocalDatabase::class.java, "database-local")
          .fallbackToDestructiveMigration()
          .build()
      chantierDao = db!!.chantierDao()
      bobinageDao = db!!.bobinageDao()
        Log.i("INFO","db créée")
    }

    suspend fun insertChantierLocalDatabase(chantier: Chantier){
        chantierDao!!.insertAll(chantier.toEntity())
    }
   suspend fun getAllChantierLocalDatabase(): List<ChantierEntity>{
        return chantierDao!!.getAll()
    }
    suspend fun getByIdChantierLocalDatabse( id: String): Chantier? {
        if (chantierDao!!.getById(id) !== null) {
            return chantierDao!!.getById(id).toChantier()
        } else return null
    }
    suspend fun updateChantierLocalDatabse( chantier: ChantierEntity){
        chantierDao!!.update(chantier)
    }
    suspend fun deleteChantierLocalDatabse( chantier: ChantierEntity){
        chantierDao!!.delete(chantier)
    }
    suspend fun insertBobinageLocalDatabase(bobinage: Bobinage){
        bobinageDao!!.insertAll(bobinage.toEntity())
    }
    suspend fun getAllBobinageLocalDatabase(): List<BobinageEntity>{
        return bobinageDao!!.getAll()
    }
    suspend fun getByIdBobinageLocalDatabse( id: String) : Bobinage? {
        if (bobinageDao!!.getById(id) !== null) {
            return bobinageDao!!.getById(id).toBobinage()
        } else return null
    }
    suspend fun updateBobinageLocalDatabse( bobinage: BobinageEntity){
        bobinageDao!!.update(bobinage)
    }
    suspend fun deleteBobinageLocalDatabse( bobinage: BobinageEntity){
        bobinageDao!!.delete(bobinage)
    }
}