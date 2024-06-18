package model

import io.ktor.http.Url

data class Character(
    val id: Int,
    val name: String,
    val description: String?,
    val thumbnail: Url,
)
