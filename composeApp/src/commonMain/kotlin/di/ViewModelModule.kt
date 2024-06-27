package di

import org.koin.dsl.module
import viewmodel.CharacterDetailViewModel
import viewmodel.CharacterListViewModel

val provideViewModelModule = module {
    single {
        CharacterListViewModel(get())
    }
    single {
        CharacterDetailViewModel(get(), get())
    }
}
