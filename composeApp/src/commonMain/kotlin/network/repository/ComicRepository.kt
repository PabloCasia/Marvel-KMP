package network.repository

import dto.ComicDto
import dto.MarvelApiResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.path
import kotlinx.coroutines.flow.Flow
import network.NetworkResult
import network.toResultFlow
import utils.authorized

interface ComicRepository {
    suspend fun fetchComicsByCharacterId(characterId: Int): Flow<NetworkResult<MarvelApiResponse<ComicDto>?>>
}

internal class ComicRepositoryImpl(private val httpClient: HttpClient) : ComicRepository {
    override suspend fun fetchComicsByCharacterId(characterId: Int): Flow<NetworkResult<MarvelApiResponse<ComicDto>?>> {
        return toResultFlow {
            val response = httpClient.get {
                url {
                    path("v1/public/characters/${characterId}/comics")
                    authorized()
                }
            }.body<MarvelApiResponse<ComicDto>>()
            NetworkResult.Success(response)
        }
    }
}
