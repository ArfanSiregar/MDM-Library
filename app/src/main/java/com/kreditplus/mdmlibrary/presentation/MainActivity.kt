package com.kreditplus.mdmlibrary.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.kreditplus.data.area.api.model.Province
import com.kreditplus.mdmlibrary.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var viewModelFactory: ViewModelProviderFactory

    private val provinceViewModel: ProvinceViewModel by lazy {
        viewModelFactory.getViewModel(ProvinceViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
        initData()
        initObserver()
    }

    private fun initView() {
        binding.etSearch.apply {
            callbackFlow {
                val textWatcher = doAfterTextChanged {
                    trySend(editableText.toString())
                }

                awaitClose { removeTextChangedListener(textWatcher) }
            }.onEach { searchQuery ->
                provinceViewModel.onEvent(
                    when {
                        searchQuery.isNotBlank() -> ProvinceEvent.SearchProvince(searchQuery = searchQuery)
                        else -> ProvinceEvent.DefaultProvince
                    }
                )
            }.filter { textSearch ->
                textSearch.isNotBlank()
            }.debounce {
                200L
            }.launchIn(
                scope = lifecycleScope
            )
        }
    }

    private fun initData() {
        provinceViewModel.onEvent(ProvinceEvent.DefaultProvince)
    }

    private fun initObserver() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                provinceViewModel.state.collect {
                    renderProvinceSuccessResponse(it.provinces)
                    renderProvinceLoadingResponse(it.isProvincesLoading)
                }
            }
        }
    }

    private fun renderProvinceSuccessResponse(provinces: List<Province>) {
        binding.tvValue.text = provinces.toString()
    }

    private fun renderProvinceLoadingResponse(provincesLoading: Boolean) {
        binding.pbLoading.isVisible = provincesLoading
    }
}