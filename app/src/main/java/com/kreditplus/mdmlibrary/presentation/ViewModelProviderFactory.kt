package com.kreditplus.mdmlibrary.presentation

import androidx.lifecycle.ViewModel

interface ViewModelProviderFactory {
    fun <T : ViewModel> getViewModel(viewModelClass: Class<T>): T
}