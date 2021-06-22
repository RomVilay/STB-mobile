package com.example.applicationstb.model

class Remontage (
    numDevis: String,
    numChantier: String,
    client: Client,
    contact: String,
    telContact: Long,
    techniciens: Array<User>,
    resp: User
        ) :Fiche(numDevis, numChantier, client, contact, telContact, techniciens, resp){
}