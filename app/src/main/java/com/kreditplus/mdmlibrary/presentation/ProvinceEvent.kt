package com.kreditplus.mdmlibrary.presentation

sealed class ProvinceEvent {
    data object DefaultProvince: ProvinceEvent()
    data class SearchProvince(val searchQuery: String = "") : ProvinceEvent()
    data object LoadMore: ProvinceEvent()
}