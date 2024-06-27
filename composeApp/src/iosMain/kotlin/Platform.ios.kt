import io.github.alexzhirkevich.cupertino.adaptive.Theme
import io.ktor.client.HttpClient
import io.ktor.client.engine.darwin.Darwin

actual fun isAndroid(): Boolean = false

actual fun determineTheme(): Theme = Theme.Cupertino
actual fun getHttpClient(): HttpClient {
    return createHttpClient(Darwin.create())
}
