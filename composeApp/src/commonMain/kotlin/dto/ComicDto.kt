package dto

import kotlinx.serialization.Serializable

@Serializable
data class ComicDto(
    val id: Int,
    val title: String,
    val description: String?,
    val isbn: String,
    val thumbnail: MarvelImageDto,
    val images: List<MarvelImageDto>,
)
