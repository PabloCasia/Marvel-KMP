package dto

import kotlinx.serialization.Serializable

@Serializable
data class MarvelApiResponse<T>(
    val code: Int? = null,
    val status: String? = null,
    val copyright: String? = null,
    val attributionText: String? = null,
    val attributionHTML: String? = null,
    val data: MarvelApiResponseDataContainer<T>? = null,
    val etag: String? = null
)
