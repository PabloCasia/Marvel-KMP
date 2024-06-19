import io.github.alexzhirkevich.cupertino.adaptive.Theme
import io.ktor.client.HttpClient

expect fun determineTheme(): Theme

expect fun isAndroid(): Boolean

expect fun getHttpClient(): HttpClient

expect fun getMarvelPublicKey(): String

expect fun getMarvelPrivateKey(): String