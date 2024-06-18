package dto

import kotlinx.serialization.Serializable

@Serializable
data class MarvelApiResponseDataContainer<T>(
    val offset: Int,
    val limit: Int,
    val total: Int,
    val count: Int,
    val results: List<T>
)
