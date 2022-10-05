package com.jimena.pm_l8


import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.jimena.pm_l8.adapters.CharacterAdapter
import com.jimena.pm_l8.datasource.api.RetrofitInstance
import com.jimena.pm_l8.datasource.model.AllAssetsResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.jimena.pm_l8.DataStore.dataStore
import com.jimena.pm_l8.datasource.local.Database
import com.jimena.pm_l8.datasource.model.DataCharacters

//Clase que se encarga de mostrar la lista de personajes

class CharacterListFragment : Fragment(R.layout.fragment_character_list), CharacterAdapter.CharacterListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var charList: MutableList<DataCharacters>
    private  var data : MutableList<DataCharacters> = mutableListOf()
    private lateinit var database: Database
    private lateinit var progressBar: ProgressBar



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerList_recyclerActivity)
        progressBar = view.findViewById(R.id.progress_fragmentCharacterList)
        data = mutableListOf()


        database = Room.databaseBuilder( //Accedemos a la base de datos
            requireContext(),
            Database::class.java,
            "dataBaseCharacters"
        ).build()

        setListeners() //Llamamos a la función que contiene los listeners
        setRoomData() //Llamamos a la función que contiene la información de la base de datos
        setupRecycler() //Llamamos a la función que contiene el recycler
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setListeners() {
        //ordenar lista por medio del toolbar
        (activity as MainActivity).toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.menu_item_orderaz -> { //Ordenar de la A a la Z
                    charList.sortBy { character ->  character.name }
                    recyclerView.adapter?.notifyDataSetChanged()
                    true
                }
                R.id.menu_item_orderza -> { //Ordenar de la Z a la A
                    charList.sortByDescending { character ->  character.name }
                    recyclerView.adapter?.notifyDataSetChanged()
                    true
                }
                R.id.menu_item_cerrar_sesion -> { //Cerrar sesión
                    clearDataStore() //Llamamos a la función que borra el token
                    true

                }
                R.id.menu_item_sincronizar -> { //sincronizar con la api
                    data.clear() //Limpiamos la lista
                    apireq() //Llamamos a la función que contiene la información de la api
                    Toast.makeText(requireContext(), "Data sincronizada", Toast.LENGTH_SHORT).show()

                    true
                }
                else -> false

            }
        }

    }

    private fun setRoomData(){
        progressBar.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE

        CoroutineScope(Dispatchers.IO).launch { //Accedemos a la base de datos
            val users = database.characterDao().getAllCharacters()
            data.addAll(users) //Añadimos los datos a la lista
            CoroutineScope(Dispatchers.Main).launch {
                progressBar.visibility = View.GONE
                recyclerView.visibility = View.VISIBLE
                recyclerView.adapter?.notifyDataSetChanged() //Notificamos al adaptador que los datos han cambiado
                //verificamos si la lista está vacía
                if (data.isEmpty()) {
                    apireq() //Si está vacía, se hace la petición a la api
                }
            }
        }
    }

    private fun setupRecycler() { //Configuramos el recycler
        charList = data
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true) //Para que el tamaño del recycler no cambie
        recyclerView.adapter = CharacterAdapter(charList, this) //Le pasamos la lista y el listener
        recyclerView.visibility = View.VISIBLE

    }

    private fun clearDataStore() {
        CoroutineScope(Dispatchers.IO).launch {
            clearKeyValue(
                key = getString(R.string.keyIniciosession)
            )
        }

        requireView().findNavController().navigate(
            CharacterListFragmentDirections.actionCharacterListFragmentToLogin()
        )
    }

    private suspend fun clearKeyValue(key: String) {
        val dataStoreKey = stringPreferencesKey(key)
        context?.dataStore?.edit { settings ->
            settings.remove(dataStoreKey)
        }
    }



    private fun apireq() {
        RetrofitInstance.api.getCharacter().enqueue(object : Callback<AllAssetsResponse> {
            override fun onResponse(
                call: Call<AllAssetsResponse>,
                response: Response<AllAssetsResponse>
            ) {
                if (response.isSuccessful && response.body() != null){
                    for (character in response.body()!!.results){
                        val character = DataCharacters(
                            name = character.name,
                            species = character.species,
                            id = character.id,
                            status = character.status,
                            gender = character.gender,
                            origin = character.origin.name,
                            episode = character.episode.size,
                            image = character.image
                        )
                        CoroutineScope(Dispatchers.IO).launch {
                            database.characterDao().createCharacter(character)
                        }
                        data.add(character)
                    }
                    CoroutineScope(Dispatchers.Main).launch {
                        charList = data
                        notifyDataChange()
                    }
                }
            }

            @SuppressLint("NotifyDataSetChanged")
            private fun notifyDataChange() {
                recyclerView.adapter!!.notifyDataSetChanged()
                progressBar.visibility = View.GONE
            }

            override fun onFailure(call: Call<AllAssetsResponse>, t: Throwable) {
                Toast.makeText(requireContext(), "Error, no tiene internet", Toast.LENGTH_SHORT).show()
            }
        })

    }

    override fun onPlaceClicked(data: DataCharacters, position: Int) {
        requireView().findNavController().navigate(
            CharacterListFragmentDirections.actionCharacterListFragmentToCharacters(
                characterId = data.id
            )
        )
    }
}