package com.example.applicationstb.ui.ficheBobinage

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.SystemClock
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import androidx.core.view.marginTop
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.applicationstb.R
import com.example.applicationstb.model.Bobinage
import com.example.applicationstb.model.Section
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.File
import java.io.IOException
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*


class FicheBobinage : Fragment() {

    companion object {
        fun newInstance() = FicheBobinage()
    }

    private val viewModel: FicheBobinageViewModel by activityViewModels()
    private lateinit var recycler: RecyclerView
    private lateinit var schemas: RecyclerView
    private val PHOTO_RESULT = 1888
    lateinit var currentPhotoPath: String
    val REQUEST_IMAGE_CAPTURE = 1

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.i(
            "INFO",
            "tableau fiches: ${arguments?.get("listBobinage")} - token: ${arguments?.get("token")} "
        )
        var list = arguments?.get("listBobinage") as Array<Bobinage>
        viewModel.token = arguments?.get("token") as String
        viewModel.listeBobinage = list.toCollection(ArrayList())
        viewModel.username = arguments?.get("username") as String
        var layout = inflater.inflate(R.layout.fiche_bobinage_fragment, container, false)
        var scrollBobi = layout.findViewById<ScrollView>(R.id.scrollBobi)
        //viewModel = ViewModelProvider(this).get(FicheBobinageViewModel::class.java)

        //champs détails
        val spinner = layout.findViewById<Spinner>(R.id.numDevis)
        val adapterDevis = ArrayAdapter(
            requireActivity(),
            R.layout.support_simple_spinner_dropdown_item,
            viewModel.listeBobinage.map { it.numFiche })
        spinner.adapter = adapterDevis
        var btnSelect = layout.findViewById<Button>(R.id.btnDemarrer)
        var non = layout.findViewById<TextView>(R.id.non)
        var oui = layout.findViewById<TextView>(R.id.oui)
        var frequence = layout.findViewById<EditText>(R.id.frequence)
        var client = layout.findViewById<EditText>(R.id.client)
        var puissance = layout.findViewById<EditText>(R.id.puissance)
        var courant = layout.findViewById<EditText>(R.id.courant)
        var phases = layout.findViewById<EditText>(R.id.phase)
        var vitesse = layout.findViewById<EditText>(R.id.vitesse)
        var type = layout.findViewById<EditText>(R.id.type)
        var marque = layout.findViewById<EditText>(R.id.marque)
        var switch = layout.findViewById<Switch>(R.id.callage)
        var dates = layout.findViewById<LinearLayout>(R.id.dates)
        var dated = layout.findViewById<TextView>(R.id.DateDebut)
        var details = layout.findViewById<TextView>(R.id.titreDetails)
        var som = layout.findViewById<TextView>(R.id.somme)
        val adapter = FillAdapter(viewModel.sections!!.value!!, { diametre, nb, position ->
            viewModel.sections.value?.set(position, Section(nb, diametre))
            var s = viewModel.somme(viewModel.sections.value!!)
            som.setText(s.toString())
            Log.i("info", "nbBrins " + nb)
            viewModel.quickSave()

        })
        val sAdapter = schemaAdapter(viewModel.photos.value!!, { item ->
            viewModel.setSchema(item)
            viewModel.fullScreen(
                layout,
                "/storage/emulated/0/Pictures/test_pictures/" + item.toString()
            )
        })
        var visibility = View.VISIBLE
        //champs fils
        var linscroll = layout.findViewById<LinearLayout>(R.id.linscroll)
        var btnfils = layout.findViewById<Button>(R.id.ajoutFil)
        var diam = layout.findViewById<EditText>(R.id.diam)
        var nbBrins = layout.findViewById<EditText>(R.id.nbfils)
        var poids = layout.findViewById<EditText>(R.id.poids)
        var RU = layout.findViewById<EditText>(R.id.RU)
        var RV = layout.findViewById<EditText>(R.id.RV)
        var RW = layout.findViewById<EditText>(R.id.RW)
        var IU = layout.findViewById<EditText>(R.id.IU)
        var IV = layout.findViewById<EditText>(R.id.IV)
        var IW = layout.findViewById<EditText>(R.id.IW)
        var IIU = layout.findViewById<EditText>(R.id.IIU)
        var IIV = layout.findViewById<EditText>(R.id.IIV)
        var IIW = layout.findViewById<EditText>(R.id.IIW)
        var addschema = layout.findViewById<Button>(R.id.addschema)
        var tension = layout.findViewById<EditText>(R.id.tension)
        var obs = layout.findViewById<EditText>(R.id.observations)
        var spire = layout.findViewById<EditText>(R.id.spire)
        var enrg = layout.findViewById<Button>(R.id.enregistrer)
        var quit = layout.findViewById<Button>(R.id.quit)
        var term = layout.findViewById<Button>(R.id.termB)
        var regexNombres = Regex("^\\d*\\.?\\d*\$")
        var regexInt = Regex("^\\d+")
        var paspolaire = layout.findViewById<EditText>(R.id.paspolaire)
        var checksonde = layout.findViewById<CheckBox>(R.id.checkSonde)
        var typesonde = layout.findViewById<EditText>(R.id.typeSondeB)
        var branchement = layout.findViewById<Spinner>(R.id.branchement)
        branchement.adapter = ArrayAdapter<String>(
            requireContext(),
            R.layout.support_simple_spinner_dropdown_item,
            arrayOf<String>("", "parallèle", "demi-parallèle", "serie", "autre")
        )
        var typeBranchement = layout.findViewById<EditText>(R.id.typeBranchement)
        var nbEncoches = layout.findViewById<EditText>(R.id.nbEncoches)
        if (activity?.let {
                ContextCompat.checkSelfPermission(
                    it.applicationContext,
                    Manifest.permission.CAMERA
                )
            }
            == PackageManager.PERMISSION_DENIED)
            this.activity?.let {
                ActivityCompat.requestPermissions(
                    it,
                    arrayOf(Manifest.permission.CAMERA),
                    PHOTO_RESULT
                )
            }
        recycler = layout.findViewById(R.id.tab)
        recycler.layoutManager = GridLayoutManager(context, 1)
        recycler.adapter = adapter
        viewModel.sections.observe(viewLifecycleOwner, {
            adapter.update(it)
        })

