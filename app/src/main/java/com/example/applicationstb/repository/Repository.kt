package com.example.applicationstb.repository

import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import com.example.applicationstb.model.User
import org.json.JSONArray
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

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

class Repository {
    val url = "http://176.141.253.12:4000"
    val retrofit = Retrofit.Builder()
        .baseUrl(url)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()
    val service : APIstb by lazy {  retrofit.create(APIstb::class.java) }
    fun logUser(username:String,psw:String,callback: Callback<LoginResponse>) {
        var body = BodyLogin(username,psw)
        var call = service.loginUser(body)
        var user: User? = null;
        call.enqueue(callback)
    }
}