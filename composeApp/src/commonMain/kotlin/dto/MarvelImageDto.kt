package dto

import kotlinx.serialization.Serializable

@Serializable
data class MarvelImageDto(
    val path: String,
    val extension: String
)