        schemas = layout.findViewById(R.id.schemas)
        schemas.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        schemas.adapter = sAdapter
        viewModel.photos.observe(viewLifecycleOwner, {
            if (viewModel.photos.value !== null) {
                sAdapter.update(viewModel.photos.value!!)
                if (viewModel.photos.value?.size == 0) {
                    schemas.visibility = View.GONE
                } else {
                    schemas.visibility = View.VISIBLE
                }
            } else {
                viewModel.photos.value = mutableListOf()
            }
        })

        /*var format = DateTimeFormatter.ofPattern("DD-MM-YYYY")
        dateDebut.setText(LocalDateTime.now().format(format))*/

        btnSelect.setOnClickListener {
            if (spinner.selectedItem == null) {
                val mySnackbar = Snackbar.make(
                    layout.findViewById<CoordinatorLayout>(R.id.FicheBobinageLayout),
                    "Veuillez sélectionner une fiche",
                    3600
                )
                mySnackbar.show()
            } else {
                scrollBobi.visibility = View.VISIBLE
                viewModel.start.value = Date()
                var bobinage = viewModel.listeBobinage.find { it.numFiche == spinner.selectedItem }
                viewModel.bobinage.value = bobinage
                viewModel.bobinage.value!!.status = 2
                viewModel.sections.value = bobinage?.sectionsFils
                viewModel.photos.value = bobinage?.photos!!.toMutableList()
                if (bobinage != null) {
                    if (viewModel.photos.value !== null) sAdapter.update(viewModel.photos.value!!)
                    marque.setText(bobinage?.marqueMoteur)
                    if (bobinage.courant !== null) {
                        courant.setText(bobinage?.courant.toString())
                    } //
                    if (bobinage.vitesse !== null) {
                        vitesse.setText(bobinage?.vitesse.toString())
                    } //
                    type.setText(bobinage?.typeBobinage)
                    client.setText(bobinage?.client?.enterprise)
                    if (bobinage.puissance !== null) {
                        puissance.setText(bobinage?.puissance.toString())
                    } //
                    if (bobinage.phases !== null) {
                        phases.setText(bobinage?.phases.toString())
                    } //
                    if (bobinage.frequences !== null) {
                        frequence.setText(bobinage?.frequences.toString())
                    } //
                    if (bobinage.calageEncoches !== null) {
                        switch.setChecked(bobinage.calageEncoches!!)
                    } else switch.setChecked(false)
                    if (bobinage.tension !== null) tension.setText(bobinage?.tension.toString())
                    adapter.list = bobinage.sectionsFils!!
                    if (bobinage.nbSpires !== null) {
                        spire.setText(bobinage?.nbSpires.toString())
                    }
                    if (bobinage.poids !== null) poids.setText(bobinage?.poids.toString())
                    if (bobinage.resistanceU !== null) {
                        RU.setText(bobinage?.resistanceU.toString())
                    }
                    if (bobinage.resistanceV !== null) RV.setText(bobinage?.resistanceV.toString())
                    if (bobinage.resistanceW !== null) RW.setText(bobinage?.resistanceW.toString())
                    if (bobinage.isolementUT !== null) IU.setText(bobinage?.isolementUT.toString())
                    if (bobinage.isolementVT !== null) IV.setText(bobinage?.isolementVT.toString())
                    if (bobinage.isolementWT !== null) IW.setText(bobinage?.isolementWT.toString())
                    if (bobinage.isolementUV !== null) IIU.setText(bobinage?.isolementUV.toString())
                    if (bobinage.isolementUW !== null) IIV.setText(bobinage?.isolementUW.toString())
                    if (bobinage.isolementVW !== null) IIW.setText(bobinage?.isolementVW.toString())
                    if (bobinage.observations !== null) obs.setText(bobinage?.observations.toString())
                    if (bobinage.pasPolaire !== null) paspolaire.setText(bobinage?.pasPolaire.toString())
                    if (bobinage.presenceSondes !== null && bobinage?.presenceSondes!!) checksonde.isChecked =
                        true
                    if (bobinage.typeSondes !== null && bobinage.presenceSondes == true) {
                        typesonde.setText(bobinage.typeSondes.toString())
                        typesonde.visibility = View.VISIBLE
                    }
                    if (bobinage.branchement !== null) branchement.setSelection(
                        arrayOf<String>(
                            "",
                            "parallèle",
                            "demi-parallèle",
                            "serie",
                            "autre"
                        ).indexOf(bobinage.branchement!!)
                    )
                    if (arrayOf<String>(
                            "",
                            "parallèle",
                            "demi-parallèle",
                            "serie"
                        ).indexOf(bobinage.branchement!!) == -1
                    ) {
                        typeBranchement.visibility = View.VISIBLE
                        typeBranchement.setText(bobinage.branchement.toString())
                    }
                    if (bobinage.nbEncoches !== null) nbEncoches.setText(bobinage.nbEncoches.toString())
                    var formater = DateTimeFormatter.ofPattern("DD-MM-YYYY HH:mm")
                    if (bobinage.dateDebut !== null) dated.setText(bobinage?.dateDebut!!.toLocaleString())
                }
                //viewModel.selectBobinage(bobinage!!._id)
                /*var format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")
            dated.setText(LocalDateTime.now().format(format))*/
            }
        }

