package network.repository

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.engine.mock.respondBadRequest
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import network.NetworkResult
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class CharacterRepositoryImplTest {

    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    private val mockEngine = MockEngine { request ->
        when (request.url.encodedPath) {
            "/v1/public/characters" -> respond(
                content = """
            {
              "code": 200,
              "status": "Ok",
              "data": {
                "offset": 0,
                "limit": 20,
                "total": 1,
                "count": 1,
                "results": [
                  {
                    "id": 1,
                    "name": "Test Character",
                    "description": "A test character",
                    "modified": "2020-01-01T00:00:00-0500",
                    "thumbnail": {
                      "path": "http://example.com/image",
                      "extension": "jpg"
                    },
                    "resourceURI": "http://example.com/character/1",
                    "comics": {
                      "available": 0,
                      "collectionURI": "http://example.com/character/1/comics",
                      "items": [],
                      "returned": 0
                    }
                  }
                ]
              }
            }
            """.trimIndent(),
                status = HttpStatusCode.OK,
                headers = headersOf(
                    HttpHeaders.ContentType,
                    ContentType.Application.Json.toString()
                )
            )

            "/v1/public/characters/1" -> respond(
                content = """
            {
              "code": 200,
              "status": "Ok",
              "data": {
                "offset": 0,
                "limit": 1,
                "total": 1,
                "count": 1,
                "results": [
                  {
                    "id": 1,
                    "name": "Iron Man",
                    "description": "A test character",
                    "modified": "2020-01-01T00:00:00-0500",
                    "thumbnail": {
                      "path": "http://example.com/image",
                      "extension": "jpg"
                    },
                    "resourceURI": "http://example.com/character/1",
                    "comics": {
                      "available": 0,
                      "collectionURI": "http://example.com/character/1/comics",
                      "items": [],
                      "returned": 0
                    }
                  }
                ]
              }
            }
            """.trimIndent(),
                status = HttpStatusCode.OK,
                headers = headersOf(
                    HttpHeaders.ContentType,
                    ContentType.Application.Json.toString()
                )
            )

            else -> respondBadRequest()
        }
    }

    private val mockHttpClient = HttpClient(mockEngine) {
        install(ContentNegotiation) {
            json(json)
        }
    }

    private val repository = CharacterRepositoryImpl(mockHttpClient)

    @Test
    fun `fetchCharacters returns success result`() = runTest {
        val result = repository.fetchCharacters(0, 10).toList().last()

        assertTrue(result is NetworkResult.Success)
        assertNotNull(result.data)
        assertEquals("Test Character", result.data?.data?.results?.first()?.name)
    }

    @Test
    fun `fetchCharacter returns success result`() = runTest {
        val result = repository.fetchCharacter(1).toList().last()

        assertTrue(result is NetworkResult.Success)
        assertNotNull(result.data)
        assertEquals("Iron Man", result.data?.data?.results?.first()?.name)
    }

    @Test
    fun `fetchCharacters returns error on 500 server error`() = runTest {
        val errorMockEngine = MockEngine { request ->
            when (request.url.encodedPath) {
                "/v1/public/characters" -> respond(
                    content = """
                {
                  "code": 500,
                  "status": "Internal Server Error",
                  "data": null,
                  "message": "Something went wrong"
                }
                """.trimIndent(),
                    status = HttpStatusCode.InternalServerError,
                    headers = headersOf(
                        HttpHeaders.ContentType,
                        ContentType.Application.Json.toString()
                    )
                )

                else -> respondBadRequest()
            }
        }

        val errorMockHttpClient = HttpClient(errorMockEngine) {
            install(ContentNegotiation) {
                json(json)
            }
        }

        val repositoryWithErrorResponse = CharacterRepositoryImpl(errorMockHttpClient)

        val result = repositoryWithErrorResponse.fetchCharacters(0, 10).toList().last()

        assertTrue((result as NetworkResult.Success)._data?.code == 500)
    }
}
