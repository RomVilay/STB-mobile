package com.example.applicationstb.repository

import android.content.Context
import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.applicationstb.localdatabase.*
import com.example.applicationstb.model.*
import com.squareup.moshi.*
import okhttp3.ConnectionSpec
import okhttp3.OkHttpClient
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.File
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.concurrent.TimeUnit

var baseUrl = "https://back-end.stb.dev.alf-environnement.net/1.1.0/"
var minioUrl = "https:/minio.stb.dev.alf-environnement.net"

class BodyLogin(var username: String?, var password: String?) : Parcelable {
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

class BodyPointage(var user: String?, var timestamp: String?) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(user)
        parcel.writeString(timestamp)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BodyPointage> {
        override fun createFromParcel(parcel: Parcel): BodyPointage {
            return BodyPointage(parcel)
        }

        override fun newArray(size: Int): Array<BodyPointage?> {
            return arrayOfNulls(size)
        }
    }
}


class BodyChantier(
    var observations: String?,
    var photos: Array<String>?,
    var materiel: String?,
    var objet: String?,
    var status: Long?,
    var dureeTotale: Long?,
    var signatureClient: String?,
    var signatureTech: String?,
    var dureeEssai:String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        arrayOf<String>().apply {
            parcel.readArray(String::class.java.classLoader)
        },
        parcel.readString(),
        parcel.readString(),
        parcel.readLong(),
        parcel.readLong(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(observations)
        arrayOf<String>().apply {
            parcel.writeArray(photos)
        }
        parcel.writeString(materiel)
        parcel.writeString(objet)
        parcel.writeLong(status!!)
        parcel.writeLong(dureeTotale!!)
        parcel.writeString(signatureClient)
        parcel.writeString(signatureTech)
        parcel.writeString(dureeEssai)
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

class BodyBobinage(
    var marqueMoteur: String?,
    var typeBobinage: String?,
    var vitesse: Float?,
    var puissance: Float?,
    var phases: Long?,
    var frequences: Float?,
    var courant: Float?,
    var nbSpires: Long?,
    var resistanceU: Float?,
    var resistanceV: Float?,
    var resistanceW: Float?,
    var isolementUT: Float?,
    var isolementVT: Float?,
    var isolementWT: Float?,
    var isolementUV: Float?,
    var isolementUW: Float?,
    var isolementVW: Float?,
    var status: Long?,
    var calageEncoches: Boolean?,
    var sectionsFils: List<Section>?,
    var observations: String?,
    var poids: Float?,
    var tension: String?,
    var dureeTotale: Long?,
    var photos: Array<String>?,
    var presenceSondes: Boolean?,
    var typeSondes: String?,
    val pasPolaire: String?,
    var branchement: String?,
    var nbEncoches: Long?
) : Parcelable {
    @RequiresApi(Build.VERSION_CODES.Q)
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readLong(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readLong(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readFloat(),
        parcel.readLong(),
        parcel.readBoolean(),
        listOf<Section>().apply {
            parcel.readList(this, Section::class.java.classLoader)
        },
        parcel.readString(),
        parcel.readFloat(),
        parcel.readString(),
        parcel.readLong(),
        arrayOf<String>().apply {
            parcel.readArray(String::class.java.classLoader)
        },
        parcel.readBoolean(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readLong(),
    ) {
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(marqueMoteur!!)
        parcel.writeString(typeBobinage!!)
        parcel.writeFloat(vitesse!!)
        parcel.writeFloat(puissance!!)
        parcel.writeLong(phases!!)
        parcel.writeFloat(frequences!!)
        parcel.writeFloat(courant!!)
        parcel.writeLong(nbSpires!!)
        parcel.writeFloat(resistanceU!!)
        parcel.writeFloat(resistanceV!!)
        parcel.writeFloat(resistanceW!!)
        parcel.writeFloat(isolementUT!!)
        parcel.writeFloat(isolementVT!!)
        parcel.writeFloat(isolementWT!!)
        parcel.writeFloat(isolementUV!!)
        parcel.writeFloat(isolementUW!!)
        parcel.writeFloat(isolementVW!!)
        parcel.writeLong(status!!)
        parcel.writeBoolean(calageEncoches!!)
        listOf<Section>().apply {
            parcel.writeList(this)
        }
        parcel.writeString(observations!!)
        parcel.writeFloat(poids!!)
        parcel.writeString(tension!!)
        parcel.writeLong(dureeTotale!!)
        arrayOf<String>().apply {
            parcel.writeArray(this)
        }
        parcel.writeBoolean(presenceSondes!!)
        parcel.writeString(typeSondes)
        parcel.writeString(pasPolaire)
        parcel.writeString(branchement)
        parcel.writeLong(nbEncoches!!)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BodyBobinage> {
        @RequiresApi(Build.VERSION_CODES.Q)
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
    var token: String?,
    var user: User?,
    var error: String?
)

class userPointage(
    var _id: String,
    var username: String?,
    var nom: String?,
    var prenom: String?
)

class Pointage2(
    var _id: String,
    var user: userPointage,
    var timestamp: String
) {
    @RequiresApi(Build.VERSION_CODES.O)
    fun toPointage(): Pointage {
        return Pointage(
            _id,
            user._id,
            ZonedDateTime.parse(timestamp)
        )
    }
}

class PointageResponse(
    var data: Pointage
)

class PointagesResponse(
    var data: Array<Pointage2>?
)

class FichesResponse(
    var data: Array<Fiche>?
)

class ChantierResponse(
    var data: Chantier?
)

class BobinageResponse(
    var data: Bobinage?
)

class DemontageTriphaseResponse(
    var data: Triphase?
)

class DemontageMotoreducteurResponse(
    var data: DemontageMotoreducteur?
)

class DemontageMotopompeResponse(
    var data: DemontageMotopompe?
)

class DemontageReducteurResponse(
    var data: DemontageReducteur?
)


class DemontageCCResponse(
    var data: CourantContinu?
)

class DemontagePompeResponse(
    var data: DemontagePompe?
)

class VehiculesResponse(
    var data: Vehicule?
)

class DemontageResponse(
    var data: DemontageMoteur?
)

class DemontagesResponse(
    var data: Array<DemontageMoteur>?
)

class DemontageAlternateurResponse(
    var data: DemontageAlternateur?
)

class DemontageMonophaseResponse(
    var data: DemontageMonophase?
)

class DemontageRotorBobineResponse(
    var data: DemontageRotorBobine?
)

class ClientsResponse(
    var data: Client?
)

class CustomDateAdapter : JsonAdapter<Date>() {
    private val dateFormat = SimpleDateFormat(SERVER_FORMAT, Locale.getDefault())

    @FromJson
    override fun fromJson(reader: JsonReader): Date? {
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

class CustomDateAdapter2 : JsonAdapter<ZonedDateTime>() {

    @RequiresApi(Build.VERSION_CODES.O)
    @FromJson
    override fun fromJson(reader: JsonReader): ZonedDateTime? {
        return try {
            val dateAsString = reader.nextString()
            ZonedDateTime.parse(dateAsString).withZoneSameLocal(ZoneOffset.of(SimpleDateFormat("Z").format(Date())))
        } catch (e: Exception) {
            null
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @ToJson
    override fun toJson(writer: JsonWriter, value: ZonedDateTime?) {
        if (value != null) {
            writer.value(value.toString())
        }
    }

    companion object {}
}


class PhotoResponse(
    var photo: File
)

class Repository(var context: Context) {
    private val moshiBuilder = Moshi.Builder().add(CustomDateAdapter()).add(CustomDateAdapter2())
    var okHttpClient = OkHttpClient.Builder()
        .callTimeout(1, TimeUnit.MINUTES)
        .connectTimeout(1, TimeUnit.MINUTES)
        .readTimeout(1, TimeUnit.MINUTES)
        .writeTimeout(2, TimeUnit.MINUTES)
        .connectionSpecs(Arrays.asList(ConnectionSpec.MODERN_TLS, ConnectionSpec.COMPATIBLE_TLS, ConnectionSpec.CLEARTEXT))
        .build()

    val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create(moshiBuilder.build()))
        .build()
    val service: APIstb by lazy { retrofit.create(APIstb::class.java) }
    var db: LocalDatabase? = null;
    var chantierDao: ChantierDao? = null;
    var bobinageDao: BobinageDao? = null;
    var vehiculeDao: VehiculeDao? = null;
    var clientDao: ClientsDao? = null;
    var remontageDao: RemontageDao? = null;
    var pointageDao: PointageDao? = null;

    var remontageRepository : RemontageRepository? = null;
    var demontageRepository : DemontageRepository? = null;

    fun logUser(username: String, psw: String, callback: Callback<LoginResponse>) {
        var body = BodyLogin(username, psw)
        var call = service.loginUser(body)
        var user: User? = null;
        call.enqueue(callback)
    }

    fun getFiches(token: String, callback: Callback<FichesResponse>) {
        var call = service.getFiches(token)
        var fiches: Array<Fiche>? = null
        call.enqueue(callback)
    }

    fun getFichesUser(token: String, userid: String, callback: Callback<FichesResponse>) {
        var call = service.getFichesUser(token, userid)
        var fiches: Array<Fiche>? = null
        call.enqueue(callback)
    }


    fun getChantier(token: String, ficheId: String, callback: Callback<ChantierResponse>) {
        var call = service.getChantier(token, ficheId)
        var fiche: Chantier? = null
        call.enqueue(callback)
    }

    fun getAllChantier(token: String, username: String, callback: Callback<ChantierResponse>) {
        var call = service.getAllChantier(token, username)
        var fiche: Chantier? = null
        call.enqueue(callback)
    }

    fun getBobinage(token: String, ficheId: String, callback: Callback<BobinageResponse>) {
        var call = service.getBobinage(token, ficheId)
        var fiche: Bobinage? = null
        call.enqueue(callback)
    }



    fun getAllVehicules(token: String, callback: Callback<VehiculesResponse>) {
        var call = service.getAllVehicules(token)
        var vehicules: Array<Vehicule>? = null
        call.enqueue(callback)
    }

    fun getVehiculesById(token: String, id: String, callback: Callback<VehiculesResponse>) {
        var call = service.getVehiculeById(token, id)
        var vehicule: Vehicule? = null
        call.enqueue(callback)
    }

    fun getAllClients(token: String, callback: Callback<ClientsResponse>) {
        var call = service.getAllClients(token)
        var vehicules: Array<Client>? = null
        call.enqueue(callback)
    }


    fun patchBobinage(
        token: String,
        ficheId: String,
        bobinage: Bobinage,
        callback: Callback<BobinageResponse>
    ) {
        if (bobinage.resistanceV == null) {
            bobinage.resistanceV = 0f
        }
        if (bobinage.resistanceW == null) {
            bobinage.resistanceW = 0f
        }
        if (bobinage.isolementUT == null) {
            bobinage.isolementUT = 0f
        }

        if (bobinage.isolementVT == null) {
            bobinage.isolementVT = 0f
        }
        if (bobinage.isolementWT == null) {
            bobinage.isolementWT = 0f
        }
        if (bobinage.isolementUV == null) {
            bobinage.isolementUV = 0f
        }
        if (bobinage.isolementUW == null) {
            bobinage.isolementUW = 0f
        }
        if (bobinage.isolementVW == null) {
            bobinage.isolementVW = 0f
        }
        if (bobinage.poids == null) {
            bobinage.poids = 0f
        }
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
            bobinage.resistanceV,
            bobinage.resistanceW,
            bobinage.isolementUT,
            bobinage.isolementVT,
            bobinage.isolementWT,
            bobinage.isolementUV,
            bobinage.isolementUW,
            bobinage.isolementVW,
            bobinage.status,
            if (bobinage.calageEncoches !== null) {
                bobinage.calageEncoches
            } else false,
            bobinage.sectionsFils!!.toList(),
            if (bobinage.observations !== null) {
                bobinage.observations
            } else "",
            bobinage.poids,
            bobinage.tension,
            bobinage.dureeTotale,
            bobinage.photos,
            bobinage.presenceSondes,
            bobinage.typeSondes,
            bobinage.pasPolaire,
            bobinage.branchement,
            bobinage.nbEncoches
        )
        var call = service.patchBobinage(token, ficheId, body)
        var fiche: Bobinage? = null
        call.enqueue(callback)
    }

    fun patchChantier(
        token: String,
        ficheId: String,
        chantier: Chantier,
        callback: Callback<ChantierResponse>
    ) {
        var body = BodyChantier(
            chantier.observations,
            chantier.photos,
            chantier.materiel,
            chantier.objet,
            chantier.status,
            chantier.dureeTotale,
            chantier.signatureClient,
            chantier.signatureTech,
            chantier.dureeEssai
        )
        var call = service.patchChantier(token, ficheId, body)
        call.enqueue(callback)
    }

    fun getVehiculeById(token: String, vehiculeId: String, callback: Callback<VehiculesResponse>) {
        var call = service.getVehiculeById(token, vehiculeId)
        var vehicule: Vehicule? = null
        call.enqueue(callback)
    }

    // photo requests
    suspend fun getURLToUploadPhoto(token: String) = service.getURLToUploadPhoto(token)

    fun getPointages(token: String, userid: String, callback: Callback<PointagesResponse>) {
        var dateMin =
            ZonedDateTime.of(LocalDateTime.now().withDayOfMonth(1), ZoneOffset.of(SimpleDateFormat("Z").format(Date())))
        var dateMax = ZonedDateTime.of(LocalDateTime.now(), ZoneOffset.of(SimpleDateFormat("Z").format(Date())))
        var call =
            service.getPointages(token, "0", 0, userid, dateMin.toString(), dateMax.toString())
        call.enqueue(callback)
    }

    suspend fun postPointages(token: String, userid: String, dateTime: ZonedDateTime) =
        service.postPointages(token, BodyPointage(userid, dateTime.toString()))

    suspend fun getURLPhoto(token: String, photoName: String) =
        service.getURLPhoto(token, photoName)

    suspend fun deletePointage(token: String,pointage: String) {
        service.deletePointage(token,pointage)
    }
    suspend fun deleteAllPointages(token: String, userid: String){
        var list = service.getPointages(token,"0",0, userid, "1970-01-01T00:00:00.000+01:00", ZonedDateTime.now().format(
            DateTimeFormatter.ISO_INSTANT)).execute()
            Log.i("info", "list ${list.body()}")
            /*list.
            list.body()!!.data!!.forEach {
                deletePointage(token, it._id)
            }*/

    }



    suspend fun getPhoto(address: String) = service.getPhoto(address)

    val MIGRATION_20_21 = object : Migration(20, 21) {
        override fun migrate(database: SupportSQLiteDatabase) {
            // Create the new table
            database.execSQL(
                "CREATE TABLE demontage_pompe_new (_id TEXT NOT NULL,numDevis TEXT, numFiche TEXT, statut INTEGER,client TEXT NOT NULL, contact TEXT, telContact TEXT, dateDebut INTEGER, dureeTotale INTEGER, observation TEXT, photos TEXT, typeFicheDemontage INTEGER NOT NULL, typeMoteur TEXT, marque TEXT, numSerie TEXT, fluide TEXT, sensRotation INTEGER, typeRessort INTEGER, typeJoint TEXT, matiere INTEGER, diametreArbre TEXT, diametreExtPR TEXT, diametreExtPF TEXT, epaisseurPF TEXT, longueurRotativeNonComprimee TEXT, longueurRotativeComprimee TEXT, longueurRotativeTravail TEXT, PRIMARY KEY (_id))"
            )
            // Copy the data
            database.execSQL(
                "INSERT INTO demontage_pompe_new (_id, numDevis, numFiche,statut, client, contact, telContact,dateDebut, dureeTotale, observation, photos, typeFicheDemontage, typeMoteur, marque, numSerie,fluide, sensRotation, typeRessort, typeJoint, matiere, diametreArbre, diametreExtPR, diametreExtPF, epaisseurPF, longueurRotativeNonComprimee, longueurRotativeComprimee, longueurRotativeTravail) SELECT _id, numDevis, numFiche,statut, client, contact, telContact,dateDebut, dureeTotale, observation, photos, typeFicheDemontage, typeMoteur, marque, numSerie,fluide, sensRotation, typeRessort, typeJoint, matiere, diametreArbre, diametreExtPR, diametreExtPF, epaisseurPF, longueurRotativeNonComprimee, longueurRotativeComprimee, longueurRotativeTravail FROM demontage_pompe"
            )
            // Remove the old table
            database.execSQL("DROP TABLE demontage_pompe")
            // Change the table name to the correct one
            database.execSQL("ALTER TABLE demontage_pompe_new RENAME TO demontage_pompe")
        }
    }
    val MIGRATION_21_22 = object : Migration(21, 22) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("ALTER TABLE demontage_pompe ADD COLUMN refRoulementAvant TEXT")
            database.execSQL("ALTER TABLE demontage_pompe ADD COLUMN refRoulementArriere TEXT")
            database.execSQL("ALTER TABLE demontage_pompe ADD COLUMN typeRoulementArriere TEXT")
            database.execSQL("ALTER TABLE demontage_pompe ADD COLUMN typeRoulementAvant TEXT")
            database.execSQL("ALTER TABLE demontage_pompe ADD COLUMN refJointAvant TEXT")
            database.execSQL("ALTER TABLE demontage_pompe ADD COLUMN refJointArriere TEXT")
            database.execSQL("ALTER TABLE demontage_pompe ADD COLUMN typeJointAvant INTEGER")
            database.execSQL("ALTER TABLE demontage_pompe ADD COLUMN typeJointArriere INTEGER")
        }
    }

    suspend fun createDb() {
        db = Room.databaseBuilder(context, LocalDatabase::class.java, "database-local")
            .fallbackToDestructiveMigration()
            .build()
        chantierDao = db!!.chantierDao()
        bobinageDao = db!!.bobinageDao()
        vehiculeDao = db!!.vehiculesDao()
        clientDao = db!!.clientDao()
        pointageDao = db!!.pointageDao()
        remontageRepository = RemontageRepository(service,db!!)
        demontageRepository = DemontageRepository(service,db!!)
        Log.i("INFO", "db crée")
    }

    //requêtes chantier
    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun getAllVehiculesLocalDatabase(): List<VehiculeEntity> {
        return vehiculeDao!!.getAll()
    }

    suspend fun insertVehiculesLocalDatabase(vehicule: Vehicule) {
        vehiculeDao!!.insertAll(vehicule.toEntity())
    }

    suspend fun getByIdVehiculesLocalDatabse(id: String): Vehicule? {
        if (vehiculeDao!!.getById(id) !== null) {
            return vehiculeDao!!.getById(id).toVehicule()
        } else return null
    }

    suspend fun updateVehiculesLocalDatabse(vehicule: VehiculeEntity) {
        vehiculeDao!!.update(vehicule)
    }

    suspend fun deleteVehiculeLocalDatabse(vehicule: VehiculeEntity) {
        vehiculeDao!!.delete(vehicule)
    }

    suspend fun getAllClientsLocalDatabase(): List<ClientEntity> {
        return clientDao!!.getAll()
    }

    suspend fun insertClientsLocalDatabase(client: Client) {
        clientDao!!.insertAll(client.toEntity())
    }

    suspend fun updateClientsLocalDatabse(client: ClientEntity) {
        clientDao!!.update(client)
    }

    suspend fun deleteClientsLocalDatabse(client: ClientEntity) {
        clientDao!!.delete(client)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun insertChantierLocalDatabase(chantier: Chantier) {
        chantierDao!!.insertAll(chantier.toEntity())
    }

    suspend fun getAllChantierLocalDatabase(): List<ChantierEntity> {
        return chantierDao!!.getAll()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun getByIdChantierLocalDatabse(id: String): Chantier? {
        if (chantierDao!!.getById(id) !== null) {
            return chantierDao!!.getById(id).toChantier()
        } else return null
    }

    suspend fun updateChantierLocalDatabse(chantier: ChantierEntity) {
        chantierDao!!.update(chantier)
    }

    suspend fun deleteChantierLocalDatabse(chantier: ChantierEntity) {
        chantierDao!!.delete(chantier)
    }

    //requêtes bobinage
    suspend fun insertBobinageLocalDatabase(bobinage: Bobinage) {
        bobinageDao!!.insertAll(bobinage.toEntity())
    }

    suspend fun getAllBobinageLocalDatabase(): List<BobinageEntity> {
        return bobinageDao!!.getAll()
    }

    suspend fun getByIdBobinageLocalDatabse(id: String): Bobinage? {
        try {
            if (bobinageDao!!.getById(id) !== null) {
                return bobinageDao!!.getById(id).toBobinage()
            } else return null
        } catch (e: Error) {
            Log.i("e", e.message!!)
            return null
        }
    }

    suspend fun updateBobinageLocalDatabse(bobinage: BobinageEntity) {
        bobinageDao!!.update(bobinage)
    }

    suspend fun deleteBobinageLocalDatabse(bobinage: BobinageEntity) {
        bobinageDao!!.delete(bobinage)
    }

    suspend fun insertPointageDatabase(pointage: Pointage) {
        pointageDao!!.insertAll(pointage.toEntity())
    }

    suspend fun getAllPointageLocalDatabase(): List<PointageEntity> {
        return pointageDao!!.getAll()
    }

    suspend fun getByIdPointageDatabase(id: String): Pointage? {
        try {
            if (pointageDao!!.getById(id) !== null) {
                return pointageDao!!.getById(id).toPointage()
            } else return null
        } catch (e: Error) {
            Log.i("e", e.message!!)
            return null
        }
    }

    suspend fun updatePointageLocalDatabase(pointage: PointageEntity) {
        pointageDao!!.update(pointage)
    }

    suspend fun deletePointageLocalDatabse(pointage: PointageEntity) {
        pointageDao!!.delete(pointage)
    }


}