package di

import getHttpClient
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.http.URLProtocol
import io.ktor.serialization.kotlinx.json.json
import isAndroid
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.json.Json
import org.koin.dsl.module
import utils.marvelPublicApiKey


val provideHttpClientModule = module {
    single {
        getHttpClient()
    }
}