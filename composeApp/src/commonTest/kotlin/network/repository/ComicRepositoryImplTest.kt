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
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class ComicRepositoryImplTest {

    private lateinit var repository: ComicRepositoryImpl
    private lateinit var mockEngine: MockEngine
    private lateinit var mockHttpClient: HttpClient

    private val jsonConfiguration = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    @BeforeTest
    fun setup() {
        mockEngine = MockEngine { request ->
            when (request.url.encodedPath) {
                "/v1/public/characters/1/comics" -> respond(
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
                        "title": "Test Comic",
                        "description": "A test comic",
                        "modified": "2020-01-01T00:00:00-0500",
                        "thumbnail": {
                          "path": "http://example.com/image",
                          "extension": "jpg"
                        },
                        "dates": [
                          {
                            "type": "onsaleDate",
                            "date": "2020-01-01T00:00:00-0500"
                          }
                        ]
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

        mockHttpClient = HttpClient(mockEngine) {
            install(ContentNegotiation) {
                json(jsonConfiguration)
            }
        }
        repository = ComicRepositoryImpl(mockHttpClient)
    }

    @AfterTest
    fun tearDown() {
        mockEngine.close()
        mockHttpClient.close()
    }

    @Test
    fun `fetchComicsByCharacterId returns success result`() = runTest {
        val result = repository.fetchComicsByCharacterId(1, 0, 20).toList().last()

        assertTrue(result is NetworkResult.Success)
        assertNotNull(result.data)
        assertEquals("Test Comic", result.data?.data?.results?.first()?.title)
    }

    @Test
    fun `fetchComicsByCharacterId returns error on 500 server error`() = runTest {
        val errorMockEngine = MockEngine { request ->
            when (request.url.encodedPath) {
                "/v1/public/characters/1/comics" -> respond(
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
                json(jsonConfiguration)
            }
        }

        val repositoryWithErrorResponse = ComicRepositoryImpl(errorMockHttpClient)

        val result = repositoryWithErrorResponse.fetchComicsByCharacterId(1, 0, 20).toList().last()

        assertTrue((result as NetworkResult.Success)._data?.code == 500)
        assertEquals("Internal Server Error", result._data?.status)
    }
}
