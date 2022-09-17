package com.jimena.pm_l8.datasource.api

import android.location.Location
import com.jimena.pm_l8.datasource.model.AllAssetsResponse
import com.jimena.pm_l8.datasource.model.CharacterDto
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface RickertAPI {

    @GET("/api/character/")
    fun getCharacter(
    ): Call<AllAssetsResponse>

    @GET("/api/character/{id}")
    fun getidCharacter(
        @Path("id") id: Int
    ): Call<CharacterDto>

}