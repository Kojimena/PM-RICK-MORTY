package com.jimena.pm_l8
import com.jimena.pm_l8.entities.Character
import java.util.*

// ESTA CLASE YA NO SE UTILIZA EN LAB 9
object RickAndMortyDB {

    private val characters = mutableListOf(
        Character(
            name = "Rick Sanchez",
            status = "Alive",
            species = "Human",
            gender = "Male",
            image = "https://rickandmortyapi.com/api/character/avatar/1.jpeg"
        ),
        Character(
            name = "Morty Smith",
            status = "Alive",
            species = "Human",
            gender = "Male",
            image = "https://rickandmortyapi.com/api/character/avatar/2.jpeg",
        ),
        Character(
            name = "Summer Smith",
            status = "Alive",
            species = "Human",
            gender = "Female",
            image = "https://rickandmortyapi.com/api/character/avatar/3.jpeg",
        ),
        Character(
            name = "Beth Smith",
            status = "Alive",
            species = "Human",
            gender = "Female",
            image = "https://rickandmortyapi.com/api/character/avatar/4.jpeg",
        ),
        Character(
            name = "Jerry Smith",
            status = "Alive",
            species = "Human",
            gender = "Male",
            image = "https://rickandmortyapi.com/api/character/avatar/5.jpeg",
        ),
        Character(
            name = "Abadango Cluster Princess",
            status = "Alive",
            species = "Alien",
            gender = "Female",
            image = "https://rickandmortyapi.com/api/character/avatar/6.jpeg",
        ),
        Character(
            name = "Abradolf Lincler",
            status = "unknown",
            species = "Human",
            gender = "Male",
            image = "https://rickandmortyapi.com/api/character/avatar/7.jpeg",
        ),
        Character(
            name = "Adjudicator Rick",
            status = "Dead",
            species = "Human",
            gender = "Male",
            image = "https://rickandmortyapi.com/api/character/avatar/8.jpeg",
        ),
        Character(
            name = "Agency Director",
            status = "Dead",
            species = "Human",
            gender = "Male",
            image = "https://rickandmortyapi.com/api/character/avatar/9.jpeg",
        ),
        Character(
            name = "Alan Rails",
            status = "Dead",
            species = "Human",
            gender = "Male",
            image = "https://rickandmortyapi.com/api/character/avatar/10.jpeg",
        ),
    )
    
    fun getCharacters() = characters

}