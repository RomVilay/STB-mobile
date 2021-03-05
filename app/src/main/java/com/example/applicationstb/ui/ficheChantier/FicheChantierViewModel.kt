package com.example.applicationstb.ui.ficheChantier

import android.media.Image
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import com.example.applicationstb.model.Chantier
import com.example.applicationstb.model.Client
import com.example.applicationstb.model.User
import com.example.applicationstb.model.Vehicule
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
class FicheChantierViewModel : ViewModel() {
    var listeChantiers = arrayListOf<Chantier>()
    var client = Client(0,"Dupond ets.",3369077543,"8 rue truc, 31000 Toulouse")
    var listeTechs = arrayOf<User>(User(0,"Dumont","Toto",1,"toto","toto"),
        User(0,"Dumont","Tom",1,"tom","tom"))
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
                "Obs"
            )
            listeChantiers.add(chantier);
            i++;
        }

    }

    // TODO: Implement the ViewModel
}