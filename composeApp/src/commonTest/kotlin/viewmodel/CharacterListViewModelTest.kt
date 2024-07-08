package viewmodel

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.engine.mock.respondError
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import network.repository.CharacterRepositoryImpl
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue


@ExperimentalCoroutinesApi
class CharacterListViewModelTest {

    private lateinit var viewModel: CharacterListViewModel
    private lateinit var mockEngine: MockEngine

    @BeforeTest
    fun setup() {
        mockEngine = MockEngine { request ->
            when (request.url.encodedPath) {
                "/v1/public/characters" -> {
                    respond(
                        content = """
                {
                  "code": 200,
                  "status": "Ok",
                  "data": {
                    "offset": 0,
                    "limit": 20,
                    "total": 150,
                    "count": 20,
                    "results": [
                      {
                        "id": 1011334,
                        "name": "3-D Man",
                        "description": "",
                        "modified": "2014-04-29T14:18:17-0400",
                        "thumbnail": {
                          "path": "http://i.annihil.us/u/prod/marvel/i/mg/c/e0/535fecbbb9784",
                          "extension": "jpg"
                        },
                        "resourceURI": "http://gateway.marvel.com/v1/public/characters/1011334",
                        "comics": {
                          "available": 12,
                          "collectionURI": "http://gateway.marvel.com/v1/public/characters/1011334/comics",
                          "items": [
                            {
                              "resourceURI": "http://gateway.marvel.com/v1/public/comics/21366",
                              "name": "Avengers: The Initiative (2007) #14"
                            }
                          ],
                          "returned": 1
                        }
                      }
                    ]
                  }
                }
            """.trimIndent(),
                        headers = headersOf("Content-Type" to listOf(ContentType.Application.Json.toString()))
                    )
                }

                else -> respondError(HttpStatusCode.NotFound)
            }
        }
        val mockHttpClient = HttpClient(mockEngine) {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                })
            }
        }
        val repository = CharacterRepositoryImpl(mockHttpClient)
        viewModel = CharacterListViewModel(repository)
    }

    @Test
    fun `initial state is correct`() = runTest {
        val initialState = viewModel.characterListViewState.first()
        assertTrue(
            initialState is CharacterListViewModel.CharacterListScreenState.Initial,
            "Initial state is not as expected."
        )
    }

    @Test
    fun `getCharacters success updates state correctly and verifies received character`() =
        runTest {
            viewModel.getCharacters(0, 20)


            val successState = viewModel.characterListViewState
                .first { it is CharacterListViewModel.CharacterListScreenState.Success } as CharacterListViewModel.CharacterListScreenState.Success


            assertEquals(
                1,
                successState.responseData.data?.results?.size,
                "Expected exactly one character in the result."
            )
            assertEquals(
                "3-D Man",
                successState.responseData.data?.results?.first()?.name,
                "Character name does not match the expected value."
            )
            assertEquals(
                1011334,
                successState.responseData.data?.results?.first()?.id,
                "Character ID does not match the expected value."
            )
            assertEquals(
                "",
                successState.responseData.data?.results?.first()?.description,
                "Character description does not match the expected value."
            )
        }


    @Test
    fun `getCharacters error updates state correctly`() = runTest {
        mockEngine = MockEngine { request ->
            when (request.url.encodedPath) {
                "/v1/public/characters" -> respondError(HttpStatusCode.InternalServerError)
                else -> respondError(HttpStatusCode.NotFound)
            }
        }
        val mockHttpClient = HttpClient(mockEngine) {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                })
            }
        }
        val repository = CharacterRepositoryImpl(mockHttpClient)
        viewModel = CharacterListViewModel(repository)


        viewModel.getCharacters(0, 20)


        val errorState = viewModel.characterListViewState
            .first { it is CharacterListViewModel.CharacterListScreenState.Error } as CharacterListViewModel.CharacterListScreenState.Error

        assertEquals(
            "Failed to fetch data",
            errorState.errorMessage,
            "Error message does not match the expected value."
        )
    }

}
