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
    fun getListePointages() =  viewModelScope.launch(Dispatchers.IO){
        var list = repository.getAllPointageLocalDatabase(LocalDateTime.now().format(
            DateTimeFormatter.ofPattern("yyyy-MM"))+"%")
        Log.i("info","année mois : ${LocalDateTime.now().format(
            DateTimeFormatter.ofPattern("yyyy-MM"))}")
        var list2 = mutableListOf<Pointage>()
        var t = 0L
        list.forEach {
            list2.add(it.toPointage())
            t = t + Duration.between(it.dateDebut,it.dateFin).toHours()
        }
        total.postValue(t)
        pointages.postValue(list2)
    }
    fun toAccueil(view: View) {
        Navigation.findNavController(view).popBackStack()
    }

}