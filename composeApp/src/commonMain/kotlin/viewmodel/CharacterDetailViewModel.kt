package viewmodel

import dto.CharacterDto
import dto.ComicDto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import network.ApiStatus
import network.repository.CharacterRepository
import network.repository.ComicRepository

class CharacterDetailViewModel(
    private val characterRepository: CharacterRepository,
    private val comicRepository: ComicRepository
) {

    private val _state = MutableStateFlow(UiState(isLoading = true))
    val state: StateFlow<UiState> = _state

    data class UiState(
        val isLoading: Boolean = false,
        val character: CharacterDto? = null,
        val error: String? = null,
        val comics: List<ComicDto> = emptyList(),
        val hasMoreComics: Boolean = false
    )

    fun getCharacterDetail(id: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            try {
                characterRepository.fetchCharacter(id).collect { response ->
                    when (response.status) {
                        ApiStatus.LOADING -> {
                            _state.value = _state.value.copy(isLoading = true)
                        }

                        ApiStatus.SUCCESS -> {
                            _state.value = _state.value.copy(
                                isLoading = false,
                                character = response.data?.data?.results?.first()
                            )
                            // limpiar lista de comics
                            _state.value = _state.value.copy(comics = emptyList())
                            getComics(id)
                        }

                        ApiStatus.ERROR -> {
                            _state.value =
                                _state.value.copy(isLoading = false, error = response.message)
                        }
                    }
                }
            } catch (e: Exception) {
                _state.value = _state.value.copy(isLoading = false, error = "Failed to fetch data")
            }
        }
    }

    private suspend fun getComics(characterId: Int, offset: Int = 0, limit: Int = 20) {
        CoroutineScope(Dispatchers.IO).launch {
            if (_state.value.comics.isEmpty()) {
                _state.value = _state.value.copy(isLoading = true)
            }
            try {
                comicRepository.fetchComicsByCharacterId(characterId, offset, limit)
                    .collect { response ->
                        when (response.status) {
                            ApiStatus.LOADING -> {
                                //  _state.value = _state.value.copy(isLoading = true)
                            }

                            ApiStatus.SUCCESS -> {
                                val oldComics = _state.value.comics
                                val newComics = response.data?.data?.results ?: emptyList()
                                val combinedComics = oldComics + newComics
                                val totalComics = response.data?.data?.total ?: 0
                                val hasMoreComics = combinedComics.size < totalComics
                                _state.value = _state.value.copy(
                                    isLoading = false,
                                    comics = combinedComics,
                                    hasMoreComics = hasMoreComics
                                )
                            }

                            ApiStatus.ERROR -> {
                                _state.value =
                                    _state.value.copy(isLoading = false, error = response.message)
                            }
                        }
                    }
            } catch (e: Exception) {
                _state.value = _state.value.copy(isLoading = false, error = "Failed to fetch data")
            }
        }
    }

    fun loadMoreComics(characterId: Int) {
        val currentComics = _state.value.comics
        if (_state.value.hasMoreComics) {
            CoroutineScope(Dispatchers.IO).launch {
                getComics(characterId, offset = currentComics.size, limit = 20)
            }
        }
    }
}
