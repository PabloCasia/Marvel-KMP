package ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import model.mappers.toCharacter
import org.koin.compose.getKoin
import ui.components.CharacterList
import ui.components.ErrorView
import ui.components.ProgressIndicator
import viewmodel.CharacterListViewModel

@Composable
fun CharacterListScreen(onCharacterClick: (Int) -> Unit) {
    val viewModel: CharacterListViewModel = getKoin().get()
    val comicListScreenState by viewModel.characterListViewState.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.getCharacters(0, 20)
    }
    when (comicListScreenState) {
        is CharacterListViewModel.CharacterListScreenState.Initial -> {
            ProgressIndicator()
        }

        is CharacterListViewModel.CharacterListScreenState.Success -> {
            val characters =
                (comicListScreenState as CharacterListViewModel.CharacterListScreenState.Success).responseData.data?.results
            characters?.map { it.toCharacter() }?.let {
                CharacterList(
                    characters = it,
                    onCharacterClick = { id ->
                        onCharacterClick(id)
                    },
                    loadMoreItems = { index ->
                        if (index == characters.size - 1) {
                            viewModel.getCharacters(index + 1, 20)
                        }
                    },
                    isLoading = isLoading
                )
            }
        }

        is CharacterListViewModel.CharacterListScreenState.Error -> {
            ErrorView(
                text = (comicListScreenState as CharacterListViewModel.CharacterListScreenState.Error).errorMessage,
                onClick = {
                    viewModel.getCharacters(0, 20)
                }
            )
        }
    }
}
