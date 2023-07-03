package com.dil.singles.fragments.more

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.dil.singles.R
import com.dil.singles.databinding.FragmentDrinkBinding
import com.dil.singles.fragments.more.viewModel.MoreViewModel
import com.dil.singles.helper.BaseFragment
import com.dil.singles.helper.Constants
import com.dil.singles.helper.SharedPrefData
import com.dil.singles.helper.locationPermission

class DrinkFragment : BaseFragment() {

    private var fragmentDrink: FragmentDrinkBinding? = null
    private val binding get() = fragmentDrink!!
    private val moreViewModel: MoreViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentDrink =
            FragmentDrinkBinding.inflate(layoutInflater, container, false)
        binding.viewModel = moreViewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listener()
        drinking()
    }

    private fun listener() {

        moreViewModel.onSuccessOccurred.consume(viewLifecycleOwner)
        {
            SharedPrefData(requireContext()).saveDataString(Constants.ONBOARD_DRINKING, "6")
            findNavController().navigate(R.id.nav_smoke_fragment)
        }
    }

    private fun drinking() {
        binding.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.frequently -> {
                    moreViewModel.doYouDrink.value = binding.frequently.text.toString()
                }
                R.id.socially -> {
                    moreViewModel.doYouDrink.value = binding.socially.text.toString()
                }
                R.id.rarely -> {
                    moreViewModel.doYouDrink.value = binding.rarely.text.toString()
                }
                R.id.never -> {
                    moreViewModel.doYouDrink.value = binding.never.text.toString()
                }
                R.id.sober -> {
                    moreViewModel.doYouDrink.value = binding.sober.text.toString()
                }
            }
        }

    }

    override fun onResume() {
        super.onResume()
        getLastLocation(requireContext())
        moreViewModel.showLoader.set(false)
    }

}