package di

import network.repository.CharacterRepository
import network.repository.CharacterRepositoryImpl
import network.repository.ComicRepository
import network.repository.ComicRepositoryImpl
import org.koin.dsl.module

val provideRepositoryModule = module {
    single<CharacterRepository> { CharacterRepositoryImpl(get()) }
    single<ComicRepository> { ComicRepositoryImpl(get()) }
}
