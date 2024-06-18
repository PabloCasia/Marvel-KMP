package dto

import kotlinx.serialization.Serializable

@Serializable
data class MarvelApiResponse<T>(
    val code: Int,
    val status: String,
    val copyright: String,
    val attributionText: String,
    val attributionHTML: String,
    val data: MarvelApiResponseDataContainer<T>,
    val etag: String
)
