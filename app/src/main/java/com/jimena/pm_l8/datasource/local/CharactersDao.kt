package com.jimena.pm_l8.datasource.local

//Acá se define la clase que va a ser la tabla de la base de datos de los personajes
import androidx.room.*
import com.jimena.pm_l8.datasource.model.DataCharacters

@Dao
interface CharactersDao {

    @Query("SELECT * FROM dataCharacters")
    suspend fun getAllCharacters(): List<DataCharacters>

    @Query("SELECT * FROM dataCharacters WHERE id = :id")
    suspend fun getCharacterById(id: Int): DataCharacters

    @Update
    suspend fun update(characters: DataCharacters)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createCharacter(characters: DataCharacters)

    @Delete
    suspend fun delete(user: DataCharacters): Int

    @Query("DELETE FROM dataCharacters")
    suspend fun deleteAll(): Int

}