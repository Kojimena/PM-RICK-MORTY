package com.jimena.pm_l8.adapters

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import coil.load
import coil.request.CachePolicy
import coil.transform.CircleCropTransformation
import com.jimena.pm_l8.R
import com.jimena.pm_l8.datasource.model.DataCharacters


class CharacterAdapter(
    private val dataSet: MutableList<DataCharacters>,
    private val characterListener: CharacterListener
):
    RecyclerView.Adapter<CharacterAdapter.ViewHolder>() {

    interface CharacterListener {
        fun onPlaceClicked(data: DataCharacters, position: Int)

    }

    class ViewHolder( //Aqui se definen los elementos que se van a mostrar en el recycler view
        private val view: View,
        private val listener: CharacterListener) : RecyclerView.ViewHolder(view) {
        private val imageType: ImageView = view.findViewById(R.id.imageView_recycleViewCharacter)
        private val textName: TextView = view.findViewById(R.id.textView_recycleViewCharacter_name)
        private val textRaceStatus: TextView = view.findViewById(R.id.textView_recycleViewCharacter_Status)
        private val layout: ConstraintLayout = view.findViewById(R.id.layout_itemPlace)
        private lateinit var character: DataCharacters

        fun setData(place: DataCharacters) {
            this.character = place
            textName.text = place.name
            (place.species + " - " + place.status).also { textRaceStatus.text = it }

            imageType.load(place.image) {
                transformations(CircleCropTransformation())
                diskCachePolicy(CachePolicy.DISABLED)
                memoryCachePolicy(CachePolicy.DISABLED)
                error(R.drawable.ic_error)
                placeholder(R.drawable.ic_descarga)
            }
            setListeners()
        }

        private fun setListeners() { //Acciones que se ejecutan al pulsar sobre un item
            layout.setOnClickListener {
                listener.onPlaceClicked(character, this.adapterPosition)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recycler_character, parent, false)

        return ViewHolder(view, characterListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setData(dataSet[position])
    }

    override fun getItemCount() = dataSet.size

}