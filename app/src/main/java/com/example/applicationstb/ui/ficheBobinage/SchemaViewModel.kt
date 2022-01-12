package com.example.applicationstb.ui.ficheBobinage

import android.net.Uri
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.Navigation
import com.example.applicationstb.R

class SchemaViewModel : ViewModel() {
    val schemaUri = MutableLiveData<Uri>()

    fun setUri(uri:Uri) {
        schemaUri.value = uri
    }
    fun back(view: View){
            Navigation.findNavController(view).popBackStack()
    }
}