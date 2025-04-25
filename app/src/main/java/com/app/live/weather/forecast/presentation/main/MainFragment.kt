package com.app.live.weather.forecast.presentation.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.updateLayoutParams
import androidx.navigation.fragment.findNavController
import com.app.live.weather.forecast.R
import com.app.live.weather.forecast.databinding.FragmentMainBinding
import com.app.live.weather.forecast.utils.base.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainFragment: BaseFragment<FragmentMainBinding, MainViewModel>() {

    override val viewModel: MainViewModel by viewModel()

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentMainBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
    }

    private fun initUi() {
        binding.run {
            btnSelectCity.setOnClickListener {
                findNavController().navigate(R.id.selectCityFragment)
            }
            btnTodayWeather.setOnClickListener {
                findNavController().navigate(R.id.todayWeatherFragment)
            }
            btnWeekWeather.setOnClickListener {
                findNavController().navigate(R.id.weekWeatherFragment)
            }
            btnTwoWeekWeather.setOnClickListener {
                findNavController().navigate(R.id.twoWeekWeatherFragment)
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