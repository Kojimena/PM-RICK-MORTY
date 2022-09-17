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
import com.jimena.pm_l8.datasource.model.CharacterDto


class CharacterAdapter(
    private val dataSet: List<CharacterDto>,
    private val characterListener: CharacterListener
):
    RecyclerView.Adapter<CharacterAdapter.ViewHolder>() {

    interface CharacterListener {
        fun onPlaceClicked(data: CharacterDto, position: Int)
    }

    class ViewHolder(private val view: View,
                     private val listener: CharacterListener) : RecyclerView.ViewHolder(view) {
        private val imageType: ImageView = view.findViewById(R.id.imageView_recycleViewCharacter)
        private val textName: TextView = view.findViewById(R.id.textView_recycleViewCharacter_name)
        private val textRaceStatus: TextView = view.findViewById(R.id.textView_recycleViewCharacter_Status)
        private val layout: ConstraintLayout = view.findViewById(R.id.layout_itemPlace)
        private lateinit var place: CharacterDto

        fun setData(place: CharacterDto) {
            this.place = place
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

        private fun setListeners() {
            layout.setOnClickListener {
                listener.onPlaceClicked(place, this.adapterPosition)
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