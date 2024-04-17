package com.kreditplus.mdmlibrary.presentation

import androidx.lifecycle.ViewModel
import javax.inject.Inject
import javax.inject.Provider

class ViewModelFactory @Inject constructor(
    private val viewModelProviders: MutableMap<Class<out ViewModel>, Provider<ViewModel>>
): ViewModelProviderFactory {
    override fun <T : ViewModel> getViewModel(viewModelClass: Class<T>): T {
        val provider = viewModelProviders[viewModelClass]
            ?: viewModelProviders.entries.firstOrNull { viewModelClass.isAssignableFrom(it.key) }?.value
            ?: throw IllegalArgumentException("Unknown ViewModel class: $viewModelClass")

        @Suppress("UNCHECKED_CAST")
        return provider.get() as T
    }
}