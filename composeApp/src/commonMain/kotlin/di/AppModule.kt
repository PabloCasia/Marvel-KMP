package di

fun appModule() = listOf(provideHttpClientModule, provideRepositoryModule, provideViewModelModule)
