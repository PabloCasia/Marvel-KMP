package di

import getHttpClient
import org.koin.dsl.module

val provideHttpClientModule = module {
    single {
        getHttpClient()
    }
}