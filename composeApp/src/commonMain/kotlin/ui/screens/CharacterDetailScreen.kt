package ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import marveldemo.composeapp.generated.resources.Res
import model.mappers.toCharacter
import org.jetbrains.compose.resources.StringResource
import org.koin.compose.getKoin
import ui.components.ProgressIndicator
import ui.components.CharacterCard
import ui.components.ErrorView
import viewmodel.CharacterListViewModel

@Composable
fun CharacterDetailScreen(
    characterId: Int,
    onCancelButtonClicked: () -> Unit = {},
    modifier: Modifier
) {
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
            CharacterCard(characters.map { it.toCharacter() })
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