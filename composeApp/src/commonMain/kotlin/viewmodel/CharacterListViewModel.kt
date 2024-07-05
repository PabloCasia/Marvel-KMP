package viewmodel

import dto.CharacterDto
import dto.MarvelApiResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import network.ApiStatus
import network.repository.CharacterRepository

class CharacterListViewModel(private val characterRepository: CharacterRepository) {

    private val _characterListState = MutableStateFlow(CharacterListState())
    private val _characterListViewState: MutableStateFlow<CharacterListScreenState> =
        MutableStateFlow(CharacterListScreenState.Initial)
    val characterListViewState = _characterListViewState.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun getCharacters(offset: Int, limit: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                characterRepository.fetchCharacters(offset, limit).collect { response ->
                    when (response.status) {
                        ApiStatus.LOADING -> {
                            _isLoading.value = true
                        }

                        ApiStatus.SUCCESS -> {
                            _characterListState.update { currentState ->
                                val oldData =
                                    currentState.responseData?.data?.results ?: emptyList()
                                val newData = response.data?.data?.results ?: emptyList()
                                val combinedData = oldData + newData
                                val combinedResponse =
                                    response.data?.copy(data = response.data.data?.copy(results = combinedData))
                                currentState.copy(
                                    errorMessage = "",
                                    responseData = combinedResponse
                                )
                            }
                            _isLoading.value = false
                        }

                        ApiStatus.ERROR -> {
                            _characterListState.update {
                                it.copy(
                                    errorMessage = response.message
                                )
                            }
                            _isLoading.value = false
                        }
                    }
                }
            } catch (e: Exception) {
                _characterListState.update {
                    it.copy(
                        errorMessage = "Failed to fetch data"
                    )
                }
                _isLoading.value = false
            } finally {
                _characterListViewState.value = _characterListState.value.toUiState()
            }
        }
    }

    sealed class CharacterListScreenState {
        data object Initial : CharacterListScreenState()
        data class Error(val errorMessage: String) : CharacterListScreenState()
        data class Success(val responseData: MarvelApiResponse<CharacterDto>) :
            CharacterListScreenState()
    }

    private data class CharacterListState(
        val errorMessage: String? = null,
        val responseData: MarvelApiResponse<CharacterDto>? = null
    ) {
        fun toUiState(): CharacterListScreenState {
            return if (errorMessage?.isNotEmpty() == true) {
                CharacterListScreenState.Error(errorMessage)
            } else {
                CharacterListScreenState.Success(responseData!!)
            }
        }
    }
}
