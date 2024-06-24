package ui.components


import CharacterCard
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import model.Character

@Composable
fun CharacterList(
    characters: List<Character>,
    onCharacterClick: (Int) -> Unit,
    loadMoreItems: (Int) -> Unit,
    isLoading: Boolean
) {
    val listState = rememberLazyGridState()

    Scaffold(
        topBar = {
            TopAppBar(
                contentColor = Color.Black,
                backgroundColor = Color.White,
                title = {
                    Text(
                        "Sngular Marvel",
                        maxLines = 1,
                    )
                },
            )
        },
    ) { innerPadding ->
        LazyVerticalGrid(
            state = listState,
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(5.dp),
            horizontalArrangement = Arrangement.spacedBy(5.dp),
            modifier = Modifier.fillMaxSize().padding(innerPadding),
            content = {
                items(characters.size + if (isLoading) 2 else 0) { index ->
                    if (index >= characters.size) {
                        CharacterSkeleton()
                    } else {
                        val data = characters[index]
                        CharacterCard(
                            data.name,
                            data.comicsSize,
                            data.id.toString(),
                            data.thumbnail,
                        ) { id ->
                            onCharacterClick(id)
                        }

                        if (index == characters.size - 1) {
                            loadMoreItems(index)
                        }
                    }
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CharacterSkeleton() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        ),
    ) {
        Column {
            Box(
                modifier = Modifier
                    .height(200.dp)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Box(Modifier.fillMaxSize().background(Color.Gray))
            }
            ListItem(
                text = { Box(Modifier.background(Color.Gray).height(20.dp).fillMaxWidth()) },
                secondaryText = {
                    Box(
                        Modifier.background(Color.Gray).height(20.dp).fillMaxWidth()
                    )
                }
            )
        }
    }
}
