package com.jimena.pm_l8


import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jimena.pm_l8.adapters.CharacterAdapter
import com.jimena.pm_l8.entities.Character

class CharacterListFragment : Fragment(R.layout.fragment_character_list), CharacterAdapter.CharacterListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var charList: MutableList<Character>



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerList_recyclerActivity)


        setupRecycler()
        setListeners()
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
                else -> false
            }
        }

    }

    private fun setupRecycler() {
        charList = RickAndMortyDB.getCharacters()
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = CharacterAdapter(charList, this)
    }

    override fun onPlaceClicked(data: Character, position: Int) {
        requireView().findNavController().navigate(
            CharacterListFragmentDirections.actionCharacterListFragmentToCharacters(
                data
            )
        )
    }
}