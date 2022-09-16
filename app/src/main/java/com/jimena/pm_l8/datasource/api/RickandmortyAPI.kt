package com.jimena.pm_l8.datasource.api

import android.location.Location
import com.jimena.pm_l8.datasource.model.CharacterDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface RickandmortyAPI {
    @GET("/character")
    suspend fun getCharacters(): Response<List<CharacterDto>>

    @GET("/character/{id}")
    suspend fun getCharacter(@Path("id") id: Int): Response<CharacterDto>




}