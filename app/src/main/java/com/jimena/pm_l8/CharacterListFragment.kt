package com.jimena.pm_l8


import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jimena.pm_l8.adapters.CharacterAdapter
import com.jimena.pm_l8.datasource.api.RetrofitInstance
import com.jimena.pm_l8.datasource.model.AllAssetsResponse
import com.jimena.pm_l8.datasource.model.CharacterDto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.jimena.pm_l8.DataStore.dataStore


class CharacterListFragment : Fragment(R.layout.fragment_character_list), CharacterAdapter.CharacterListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var charList: MutableList<CharacterDto>




    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerList_recyclerActivity)

        setListeners()
        apireq()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setListeners() {
        //ordenar lista por medio del toolbar
        (activity as MainActivity).toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.menu_item_orderaz -> {
                    charList.sortBy { character ->  character.name }
                    recyclerView.adapter?.notifyDataSetChanged()
                    true
                }
                R.id.menu_item_orderza -> {
                    charList.sortByDescending { character ->  character.name }
                    recyclerView.adapter?.notifyDataSetChanged()
                    true
                }
                R.id.menu_item_cerrar_sesion -> {
                    clearDataStore()
                    true

                }
                else -> false

            }
        }

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


    private fun setupRecycler() {
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = CharacterAdapter(charList, this)
    }

    override fun onPlaceClicked(data: CharacterDto, position: Int) {
        requireView().findNavController().navigate(
            CharacterListFragmentDirections.actionCharacterListFragmentToCharacters(
                characterId = data.id
            )
        )
    }

    private fun apireq() {
        RetrofitInstance.api.getCharacter().enqueue(object : Callback<AllAssetsResponse> {
            override fun onResponse(
                call: Call<AllAssetsResponse>,
                response: Response<AllAssetsResponse>
            ) {
                if (response.isSuccessful && response.body() != null){
                    charList = response.body()!!.results as MutableList<CharacterDto>
                    setupRecycler()
                }
            }

            override fun onFailure(call: Call<AllAssetsResponse>, t: Throwable) {
                println("Error")
            }

        })

    }
}