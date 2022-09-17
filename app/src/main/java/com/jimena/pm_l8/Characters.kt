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
import kotlin.properties.Delegates
import com.jimena.pm_l8.datasource.api.RetrofitInstance
import com.jimena.pm_l8.datasource.model.CharacterDto
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Characters : Fragment(R.layout.fragment_characters) {

    private lateinit var image : ImageView
    private lateinit var name : TextView
    private lateinit var species : TextView
    private lateinit var status : TextView
    private lateinit var gender : TextView
    private var idchar by Delegates.notNull<Int>()
    private lateinit var resultAPI : CharacterDto
    private lateinit var origin : TextView
    private lateinit var epAppearances: TextView

    private val args: CharactersArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        image = view.findViewById(R.id.imageView_characters_fragment)
        name = view.findViewById(R.id.nameCharacter_characters_fragment)
        species = view.findViewById(R.id.textRace_characters_fragment)
        status = view.findViewById(R.id.textAliveDeath_characters_fragment)
        gender = view.findViewById(R.id.textMaleFemale_characters_fragment)
        origin = view.findViewById(R.id.originName_characters_fragment)
        epAppearances = view.findViewById(R.id.numberofepisodes_characters_fragment)

        idchar = args.characterId
        apiReq()
    }

    private fun setImage() {
        image.load(resultAPI.image) {
            transformations(CircleCropTransformation())
            diskCachePolicy(CachePolicy.ENABLED)
            memoryCachePolicy(CachePolicy.ENABLED)
            error(R.drawable.ic_error)
            placeholder(R.drawable.ic_descarga)
        }
    }

    private fun setInfo() {
        name.text = resultAPI.name
        species.text = resultAPI.species
        status.text =  resultAPI.status
        gender.text = resultAPI.gender
        origin.text = resultAPI.origin.name
        epAppearances.text = resultAPI.episode.size.toString()
    }

    private fun apiReq() {

        RetrofitInstance.api.getidCharacter(idchar).enqueue(object : Callback<CharacterDto> {
                override fun onResponse(
                    call: Call<CharacterDto>,
                    response: Response<CharacterDto>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        resultAPI = response.body()!!
                        setImage()
                        setInfo()
                    }
                }

                override fun onFailure(call: Call<CharacterDto>, t: Throwable) {
                    println("Error")
                }

            })
    }
}