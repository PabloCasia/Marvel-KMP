import io.github.alexzhirkevich.cupertino.adaptive.Theme
import io.ktor.client.HttpClient
import io.ktor.client.engine.darwin.Darwin
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.http.URLProtocol
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import utils.marvelPublicApiKey

actual fun isAndroid(): Boolean = false

actual fun determineTheme(): Theme = Theme.Cupertino
actual fun getHttpClient(): HttpClient {
    return HttpClient(Darwin) {
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
                parameters.append("apikey", marvelPublicApiKey)
            }
        }
    }
}