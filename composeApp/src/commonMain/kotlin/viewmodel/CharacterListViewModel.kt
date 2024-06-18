package viewmodel

import dto.CharacterDto
import dto.MarvelApiResponse
import dto.MarvelApiResponseDataContainer
import dto.MarvelImageDto
import io.ktor.http.Url
import isAndroid
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import network.ApiStatus
import network.repository.CharacterRepository

class CharacterListViewModel(private val characterRepository: CharacterRepository) {

    private val _characterListState = MutableStateFlow(CharacterListState())
    private val _characterListViewState: MutableStateFlow<CharacterListScreenState> =
        MutableStateFlow(CharacterListScreenState.Loading)
    val characterListViewState = _characterListViewState.asStateFlow()

    suspend fun getCharacters() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                characterRepository.fetchCharacters().collect { response ->
                    when (response.status) {
                        ApiStatus.LOADING -> {
                            _characterListState.update { it.copy(isLoading = true) }
                        }

                        ApiStatus.SUCCESS -> {
                            _characterListState.update {
                                it.copy(
                                    isLoading = false,
                                    errorMessage = "",
                                    responseData = response.data
                                )
                            }
                        }

                        ApiStatus.ERROR -> {
                            _characterListState.update {
                                it.copy(
                                    isLoading = false,
                                    errorMessage = response.message
                                )
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                _characterListState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Failed to fetch data"
                    )
                }
            } finally {
                _characterListViewState.value = _characterListState.value.toUiState()
            }
        }
    }

    sealed class CharacterListScreenState {
        data object Loading : CharacterListScreenState()
        data class Error(val errorMessage: String) : CharacterListScreenState()
        data class Success(val responseData: MarvelApiResponse<CharacterDto>) :
            CharacterListScreenState()
    }

    private data class CharacterListState(
        val isLoading: Boolean = false,
        val errorMessage: String? = null,
        val responseData: MarvelApiResponse<CharacterDto>? = null
    ) {
        fun toUiState(): CharacterListScreenState {
            return if (isLoading) {
                CharacterListScreenState.Loading
            } else if (errorMessage?.isNotEmpty() == true) {
                CharacterListScreenState.Error(errorMessage)
            } else {
                CharacterListScreenState.Success(responseData!!)
            }
        }
    }
}