        details.setOnClickListener {
            if (visibility == View.GONE) {
                visibility = View.VISIBLE
                details.setText("masquer les détails")
            } else {
                visibility = View.GONE
                details.setText("afficher les détails")
            }
            non.visibility = visibility
            client.visibility = visibility
            oui.visibility = visibility
            frequence.visibility = visibility
            puissance.visibility = visibility
            courant.visibility = visibility
            phases.visibility = visibility
            vitesse.visibility = visibility
            type.visibility = visibility
            marque.visibility = visibility
            dates.visibility = visibility
            tension.visibility = visibility
            switch.visibility = visibility
            poids.visibility = visibility
            Log.i("INFO", "change")
        }
        btnfils.setOnClickListener {
            if (nbBrins.text.isNotEmpty() && diam.text.isNotEmpty()) {
                viewModel.addSection(
                    nbBrins.text.toString().toLong(),
                    diam.text.toString().toDouble()
                )
                var s = viewModel.somme(viewModel.sections.value!!)
                som.setText(s.toString())
                viewModel.quickSave()
            } else {
                val mySnackbar = Snackbar.make(
                    layout,
                    "Veuillez préciser le nombre de brins et le diamètre de la section",
                    3600
                )
                mySnackbar.show()
            }
        }
        addschema.setOnClickListener {
            runBlocking {
                lifecycleScope.launch(Dispatchers.IO) {
                    // var job = CoroutineScope(Dispatchers.IO).launch {
                    if (viewModel.isOnline(requireContext())) {
                        viewModel.getNameURI()
                    }
                    //}
                    //job.join()
                    var test = ActivityCompat.checkSelfPermission(
                        requireContext(),
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ) == PackageManager.PERMISSION_GRANTED
                    if (!test) {
                        requestPermissions(
                            arrayOf(
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE
                            ), 1
                        )
                    }
                    val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { cameraIntent ->
                        // Ensure that there's a camera activity to handle the intent
                        cameraIntent.resolveActivity(requireActivity().packageManager).also {
                            // Create the File where the photo should go
                            val photoFile: File? = try {
                                createImageFile()
                            } catch (ex: IOException) {
                                // Error occurred while creating the File
                                Log.i("INFO", "error while creating file")
                                null
                            }
                            // Continue only if the File was successfully created
                            photoFile?.also {
                                val photoURI: Uri = FileProvider.getUriForFile(
                                    requireContext(),
                                    "com.example.applicationstb.fileprovider",
                                    it
                                )
                                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                                startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE)
                            }
                        }
                    }
                }
            }
        }
        marque.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                var margins = linscroll.layoutParams as ViewGroup.MarginLayoutParams
                margins.topMargin = 220
                linscroll.layoutParams = margins
            } else {
                var margins = linscroll.layoutParams as ViewGroup.MarginLayoutParams
                margins.topMargin = 0
                linscroll.layoutParams = margins
            }
        }
        courant.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                var margins = linscroll.layoutParams as ViewGroup.MarginLayoutParams
                margins.topMargin = 220
                linscroll.layoutParams = margins
            } else {
                var margins = linscroll.layoutParams as ViewGroup.MarginLayoutParams
                margins.topMargin = 0
                linscroll.layoutParams = margins
            }
        }
        client.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                var margins = linscroll.layoutParams as ViewGroup.MarginLayoutParams
                margins.topMargin = 220
                linscroll.layoutParams = margins
            } else {
                var margins = linscroll.layoutParams as ViewGroup.MarginLayoutParams
                margins.topMargin = 0
                linscroll.layoutParams = margins
            }
        }
        type.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                var margins = linscroll.layoutParams as ViewGroup.MarginLayoutParams
                margins.topMargin = 220
                linscroll.layoutParams = margins
            } else {
                var margins = linscroll.layoutParams as ViewGroup.MarginLayoutParams
                margins.topMargin = 0
                linscroll.layoutParams = margins
            }
        }
        vitesse.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                var margins = linscroll.layoutParams as ViewGroup.MarginLayoutParams
                margins.topMargin = 220
                linscroll.layoutParams = margins
            } else {
                var margins = linscroll.layoutParams as ViewGroup.MarginLayoutParams
                margins.topMargin = 0
                linscroll.layoutParams = margins
            }
        }
        phases.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                var margins = linscroll.layoutParams as ViewGroup.MarginLayoutParams
                margins.topMargin = 220
                linscroll.layoutParams = margins
            } else {
                var margins = linscroll.layoutParams as ViewGroup.MarginLayoutParams
                margins.topMargin = 0
                linscroll.layoutParams = margins
            }
        }
        frequence.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                var margins = linscroll.layoutParams as ViewGroup.MarginLayoutParams
                margins.topMargin = 220
                linscroll.layoutParams = margins
            } else {
                var margins = linscroll.layoutParams as ViewGroup.MarginLayoutParams
                margins.topMargin = 0
                linscroll.layoutParams = margins
            }
        }
        puissance.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                var margins = linscroll.layoutParams as ViewGroup.MarginLayoutParams
                margins.topMargin = 220
                linscroll.layoutParams = margins
            } else {
                var margins = linscroll.layoutParams as ViewGroup.MarginLayoutParams
                margins.topMargin = 0
                linscroll.layoutParams = margins
            }
        }
        marque.doAfterTextChanged {
            if (marque.text.isNotEmpty() && marque.hasFocus()) {
                viewModel.bobinage.value!!.marqueMoteur = marque.text.toString()
                viewModel.quickSave()
            }
        }
        courant.doAfterTextChanged {
            if (courant.text.isNotEmpty() && courant.text.matches(regexNombres)) viewModel.bobinage.value!!.courant =
                courant.text.toString().replace(",", ".").toFloat()
            viewModel.quickSave()
        }
        vitesse.doAfterTextChanged {
            if (vitesse.text.isNotEmpty() && vitesse.text.matches(regexNombres)) viewModel.bobinage.value!!.vitesse =
                vitesse.text.toString().replace(",", ".").toFloat()
            viewModel.quickSave()
        }
        type.doAfterTextChanged {
            if (type.text.isNotEmpty()) viewModel.bobinage.value!!.typeBobinage =
                type.text.toString()
            viewModel.quickSave()
        }
        phases.doAfterTextChanged {
            if (phases.text.isNotEmpty() && phases.text.matches(regexInt)) viewModel.bobinage.value!!.phases =
                phases.text.toString().toLong()
            viewModel.quickSave()
        }
        frequence.doAfterTextChanged {
            if (frequence.text.isNotEmpty() && frequence.text.matches(regexNombres)) viewModel.bobinage.value!!.frequences =
                frequence.text.toString().replace(",", ".").toFloat()
            viewModel.quickSave()
        }
        puissance.doAfterTextChanged {
            if (puissance.text.isNotEmpty() && puissance.text.matches(regexNombres)) viewModel.bobinage.value!!.puissance =
                puissance.text.toString().replace(",", ".").toFloat()
            viewModel.quickSave()
        }
        switch.setOnCheckedChangeListener { buttonView, isChecked ->
            viewModel.bobinage.value!!.calageEncoches = isChecked
            viewModel.quickSave()
        }
        tension.doAfterTextChanged {
            if (tension.text.isNotEmpty()) viewModel.bobinage.value!!.tension =
                tension.text.toString()
            Log.i("info", "val tension ${viewModel.bobinage.value!!.tension}")
            viewModel.quickSave()
        }

        spire.doAfterTextChanged {
            if (spire.text.isNotEmpty() && spire.text.matches(regexInt)) viewModel.bobinage.value!!.nbSpires =
                spire.text.toString().toLong()
            viewModel.quickSave()
        }
        poids.doAfterTextChanged {
            if (poids.text.isNotEmpty() && poids.text.matches(regexNombres)) viewModel.bobinage.value!!.poids =
                poids.text.toString().replace(",", ".").toFloat()
            viewModel.quickSave()
        }
        RU.doAfterTextChanged {
            if (RU.text.isNotEmpty() && RU.text.matches(regexNombres)) viewModel.bobinage.value!!.resistanceU =
                RU.text.toString().replace(",", ".").toFloat()
            viewModel.quickSave()
        }
        RV.doAfterTextChanged {
            if (RV.text.isNotEmpty() && RV.text.matches(regexNombres)) viewModel.bobinage.value!!.resistanceV =
                RV.text.toString().replace(",", ".").toFloat()
            viewModel.quickSave()
        }
        RW.doAfterTextChanged {
            if (RW.text.isNotEmpty() && RW.text.matches(regexNombres)) viewModel.bobinage.value!!.resistanceW =
                RW.text.toString().replace(",", ".").toFloat()
            viewModel.quickSave()
        }
        IU.doAfterTextChanged {
            if (IU.text.isNotEmpty() && IU.text.matches(regexNombres)) viewModel.bobinage.value!!.isolementUT =
                IU.text.toString().replace(",", ".").toFloat()
            viewModel.quickSave()
        }
        IV.doAfterTextChanged {
            if (IV.text.isNotEmpty() && IV.text.matches(regexNombres)) viewModel.bobinage.value!!.isolementVT =
                IV.text.toString().replace(",", ".").toFloat()
            viewModel.quickSave()
        }
        IW.doAfterTextChanged {
            if (IW.text.isNotEmpty() && IW.text.matches(regexNombres)) viewModel.bobinage.value!!.isolementWT =
                IW.text.toString().replace(",", ".").toFloat()
            viewModel.quickSave()
        }
        IIU.doAfterTextChanged {
            if (IIU.text.isNotEmpty() && IIU.text.matches(regexNombres)) viewModel.bobinage.value!!.isolementUV =
                IIU.text.toString().replace(",", ".").toFloat()
            viewModel.quickSave()
        }
        IIV.doAfterTextChanged {
            if (IIV.text.isNotEmpty() && IIV.text.matches(regexNombres)) viewModel.bobinage.value!!.isolementUW =
                IIV.text.toString().replace(",", ".").toFloat()
            viewModel.quickSave()
        }
        IIW.doAfterTextChanged {
            if (IIW.text.isNotEmpty() && IIW.text.matches(regexNombres)) viewModel.bobinage.value!!.isolementVW =
                IIW.text.toString().replace(",", ".").toFloat()
            viewModel.quickSave()
        }
        obs.doAfterTextChanged {
            viewModel.bobinage.value!!.observations = obs.text.toString()
            viewModel.quickSave()
        }
        switch.setOnCheckedChangeListener { _, isChecked ->
            viewModel.bobinage.value!!.calageEncoches = isChecked
            viewModel.quickSave()
        }
        quit.setOnClickListener {
            viewModel.back(layout)
        }
        paspolaire.doAfterTextChanged {
            if (paspolaire.hasFocus() && paspolaire.text.isNotEmpty())
            viewModel.bobinage.value!!.pasPolaire = paspolaire.text.toString()
            viewModel.quickSave()
        }
        checksonde.setOnCheckedChangeListener { _, ischeked ->
            if (ischeked){
                viewModel.bobinage.value!!.presenceSondes = true
                typesonde.visibility = View.VISIBLE
                viewModel.quickSave()
            } else {
                viewModel.bobinage.value!!.presenceSondes = false
                viewModel.bobinage.value!!.typeSondes = ""
                typesonde.visibility = View.INVISIBLE
                viewModel.quickSave()
            }
        }
        typesonde.doAfterTextChanged {
            if (typesonde.hasFocus() && typesonde.text.isNotEmpty()) {
                viewModel.bobinage.value!!.typeSondes = typesonde.text.toString()
                viewModel.quickSave()
            }
        }
        branchement.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                    if (viewModel.bobinage.value !== null) {
                        Log.i("info", "branchement: " + branchement.selectedItem.toString())
                        viewModel.bobinage.value!!.branchement = branchement.selectedItem.toString()
                        viewModel.quickSave()
                        if (branchement.selectedItem.toString() == "autre")
                            typeBranchement.visibility = View.VISIBLE
                        else
                            typeBranchement.visibility = View.INVISIBLE
                    }
            }
        }
        typeBranchement.doAfterTextChanged {
            if (typeBranchement.hasFocus() && typeBranchement.isVisible && typeBranchement.text.isNotEmpty()) {
                viewModel.bobinage.value!!.branchement  = typeBranchement.text.toString()
                viewModel.quickSave()
            }
        }
        nbEncoches.doAfterTextChanged {
            if (nbEncoches.hasFocus() && nbEncoches.text.isNotEmpty()) {
                viewModel.bobinage.value!!.nbEncoches = nbEncoches.text.toString().toLong()
                viewModel.quickSave()
            }
        }

        enrg.setOnClickListener {
            viewModel.bobinage.value!!.status = 2L
            viewModel.bobinage.value!!.sectionsFils = viewModel.sections.value
            viewModel.getTime()

            viewModel.save(
                requireContext(),
                layout.findViewById<CoordinatorLayout>(R.id.FicheBobinageLayout)
            )
        }
        term.setOnClickListener {
            viewModel.bobinage.value!!.status = 3L
            viewModel.bobinage.value!!.sectionsFils = viewModel.sections.value
            viewModel.getTime()
            viewModel.save(
                requireContext(),
                layout.findViewById<CoordinatorLayout>(R.id.FicheBobinageLayout)
            )
        }
        return layout
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // TODO: Use the ViewModel
    }

    @RequiresApi(Build.VERSION_CODES.M)
    @Throws(IOException::class)
    private fun createImageFile(): File {
        //  Log.i("INFO","nom utilisé"+viewModel.imageName.value!!.name.toString().removeSuffix(".jpg"))
        // Create an image file name
        val storageDir: File =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + "/test_pictures")
        if (storageDir.exists()) {
            if (viewModel.isOnline(requireContext())) {
                return File.createTempFile(
                    viewModel.imageName.value!!.name.toString().removeSuffix(".jpg"),/* prefix */
                    ".jpg", /* suffix */
                    storageDir /* directory */
                ).apply {
                    // Save a file: path for use with ACTION_VIEW intents
                    currentPhotoPath = absolutePath
                    Log.i("INFO", currentPhotoPath)
                }
            } else {
                return File.createTempFile(
                    viewModel.bobinage.value?.numFiche + "_" + SystemClock.uptimeMillis(),/* prefix */
                    ".jpg", /* suffix */
                    storageDir /* directory */
                ).apply {
                    // Save a file: path for use with ACTION_VIEW intents
                    currentPhotoPath = absolutePath
                }
            }
        } else {
            makeFolder()
            if (viewModel.isOnline(requireContext())) {
                return File.createTempFile(
                    viewModel.imageName.value?.name.toString().removeSuffix(".jpg"),/* prefix */
                    ".jpg", /* suffix */
                    storageDir /* directory */
                ).apply {
                    // Save a file: path for use with ACTION_VIEW intents
                    currentPhotoPath = absolutePath
                    Log.i("INFO", currentPhotoPath)
                }
            } else {
                return File.createTempFile(
                    viewModel.bobinage.value!!.numFiche + "_" + SystemClock.uptimeMillis(),/* prefix */
                    ".jpg", /* suffix */
                    storageDir /* directory */
                ).apply {
                    // Save a file: path for use with ACTION_VIEW intents
                    currentPhotoPath = absolutePath
                    Log.i("INFO", "local photo path:" + currentPhotoPath)
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val dir =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + "/test_pictures")
        if (resultCode < 0 || resultCode > 0) {
            if (requestCode == PHOTO_RESULT) {
                val photo: Bitmap = data?.extras?.get("data") as Bitmap
                val uri = context?.let { photo.saveImage(it.applicationContext) }
                if (uri != null) {
                    Log.i("INFO", "uri:" + uri)
                    viewModel.addPhoto(uri)
                    viewModel.galleryAddPic(uri.path)
                    /*var picture = File(uri.path)
                    try {
                        viewModel.sendPhoto(data?.extras?.get("data") as File)
                    } catch (e: java.lang.Exception) {
                        Log.e("EXCEPTION",e.message!!)
                    }*/
                }
                Log.i("INFO", uri.toString())
            }
            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                //val photo: Bitmap = data?.extras?.get("data") as Bitmap
                //imageView.setImageBitmap(photo)
                val dir =
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + "/test_pictures")
                if (dir.exists()) {
                    if (viewModel.isOnline(requireContext())) {
                        val from = File(
                            dir,
                            currentPhotoPath.removePrefix("/storage/emulated/0/Pictures/test_pictures/")
                        )
                        val to = File(dir, viewModel.imageName.value!!.name)
                        if (from.exists()) from.renameTo(to)
                        try {
                            viewModel.addPhoto(Uri.parse(to.absolutePath))
                            viewModel.galleryAddPic(to.absolutePath)
                            viewModel.sendPhoto(to)
                            viewModel.quickSave()
                        } catch (e: java.lang.Exception) {
                            Log.e("EXCEPTION", e.message!!)
                        }
                    } else {
                        viewModel.addPhoto(Uri.parse(currentPhotoPath.removePrefix("/storage/emulated/0/Pictures/test_pictures/")))
                        viewModel.galleryAddPic(currentPhotoPath)
                        viewModel.quickSave()
                    }
                }
                //viewModel.sendPhoto(data?.extras?.get("data") as File)
            }
        }
    }


    fun makeFolder() {
        val storageDir: File =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + "/test_pictures")
        storageDir.mkdir()
    }

    fun Bitmap.saveImage(context: Context): Uri? {
        //Log.i("INFO",viewModel.imageName.value.toString())
        val values = ContentValues()
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        values.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/test_pictures")
        values.put(MediaStore.Images.Media.IS_PENDING, true)
        values.put(
            MediaStore.Images.Media.DISPLAY_NAME,
            viewModel.imageName.value!!.name.toString()
        )

        val uri: Uri? =
            context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        if (uri != null) {
            saveImageToStream(this, context.contentResolver.openOutputStream(uri))
            values.put(MediaStore.Images.Media.IS_PENDING, false)
            context.contentResolver.update(uri, values, null, null)
            Log.i("INFO", uri.toString())
            return uri
        }
        return null
    }


    fun saveImageToStream(bitmap: Bitmap, outputStream: OutputStream?) {
        if (outputStream != null) {
            try {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                outputStream.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}