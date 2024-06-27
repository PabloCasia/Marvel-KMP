package ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
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
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import marveldemo.composeapp.generated.resources.Res
import marveldemo.composeapp.generated.resources.back
import marveldemo.composeapp.generated.resources.character_detail_screen
import model.Character
import model.Comic
import model.mappers.toCharacter
import model.mappers.toComic
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.getKoin
import ui.components.ErrorView
import viewmodel.CharacterDetailViewModel

@Composable
fun CharacterDetailScreen(
    characterId: Int,
    onBackClick: () -> Unit
) {
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
                    },
                    viewModel,
                    onBackClick
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailContent(
    character: Character,
    comics: List<Comic>,
    viewModel: CharacterDetailViewModel,
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(Res.string.character_detail_screen)) },
                navigationIcon = {
                    IconButton(onClick = {
                        onBackClick.invoke()
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(Res.string.back)
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
                            .fillMaxWidth()
                            .aspectRatio(16f / 9f),
                        contentScale = ContentScale.FillBounds,
                    )
                }
                item {
                    Text(
                        text = character.name,
                        modifier = Modifier.padding(8.dp),
                        style = typography.bodyLarge,
                    )
                }
                if (!character.description.isNullOrEmpty()) {
                    item {
                        Text(
                            text = character.description,
                            modifier = Modifier.padding(8.dp),
                            style = typography.bodyMedium
                        )
                    }
                }
                itemsIndexed(comics) { index, comic ->
                    ComicItem(comic)
                    if (index == comics.size - 1 && viewModel.state.value.hasMoreComics) {
                        LaunchedEffect(Unit) {
                            viewModel.loadMoreComics(character.id)
                        }
                    }
                }
                if (viewModel.state.value.hasMoreComics && comics.isNotEmpty()) {
                    item { ComicSkeleton() }
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
            Text(
                text = comic.title,
                style = typography.bodyLarge,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(8.dp))
            comic.date?.let {
                Text(
                    text = it,
                    style = typography.bodyMedium,
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


@Composable
fun ComicSkeleton() {
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
            Box(
                modifier = Modifier
                    .height(200.dp)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Box(Modifier.fillMaxSize().background(Color.Gray))
            }
            ListItem(
                headlineContent = {
                    Box(
                        Modifier.background(Color.Gray).height(20.dp).fillMaxWidth()
                    )
                },
                leadingContent = {
                    Box(
                        Modifier.background(Color.Gray).height(20.dp).fillMaxWidth()
                    )
                }
            )
        }
    }
}
