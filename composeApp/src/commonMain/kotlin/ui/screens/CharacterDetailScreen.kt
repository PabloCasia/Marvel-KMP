package ui.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import cafe.adriel.voyager.core.screen.Screen
import org.koin.compose.getKoin
import ui.components.ErrorView
import ui.components.ProgressIndicator
import viewmodel.CharacterListViewModel

class CharacterDetailScreen(
    characterId: Int,
) : Screen {

    @Composable
    override fun Content() {
        val viewModel: CharacterListViewModel = getKoin().get()
        val comicListScreenState by viewModel.characterListViewState.collectAsState()
        LaunchedEffect(Unit) {
            viewModel.getCharacters()
        }
        when (comicListScreenState) {
            is CharacterListViewModel.CharacterListScreenState.Loading -> {
                ProgressIndicator()
            }

            is CharacterListViewModel.CharacterListScreenState.Success -> {
                val characters =
                    (comicListScreenState as CharacterListViewModel.CharacterListScreenState.Success).responseData.data.results
                Text(characters.first().name)
            }

            is CharacterListViewModel.CharacterListScreenState.Error -> {
                ErrorView(
                    text = (comicListScreenState as CharacterListViewModel.CharacterListScreenState.Error).errorMessage,
                    onClick = {
                        // do something
                    }
                )
            }
        }
    }
}