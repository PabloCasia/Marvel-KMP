package network.repository

import dto.CharacterDto
import dto.MarvelApiResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.path
import kotlinx.coroutines.flow.Flow
import network.NetworkResult
import network.toResultFlow
import utils.authorized

internal class CharacterRepositoryImpl(private val httpClient: HttpClient) : CharacterRepository {
    override suspend fun fetchCharacters(
        offset: Int,
        limit: Int
    ): Flow<NetworkResult<MarvelApiResponse<CharacterDto>?>> {
        return toResultFlow {
            val response = httpClient.get {
                url {
                    path("v1/public/characters")
                    parameters.append("offset", offset.toString())
                    parameters.append("limit", limit.toString())
                    authorized()
                }
            }.body<MarvelApiResponse<CharacterDto>>()
            NetworkResult.Success(response)
        }
    }

    override suspend fun fetchCharacter(characterId: Int): Flow<NetworkResult<MarvelApiResponse<CharacterDto>?>> {
        return toResultFlow {
            val response = httpClient.get {
                url {
                    path("v1/public/characters/${characterId}")
                    authorized()
                }
            }.body<MarvelApiResponse<CharacterDto>>()
            NetworkResult.Success(response)
        }
    }
}
