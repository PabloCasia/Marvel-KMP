package ui.components


import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import io.github.alexzhirkevich.cupertino.adaptive.AdaptiveButton
import io.github.alexzhirkevich.cupertino.adaptive.ExperimentalAdaptiveApi
import io.kamel.core.utils.File
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import io.ktor.http.Url
import model.Character

@Composable
fun CharacterCard(list: List<Character>) {
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
                navigationIcon = {
                    IconButton(onClick = { /* do something */ }) {
                        Icon(
                            imageVector = Icons.Filled.Menu,
                            tint = Color.Black,
                            contentDescription = "Localized description"
                        )
                    }
                },
            )
        },
    ) { innerPadding ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(5.dp),
            horizontalArrangement = Arrangement.spacedBy(5.dp),
            modifier = Modifier.fillMaxSize(),
            content = {
                items(list.size) { index ->
                    val data = list[index]
                    CharacterItem(
                        data.name,
                        data.description ?: "",
                        data.id.toString(),
                        data.thumbnail,
                    )
                }
            }
        )
    }
}

@OptIn(ExperimentalAdaptiveApi::class)
@Composable
fun CharacterItem(name: String, description: String, id: String, image: Url) {
    Card(
        modifier = Modifier.padding(8.dp)
            .fillMaxWidth()
            .wrapContentHeight(),
        shape = MaterialTheme.shapes.medium,
        elevation = 5.dp,
        backgroundColor = MaterialTheme.colors.surface
    ) {
        Column(modifier = Modifier.height(380.dp).padding(10.dp)) {
            KamelImage(
                resource = asyncPainterResource(data = image),
                contentDescription = null,
                animationSpec = tween(),
                modifier = Modifier.height(150.dp).fillMaxWidth().clip(MaterialTheme.shapes.medium),
                contentScale = ContentScale.Crop,
            )
            Spacer(modifier = Modifier.height(16.dp))
            Column {
                Text(
                    modifier = Modifier.padding(2.dp),
                    text = name,
                    maxLines = 3,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.h6,
                    color = Color.Black,
                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    modifier = Modifier.padding(2.dp),
                    text = if (description.length > 30) {
                        description.substring(0, 30).plus("...")
                    } else {
                        description
                    },
                    fontWeight = FontWeight.Normal,
                    maxLines = 4,
                    style = MaterialTheme.typography.body2,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    modifier = Modifier.padding(2.dp),
                    text = id,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.button,
                )
                Spacer(modifier = Modifier.height(3.dp))
                Button(
                    onClick = { },
                    modifier = Modifier.padding(16.dp),
                ) {
                    Text(
                        text = "See more",
                        modifier = Modifier.padding(2.dp),
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}
