import io.github.alexzhirkevich.cupertino.adaptive.Theme
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp

actual fun isAndroid(): Boolean = true

actual fun determineTheme(): Theme = Theme.Material3
actual fun getHttpClient(): HttpClient {
    return createHttpClient(OkHttp.create())
}
