package com.kreditplus.mdmlibrary.di

import com.kreditplus.data.area.api.repository.AreaRepository
import org.koin.dsl.module
import javax.inject.Provider

fun repositoryModule(
    areaRepository: Provider<AreaRepository>
) = module {
    single<AreaRepository> { areaRepository.get() }
}