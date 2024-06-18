package di

import network.repository.CharacterRepository
import network.repository.CharacterRepositoryImpl
import org.koin.dsl.module

val provideRepositoryModule = module {
    single<CharacterRepository> { CharacterRepositoryImpl(get()) }
}