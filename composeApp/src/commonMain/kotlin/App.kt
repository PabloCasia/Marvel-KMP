import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import di.appModule
import io.kamel.core.config.KamelConfig
import io.kamel.core.config.httpFetcher
import io.ktor.client.HttpClient
import org.koin.compose.KoinApplication
import ui.AppTheme
import ui.screens.CharacterListScreen

@Composable
fun App() {
    KoinApplication(application = {
        modules(appModule())
    }) {
        AppTheme {
            Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                CharacterListScreen()
            }
        }
    }
}
