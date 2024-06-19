package ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import model.mappers.toCharacter
import org.koin.compose.getKoin
import ui.components.CharacterList
import ui.components.ErrorView
import ui.components.ProgressIndicator
import viewmodel.CharacterListViewModel

class CharacterListScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current
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
                CharacterList(characters.map { it.toCharacter() }) { id ->
                    navigator?.push(CharacterDetailScreen(id))
                }
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