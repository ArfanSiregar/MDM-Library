package com.kreditplus.mdmlibrary

import android.app.Application
import com.kreditplus.data.area.api.repository.AreaRepository
import com.kreditplus.mdmlibrary.di.repositoryModule
import com.kreditplus.mdmlibrary.di.viewModelModule
import dagger.hilt.android.HiltAndroidApp
import org.koin.core.context.startKoin
import javax.inject.Inject
import javax.inject.Provider

@HiltAndroidApp
class App: Application() {

    @Inject
    lateinit var areaRepository: Provider<AreaRepository>

    override fun onCreate() {
        super.onCreate()
        startKoin {
            modules(
                repositoryModule(
                    areaRepository = areaRepository
                ),
                viewModelModule
            )
        }
    }
}