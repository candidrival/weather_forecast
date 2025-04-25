package com.app.live.weather.forecast.presentation.weeks_forecast

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.live.weather.forecast.R
import com.app.live.weather.forecast.databinding.FragmentTwoWeekWeatherBinding
import com.app.live.weather.forecast.databinding.FragmentWeekWeatherBinding
import com.app.live.weather.forecast.utils.base.BaseFragment
import com.app.live.weather.forecast.utils.showToast
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class TwoWeekWeatherFragment: BaseFragment<FragmentTwoWeekWeatherBinding, ForecastViewModel>() {
    override val viewModel: ForecastViewModel by viewModel()

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentTwoWeekWeatherBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.loadForecast(
            onError = {
                showToast(requireActivity(), getString(R.string.error_message))
                findNavController().popBackStack()
            }
        )
        initUi()
    }

    private fun initUi() {
        val adapter = ForecastAdapter()
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        viewModel.forecast.observe(viewLifecycleOwner) { forecast ->
            binding.tvCity.text = forecast.city.name
            val listForecast = forecast.list
            val list = if (listForecast.size < 14) listForecast + listForecast.take(14 - listForecast.size) else listForecast
            adapter.submitList(list)
        }

        lifecycleScope.launch {
            viewModel.isLoading.collect { isLoading ->
                with(binding) {
                    progressBar.isVisible = isLoading
                    recyclerView.isGone = isLoading
                }
            }
        }
    }

    override fun initInsets() {
        observeNullable(viewModel.statusBarHeight) {
            if (it != null) { // sets android status bar height
                binding.statusBar.updateLayoutParams { height = it }
            }
        }
        observeNullable(viewModel.navigationBottomHeight) {
            if (it != null) { // sets android bottom navigation buttons height
                binding.navigationBar.updateLayoutParams { height = it }
            }
        }
    }
}