package ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import model.Character
import model.Comic
import model.mappers.toCharacter
import model.mappers.toComic
import org.koin.compose.getKoin
import ui.components.ErrorView
import viewmodel.CharacterDetailViewModel

class CharacterDetailScreen(
    private val characterId: Int,
) : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current
        val viewModel: CharacterDetailViewModel = getKoin().get()
        val characterDetailState by viewModel.state.collectAsState()
        LaunchedEffect(Unit) {
            viewModel.getCharacterDetail(characterId)
        }
        when {
            characterDetailState.isLoading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            characterDetailState.error != null -> {
                ErrorView(
                    text = characterDetailState.error!!,
                    onClick = {
                        viewModel.getCharacterDetail(characterId)
                    }
                )
            }

            else -> {
                val character = characterDetailState.character?.toCharacter()
                if (character != null) {
                    DetailContent(
                        character,
                        characterDetailState.comics.map {
                            it.toComic()
                        }, navigator
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailContent(
    character: Character,
    comics: List<Comic>,
    navigator: Navigator?
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Character Detail") },
                navigationIcon = {
                    IconButton(onClick = {
                        navigator?.pop()
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
        content = { paddingValues ->
            LazyColumn(
                modifier = Modifier.padding(paddingValues),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                item {
                    KamelImage(
                        resource = asyncPainterResource(data = character.thumbnail),
                        contentDescription = character.name,
                        modifier = Modifier
                            .height(200.dp)
                            .fillMaxWidth(),
                        contentScale = ContentScale.FillBounds,
                    )
                }
                item {
                    Text(
                        text = character.name,
                        modifier = Modifier.padding(8.dp),
                        style = typography.h5,
                    )
                }
                if (!character.description.isNullOrEmpty()) {
                    item {
                        Text(
                            text = character.description,
                            modifier = Modifier.padding(8.dp),
                            style = typography.body1
                        )
                    }
                }
                items(comics) { comic ->
                    ComicItem(comic)
                }
            }
        }
    )
}

@Composable
fun ComicItem(comic: Comic) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        ),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            comic.title.let {
                Text(
                    text = it,
                    style = typography.h6,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            comic.date?.let {
                Text(
                    text = it,
                    style = typography.body2,
                    color = Color.Gray
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            AnimatedVisibility(visible = true) {
                KamelImage(
                    resource = asyncPainterResource(data = comic.thumbnail),
                    contentDescription = comic.title,
                    modifier = Modifier
                        .height(200.dp)
                        .fillMaxWidth(),
                    contentScale = ContentScale.Fit,
                )
            }
        }
    }
}
