package com.example.applicationstb.ui.pointage

import android.app.Application
import android.os.Build
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.Navigation
import com.example.applicationstb.model.Pointage
import com.example.applicationstb.repository.Repository
import com.example.applicationstb.ui.accueil.AccueilDirections
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class PointageViewModel(application: Application) : AndroidViewModel(application) {
    var repository = Repository(getApplication<Application>().applicationContext);

    init {
        viewModelScope.launch(Dispatchers.IO) {
            repository.createDb()
        }
    }

    var token: String? = null;
    var pointages = MutableLiveData<MutableList<Pointage>>()
    var total = MutableLiveData<Long>(0)

    @RequiresApi(Build.VERSION_CODES.O)
    fun getListePointages() = viewModelScope.launch(Dispatchers.IO) {
        var list = async {repository.getAllPointageLocalDatabase()}.await().map { it.toPointage() }.toMutableList()
        var t = 0L
        for (i in 1..31) {
            if (list.filter { it.timestamp.dayOfMonth == i }.size > 1) {
                var daylist = list.filter { it.timestamp.dayOfMonth == i }
                if ( daylist.size % 2 == 0) {
                    for (j in 0..daylist.size-1 step 2){
                        t = t + Duration.between(daylist[j].timestamp!!,daylist[j+1].timestamp!!).toHours()
                    }
                } else {
                    for (j in 0..daylist.size-2 step 2){
                         t = t + Duration.between(daylist[j].timestamp!!,daylist[j+1].timestamp!!).toHours()
                    }
                }

            }
        }
        total.postValue(t)
        list.sortBy { it.timestamp }
        pointages.postValue(list)
    }

    fun toAccueil(view: View) {
        Navigation.findNavController(view).popBackStack()
    }

}