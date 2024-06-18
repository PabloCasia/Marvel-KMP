package model

import io.ktor.http.Url

data class Comic(
    val id: Int,
    val title: String,
    val description: String?,
    val isbn: String,
    val thumbnail: Url,
    val images: List<Url>,
)
