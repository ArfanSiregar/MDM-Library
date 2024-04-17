package com.kreditplus.mdmlibrary.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kreditplus.base.data.KreditplusResponse
import com.kreditplus.data.area.api.error.BadRequestError
import com.kreditplus.data.area.api.error.InternalServerError
import com.kreditplus.data.area.api.error.NotFound
import com.kreditplus.data.area.api.error.RequestTimeoutError
import com.kreditplus.data.area.api.error.UnAuthorizationError
import com.kreditplus.data.area.api.model.Province
import com.kreditplus.data.area.api.repository.AreaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


class ProvinceViewModel @Inject constructor(
    private val areaRepository: AreaRepository
): ViewModel() {

    private val _stateFlow = MutableStateFlow(ProvinceState())

    val state get() = _stateFlow.asStateFlow()

    fun onEvent(event: ProvinceEvent) {
        when(event) {
            is ProvinceEvent.DefaultProvince -> {
                loadingProvinces()
                getProvinces()
            }
            is ProvinceEvent.SearchProvince -> {
                loadingProvinces()
                clearProvinces()
                getProvinces(event.searchQuery, _stateFlow.value.currentPage)
            }

            else -> {
                if (_stateFlow.value.isLoadMore) {
                    loadingPaginate()
                    getProvinces(_stateFlow.value.searchQuery, _stateFlow.value.nextPage)
                }
            }
        }
    }

    private fun loadingPaginate() {
        _stateFlow.update {
            it.copy(isLoadingPaginate = true)
        }
    }

    private fun loadingProvinces() {
        _stateFlow.update {
            it.copy(
                isProvincesLoading = true,
                isLoadingPaginate = false,
                isUISuccessShowing = false,
                provincesNotFound = false,
            )
        }
    }

    private fun getProvinces(search: String = "", page: Int = 1) = viewModelScope.launch {
        val response = areaRepository.areaProvince(page = page, name = search)
        handleGetProvincesResponse(response, page, search)
    }

    private fun handleGetProvincesResponse(
        response: KreditplusResponse<List<Province>>,
        page: Int,
        search: String,
    ) {
        when(response) {
            is KreditplusResponse.Success -> {
                val totalPage = response.meta["total_page"] as? Int
                val nextPage = response.meta["next_page"] as? Int
                val currentPage = (response.meta["current"] as? Int) ?: page
                val loadMore = (totalPage != null) && (currentPage < totalPage)
                _stateFlow.update {
                    it.copy(
                        isProvincesLoading = false,
                        isLoadingPaginate = false,
                        isUISuccessShowing = true,
                        isLoadMore = loadMore,
                        currentPage = currentPage,
                        nextPage = nextPage ?: currentPage,
                        searchQuery = search,
                        provinces = if (currentPage == 1) {
                            response.data
                        } else {
                            (state.value.provinces + (response.data)).distinct()
                        }
                    )
                }
            }

            is BadRequestError -> {
                _stateFlow.update {
                    it.copy(
                        isProvincesError = true,
                        provincesErrorMessage = response.message
                    )
                }
            }

            is RequestTimeoutError -> {
                _stateFlow.update {
                    it.copy(
                        isProvincesError = true,
                        provincesErrorMessage = response.message
                    )
                }
            }

            is UnAuthorizationError -> {
                _stateFlow.update {
                    it.copy(
                        isProvincesError = true,
                        provincesErrorMessage = response.message
                    )
                }
            }

            is NotFound -> {
                _stateFlow.update {
                    it.copy(
                        provincesNotFound = true,
                        isProvincesLoading = false,
                    )
                }
            }

            is InternalServerError -> {
                _stateFlow.update {
                    it.copy(
                        isProvincesError = true,
                        provincesErrorMessage = response.message
                    )
                }
            }

            else -> {
                _stateFlow.update {
                    it.copy(
                        isProvincesError = true,
                        provincesErrorMessage = "Terjadi kesalahan!"
                    )
                }
            }
        }
    }

    private fun clearProvinces() {
        _stateFlow.update {
            it.copy(
                provinces = emptyList(),
                currentPage = 1
            )
        }
    }
}