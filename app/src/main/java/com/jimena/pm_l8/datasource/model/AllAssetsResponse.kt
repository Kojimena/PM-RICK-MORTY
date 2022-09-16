package com.jimena.pm_l8.datasource.model

data class AllAssetsResponse(
    val info: Info,
    val results: List<CharacterDto>
)