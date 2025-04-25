package com.app.live.weather.forecast.presentation.select_city

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.updateLayoutParams
import androidx.navigation.fragment.findNavController
import com.app.live.weather.forecast.R
import com.app.live.weather.forecast.databinding.FragmentSelectCityBinding
import com.app.live.weather.forecast.utils.base.BaseFragment
import com.app.live.weather.forecast.utils.showToast
import org.koin.androidx.viewmodel.ext.android.viewModel

class SelectCityFragment: BaseFragment<FragmentSelectCityBinding, SelectCityViewModel>() {
    override val viewModel: SelectCityViewModel by viewModel()

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    )= FragmentSelectCityBinding.inflate(inflater, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
    }

    private fun initUi() {
        binding.run {
            btnSave.setOnClickListener {
                val city = etCity.text.toString().trim()
                if (city.isNotEmpty()) {
                    viewModel.putCity(city)
                    findNavController().popBackStack()
                } else {
                   showToast(requireContext(), getString(R.string.please_enter_city))
                }
            }
            etCity.setText(viewModel.getCity() ?: "")
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