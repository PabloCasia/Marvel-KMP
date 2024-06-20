package dto

import kotlinx.serialization.Serializable

@Serializable
data class ComicDto(
    val id: Int,
    val title: String,
    val dates: List<DateDto>?,
    val thumbnail: MarvelImageDto,
)

@Serializable
data class DateDto(
    val date: String,
    val type: String
)
