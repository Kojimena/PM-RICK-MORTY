package com.jimena.pm_l8

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.room.Room
import coil.load
import coil.request.CachePolicy
import coil.transform.CircleCropTransformation
import kotlin.properties.Delegates
import com.jimena.pm_l8.datasource.api.RetrofitInstance
import com.jimena.pm_l8.datasource.local.Database
import com.jimena.pm_l8.datasource.model.CharacterDto
import com.jimena.pm_l8.datasource.model.DataCharacters
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Characters : Fragment(R.layout.fragment_characters) {

    private lateinit var image : ImageView
    private lateinit var name : EditText
    private lateinit var species : EditText
    private lateinit var status : EditText
    private lateinit var gender : EditText
    private lateinit var origin : EditText
    private lateinit var epAppearances: EditText
    private var idchar by Delegates.notNull<Int>()
    private lateinit var database: Database
    private lateinit var characters: DataCharacters
    private lateinit var buttonGuardar: Button
    private lateinit var resultAPI : CharacterDto


    private val args: CharactersArgs by navArgs()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        image = view.findViewById(R.id.imageView_characters_fragment)
        name = view.findViewById(R.id.nameCharacterEditText_characters_fragment)
        species = view.findViewById(R.id.textSpeciesEditText_characters_fragment)
        status = view.findViewById(R.id.textStatusEditText_characters_fragment)
        gender = view.findViewById(R.id.textGenderEditText_characters_fragment)
        origin = view.findViewById(R.id.textOriginEditText_characters_fragment)
        epAppearances = view.findViewById(R.id.episodeaperancesEditText_characters_fragment)
        buttonGuardar = view.findViewById(R.id.saveButton_characters_fragment)
        idchar = args.characterId

        database = Room.databaseBuilder(
            requireContext(),
            Database::class.java,
            "dataBaseCharacters"
        ).build()

        setListeners()
        loadUser() //llamamos a la funcion para cargar los datos del usuario
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("NotifyDataSetChanged")
    private fun setListeners() {
        //ordenar lista por medio del toolbar
        (activity as MainActivity).toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.menu_item_eliminar -> {
                    val builder = AlertDialog.Builder(requireContext())
                    builder.apply {
                        setTitle("Warning")
                        setMessage("Are you sure you want to delete this character?")
                        setPositiveButton("Yes"
                        ) { _, _ ->
                               CoroutineScope(Dispatchers.IO).launch {
                                    database.characterDao().delete(characters)
                                }
                        }
                        setNegativeButton("Cancel") { _, _ -> }
                        show()
                    }
                    true
                }

                R.id.menu_item_sincronizar-> {
                    val builder = AlertDialog.Builder(requireContext())
                    builder.apply {
                        setTitle("Warning")
                        setMessage("Are you sure you want to synchronize this character?")
                        setPositiveButton("Yes"
                        ) { _, _ ->
                            CoroutineScope(Dispatchers.IO).launch {
                               apiReq()
                            }
                        }
                        setNegativeButton("Cancel") { _, _ -> }
                        show()
                    }
                    true
                }
                else -> true
            }
        }
        buttonGuardar.setOnClickListener{
            getEditData()
        }
    }


    private fun loadUser() {
        //llamamos a la funcion para cargar los datos del usuario
        CoroutineScope(Dispatchers.IO).launch {
            characters = database.characterDao().getCharacterById(idchar)
            CoroutineScope(Dispatchers.Main).launch {
                setChanges() //llamamos a la funcion para aplicar los cambios
            }
        }
    }

    private fun setChanges() {
        image.load(characters.image) {
            transformations(CircleCropTransformation())
            diskCachePolicy(CachePolicy.ENABLED)
            memoryCachePolicy(CachePolicy.ENABLED)
            error(R.drawable.ic_error)
            placeholder(R.drawable.ic_descarga)
        }
        name.setText(characters.name)
        species.setText(characters.species)
        status.setText(characters.status)
        gender.setText(characters.gender)
        origin.setText(characters.origin)
        epAppearances.setText(characters.episode.toString())
    }


    private fun apiReq() {

        RetrofitInstance.api.getidCharacter(idchar).enqueue(object : Callback<CharacterDto> {
                override fun onResponse(
                    call: Call<CharacterDto>,
                    response: Response<CharacterDto>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        resultAPI = response.body()!!
                        setChangesApi()
                    }
                }

                override fun onFailure(call: Call<CharacterDto>, t: Throwable) {
                    Toast.makeText(requireContext(),"No tiene internet", Toast.LENGTH_SHORT).show()
                }

        })
    }


    private fun getEditData() {
        characters.name = name.text.toString()
        characters.species = species.text.toString()
        characters.gender = gender.text.toString()
        characters.origin = origin.text.toString()
        characters.status = status.text.toString()
        characters.episode = epAppearances.text.toString().toIntOrNull()
        CoroutineScope(Dispatchers.IO).launch {
            database.characterDao().update(characters)
            CoroutineScope(Dispatchers.Main).launch {
                Toast.makeText(requireContext(), "Guardado correctamente", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setChangesApi(){
        image.load(resultAPI.image) {
            transformations(CircleCropTransformation())
            diskCachePolicy(CachePolicy.ENABLED)
            memoryCachePolicy(CachePolicy.ENABLED)
            error(R.drawable.ic_error)
            placeholder(R.drawable.ic_descarga)
        }
        name.setText(resultAPI.name)
        species.setText(resultAPI.species)
        status.setText(resultAPI.status)
        gender.setText(resultAPI.gender)
        origin.setText(resultAPI.origin.name)
        epAppearances.setText(resultAPI.episode.size.toString())
        getEditData()
    }



}