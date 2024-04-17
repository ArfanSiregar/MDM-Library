package com.kreditplus.mdmlibrary.presentation

import com.kreditplus.data.area.api.model.Province

data class ProvinceState(
    val isProvincesLoading: Boolean = true,
    val isLoadingPaginate: Boolean = false,
    val isLoadMore: Boolean = false,
    val isProvincesError: Boolean = false,
    val provincesErrorMessage: String = "",
    val isUISuccessShowing: Boolean = false,
    val currentPage: Int = 1,
    val nextPage: Int = 1,
    val searchQuery: String = "",
    val provinces: List<Province> = emptyList(),
    val provincesNotFound: Boolean = false,
)
