package com.app.live.weather.forecast.presentation.today_weather

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.app.live.weather.forecast.R
import com.app.live.weather.forecast.databinding.FragmentTodayWeatherBinding
import com.app.live.weather.forecast.utils.base.BaseFragment
import com.app.live.weather.forecast.utils.showToast
import com.bumptech.glide.Glide
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TodayWeatherFragment : BaseFragment<FragmentTodayWeatherBinding, TodayWeatherViewModel>() {
    override val viewModel: TodayWeatherViewModel by viewModel()

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentTodayWeatherBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.loadWeather(
            onError = {
                showToast(requireActivity(), getString(R.string.error_message))
                findNavController().popBackStack()
            }
        )
        initUi()
    }

    private fun initUi() {
        viewModel.weather.observe(viewLifecycleOwner) { weather ->
            with(binding) {
                tvCity.text = weather.name
                tvTemperature.text = getString(R.string.temperature_today, weather.main.temp.toString())
                tvDescription.text = weather.weather.firstOrNull()?.description ?: ""
                tvWind.text = getString(R.string.wind_today, weather.wind.speed.toString())
                tvHumidity.text = getString(R.string.humidity_today, weather.main.humidity.toString())
                tvClouds.text = getString(R.string.clouds_today, weather.clouds.all.toString())
                tvDate.text = formatDate(weather.dt)
                val icon = weather.weather.firstOrNull()?.icon ?: "01d"
                val iconUrl = "https://openweathermap.org/img/wn/${icon}@4x.png"
                Glide.with(requireContext()).load(iconUrl).into(ivWeatherIcon)
            }
        }
        lifecycleScope.launch {
            viewModel.isLoading.collect { isLoading ->
                with(binding) {
                    progressBar.isVisible = isLoading
                    todayWeatherLayout.isGone = isLoading
                }
            }
        }
    }

    private fun formatDate(time: Long): String {
        val date = Date(time)
        val format = SimpleDateFormat("yyyy.MM.dd HH:mm", Locale.US)
        return format.format(date)
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