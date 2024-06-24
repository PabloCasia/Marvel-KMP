package network.repository

import dto.ComicDto
import dto.MarvelApiResponse
import kotlinx.coroutines.flow.Flow
import network.NetworkResult

interface ComicRepository {
    suspend fun fetchComicsByCharacterId(
        characterId: Int,
        offset: Int,
        limit: Int
    ): Flow<NetworkResult<MarvelApiResponse<ComicDto>?>>
}
