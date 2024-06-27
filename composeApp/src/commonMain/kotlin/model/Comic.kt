package model

import io.ktor.http.Url

data class Comic(
    val id: Int,
    val title: String,
    val date: String?,
    val thumbnail: Url,
)
