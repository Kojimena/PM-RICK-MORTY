package com.jimena.pm_l8.datasource.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.jimena.pm_l8.datasource.model.DataCharacters

@Database(entities = [DataCharacters::class], version = 1, exportSchema = false)
abstract class Database: RoomDatabase() {
      abstract fun characterDao(): CharactersDao
}