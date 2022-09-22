package com.jimena.pm_l8

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.findNavController
import com.google.android.material.textfield.TextInputLayout


class Login: Fragment(R.layout.fragment_login) {

    private lateinit var ButtonLogin: Button
    private lateinit var InputLayouEmail: TextInputLayout
    private lateinit var NewUser: TextView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ButtonLogin = view.findViewById(R.id.iniciosesion)
        InputLayouEmail = view.findViewById(R.id.correoLogin)
        NewUser = view.findViewById(R.id.NewuserLogin)
        setListeners()
    }

    private fun setListeners() {
        ButtonLogin.setOnClickListener {
            val text = "El correo no existe"
            val duration = Toast.LENGTH_SHORT
            if (InputLayouEmail.editText?.text.toString() == "Her21199@uvg.edu.gt") {
                val action = LoginDirections.actionLoginToCharacterListFragment()
                requireView().findNavController().navigate(action)
            } else {
                Toast.makeText(context, text, duration).show()
            }
        }
    }
}