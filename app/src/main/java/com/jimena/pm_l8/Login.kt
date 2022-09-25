package com.jimena.pm_l8

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.navigation.findNavController
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


class Login: Fragment(R.layout.fragment_login) {

    private lateinit var ButtonLogin: Button
    private lateinit var InputLayouEmail: TextInputLayout
    private lateinit var InputLayouPassword: TextInputLayout
    private lateinit var NewUser: TextView
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ButtonLogin = view.findViewById(R.id.iniciosesion)
        InputLayouEmail = view.findViewById(R.id.correoLogin)
        InputLayouPassword = view.findViewById(R.id.passworduser)
        NewUser = view.findViewById(R.id.NewuserLogin)
        setListeners()
    }

    private fun setListeners() {
        (activity as MainActivity).toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.menu_item_cerrar_sesion -> {
                    // eliminar datos de sesión
                    CoroutineScope(Dispatchers.IO).launch {
                        context?.dataStore?.edit { settings ->
                            settings.remove(stringPreferencesKey("email"))
                            settings.remove(stringPreferencesKey("password"))
                        }
                    }
                    true
                }
                else -> false
            }

        }
        ButtonLogin.setOnClickListener {
            val text = "Correo o contraseña incorrectos"
            val duration = Toast.LENGTH_SHORT
            if (InputLayouEmail.editText?.text.toString() == "Her21199@uvg.edu.gt" && InputLayouPassword.editText?.text.toString() == "Her21199@uvg.edu.gt") {
                val action = LoginDirections.actionLoginToCharacterListFragment()
                requireView().findNavController().navigate(action)
                val key = InputLayouEmail.editText!!.text.toString()
                CoroutineScope(Dispatchers.IO).launch {
                    saveKeyValue(
                        key = key,
                        value = InputLayouPassword.editText!!.text.toString()
                    )

                    CoroutineScope(Dispatchers.Main).launch {
                        InputLayouEmail.editText!!.text.clear()
                        InputLayouPassword.editText!!.text.clear()

                        Toast.makeText(
                            requireContext(),
                            "Datos guardados",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
            else {
                Toast.makeText(context, text, duration).show()
            }
        }
    }

    private suspend fun saveKeyValue(key: String, value: String) {
        val dataStoreKey = stringPreferencesKey(key)
        context?.dataStore?.edit { settings ->
            settings[dataStoreKey] = value
        }
    }

    private suspend fun getValueFromKey(key: String) : String? {
        val dataStoreKey = stringPreferencesKey(key)
        val preferences = context?.dataStore?.data?.first()
        return preferences?.get(dataStoreKey)
    }


}