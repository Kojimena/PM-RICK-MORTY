package com.jimena.pm_l8

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import coil.load
import coil.request.CachePolicy
import coil.transform.CircleCropTransformation

class Characters : Fragment(R.layout.fragment_characters) {

    private lateinit var image : ImageView
    private lateinit var name : TextView
    private lateinit var species : TextView
    private lateinit var status : TextView
    private lateinit var gender : TextView

    private val args: CharactersArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        image = view.findViewById(R.id.imageView_characters_fragment)
        name = view.findViewById(R.id.nameCharacter_characters_fragment)
        species = view.findViewById(R.id.textRace_characters_fragment)
        status = view.findViewById(R.id.textAliveDeath_characters_fragment)
        gender = view.findViewById(R.id.textMaleFemale_characters_fragment)

        setImage()
        setInfo()
    }

    private fun setImage() {
        image.load(args.characterInfo.image) {
            transformations(CircleCropTransformation())
            diskCachePolicy(CachePolicy.ENABLED)
            memoryCachePolicy(CachePolicy.ENABLED)
            error(R.drawable.ic_error)
            placeholder(R.drawable.ic_descarga)
        }
    }

    private fun setInfo() {
        name.text = args.characterInfo.name
        species.text = args.characterInfo.species
        status.text = args.characterInfo.status
        gender.text = args.characterInfo.gender
    }
}