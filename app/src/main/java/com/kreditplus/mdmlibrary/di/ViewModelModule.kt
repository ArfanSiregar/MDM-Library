package com.kreditplus.mdmlibrary.di

import androidx.lifecycle.ViewModel
import com.kreditplus.mdmlibrary.data.util.ViewModelKey
import com.kreditplus.mdmlibrary.presentation.ProvinceViewModel
import com.kreditplus.mdmlibrary.presentation.ViewModelFactory
import com.kreditplus.mdmlibrary.presentation.ViewModelProviderFactory
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoMap

@Module
@InstallIn(SingletonComponent::class)
abstract class ViewModelModule {
    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProviderFactory

    @Binds
    @IntoMap
    @ViewModelKey(ProvinceViewModel::class)
    abstract fun bindProvinceViewModel(provinceViewModel: ProvinceViewModel): ViewModel
}