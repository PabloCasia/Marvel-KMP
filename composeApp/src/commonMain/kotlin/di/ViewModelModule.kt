package di

import org.koin.dsl.module
import viewmodel.CharacterListViewModel

val provideViewModelModule = module {
    single {
        CharacterListViewModel(get())
    }
}