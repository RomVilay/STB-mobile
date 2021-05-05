package com.example.applicationstb.ui.ficheBobinage

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SchemaViewModel : ViewModel() {
    val schemaUri = MutableLiveData<Uri>()

    fun setUri(uri:Uri) {
        schemaUri.value = uri
    }
}