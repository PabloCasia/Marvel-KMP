import com.sngular.marvelkmp.BuildKonfig
import io.github.alexzhirkevich.cupertino.adaptive.Theme
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.http.URLProtocol
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

actual fun isAndroid(): Boolean = true

actual fun determineTheme(): Theme = Theme.Material3
actual fun getHttpClient(): HttpClient {
    return HttpClient {
        install(ContentNegotiation) {
            json(
                Json {
                    ignoreUnknownKeys = true
                    prettyPrint = true
                    isLenient = true
                }
            )
        }

        install(Logging) {
            logger = Logger.SIMPLE
            level = LogLevel.ALL
        }

        defaultRequest {
            url {
                host = "gateway.marvel.com"
                protocol = URLProtocol.HTTPS
                parameters.append("apikey", getMarvelPublicKey())
            }
        }
    }
}

actual fun getMarvelPublicKey(): String {
    return BuildKonfig.MARVEL_PUBLIC_KEY
}

actual fun getMarvelPrivateKey(): String {
    return BuildKonfig.MARVEL_PRIVATE_KEY
}
