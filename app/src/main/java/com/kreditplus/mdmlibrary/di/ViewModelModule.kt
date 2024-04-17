package com.kreditplus.mdmlibrary.di

import com.kreditplus.mdmlibrary.presentation.ProvinceViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { ProvinceViewModel(get()) }
}