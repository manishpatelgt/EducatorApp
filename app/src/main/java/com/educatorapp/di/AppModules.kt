package com.educatorapp.di

import com.educatorapp.data.database.EducatorDatabase
import com.educatorapp.data.preferences.PreferencesHelper
import com.educatorapp.ui.fragments.favorites.FavoriteViewModel
import com.educatorapp.ui.fragments.videoplayer.VideoPlayViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

/** 1. Preferences MODULES ------------------------------------------------------------------------------------------------- */
val preferencesModule = module {
    single {
        PreferencesHelper(androidApplication())
    }
}

/** 2.DATABASE MODULES ------------------------------------------------------------------------------------------------ */
val databaseModule = module {

    /** Database */
    single {
        EducatorDatabase.getDatabase(androidApplication())
    }

    /** Video DAO */
    factory {
        get<EducatorDatabase>().getVideoDao()
    }
}

/** 4. VIEWMODEL MODULES ------------------------------------------------------------------------------------------------- */
val viewModelModule = module {
    viewModel {
        FavoriteViewModel(get())
    }

    viewModel {
        VideoPlayViewModel(get())
    }

    /*viewModel {
        ProfileViewModel(get())
    }*/
}

/** KOIN APP MODULES * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
val appModule = listOf(databaseModule, preferencesModule, viewModelModule)