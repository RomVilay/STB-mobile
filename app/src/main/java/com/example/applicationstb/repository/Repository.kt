package com.example.applicationstb.repository

import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import com.example.applicationstb.model.Fiche
import com.example.applicationstb.model.User
import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.ToJson
import org.json.JSONArray
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.text.SimpleDateFormat
import java.util.*

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

class LoginResponse(
    var token:String?,
    var user:User?,
    var error: String?
)
class FichesResponse(
    var fiches:Array<Fiche>?
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

class Repository {
    private val moshiBuilder = Moshi.Builder().add(CustomDateAdapter())
    val url = "http://195.154.107.195:4000"
    val retrofit = Retrofit.Builder()
        .baseUrl(url)
        .addConverterFactory(MoshiConverterFactory.create(moshiBuilder.build()))
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()
    val service : APIstb by lazy {  retrofit.create(APIstb::class.java) }
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
}