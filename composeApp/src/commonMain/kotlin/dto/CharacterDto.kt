package dto

import kotlinx.serialization.Serializable

@Serializable
data class CharacterDto(
    val id: Int,
    val name: String,
    val description: String?,
    val thumbnail: MarvelImageDto,
    val comics: ComicList?,
)

@Serializable
data class ComicList(
    val available: Int?,
    val returned: Int?,
    val collectionURI: String?,
    val items: List<ComicSummary>?
)

@Serializable
data class ComicSummary(
    val resourceURI: String?,
    val name: String?
)
