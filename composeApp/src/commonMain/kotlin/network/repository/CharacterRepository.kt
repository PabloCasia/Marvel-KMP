package network.repository

import dto.CharacterDto
import dto.MarvelApiResponse
import kotlinx.coroutines.flow.Flow
import network.NetworkResult

interface CharacterRepository {
    suspend fun fetchCharacters(
        offset: Int,
        limit: Int,
    ): Flow<NetworkResult<MarvelApiResponse<CharacterDto>?>>

    suspend fun fetchCharacter(characterId: Int): Flow<NetworkResult<MarvelApiResponse<CharacterDto>?>>
}
