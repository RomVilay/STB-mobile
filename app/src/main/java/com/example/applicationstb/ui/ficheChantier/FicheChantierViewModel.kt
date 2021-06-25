package com.example.applicationstb.ui.ficheChantier

import android.graphics.Bitmap
import android.graphics.Path
import android.os.Build
import android.view.View
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.Navigation
import com.example.applicationstb.R
import com.example.applicationstb.model.Chantier
import com.example.applicationstb.model.Client
import com.example.applicationstb.model.User
import com.example.applicationstb.model.Vehicule
import android.net.Uri

class FicheChantierViewModel : ViewModel() {
    var listeChantiers = arrayListOf<Chantier>()
    var client = Client(0,"Dupond ets.",3369077543,"8 rue truc, 31000 Toulouse")
    var signatures = arrayListOf<Uri?>()
    var listeTechs = arrayOf<User>(User("0","Dumont","Toto",1,"toto","toto","0"),
        User("0","Dumont","Tom",1,"tom","tom","0"))
    init {
        var i =0
        while (i<10)
        {
            var chantier = Chantier (i.toString(),
                i.toString() ,
                client,
                "Dupond M.",
                3369077543,
                listeTechs,
                listeTechs[0],
                Vehicule(1,"trafic",230000),
                "2 rue des paquerettes",
                "Lorem ipsum",
                "Lorem ipsum",
                "Lorem ipsum",

            )
            listeChantiers.add(chantier);
            i++;
        }

    }

    fun back(view:View){
        //console.log(signatures)
        val action = FicheChantierDirections.deChantierversAccueil("Token")
        Navigation.findNavController(view).navigate(action)
    }
    /*fun setSignature(sign:Bitmap){
        signature.value = sign
    }*/

    fun save(){

    }

    // TODO: Implement the ViewModel
}