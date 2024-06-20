import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.FadeTransition
import cafe.adriel.voyager.transitions.ScaleTransition
import cafe.adriel.voyager.transitions.SlideTransition
import di.appModule
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
                Navigator(screen = CharacterListScreen()) { navigator ->
                    // FadeTransition(navigator = navigator)
                    SlideTransition(navigator = navigator)
                    // ScaleTransition(navigator = navigator)
                }
            }
        }
    }
}
