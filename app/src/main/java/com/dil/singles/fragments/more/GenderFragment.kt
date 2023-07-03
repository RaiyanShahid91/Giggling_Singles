package com.dil.singles.fragments.more

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.dil.singles.R
import com.dil.singles.databinding.FragmentGenderBinding
import com.dil.singles.fragments.more.viewModel.MoreViewModel
import com.dil.singles.helper.*

class GenderFragment : BaseFragment() {

    private var frgmentGenderBinding: FragmentGenderBinding? = null
    private val binding get() = frgmentGenderBinding!!
    private val moreViewModel: MoreViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        frgmentGenderBinding =
            FragmentGenderBinding.inflate(layoutInflater, container, false)
        binding.viewModel = moreViewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        gender()
        listener()
    }

    private fun listener() {
        moreViewModel.addGender.consume(viewLifecycleOwner)
        {
            showCustomToastSuccess(requireContext(),binding.rootLayout,"Please select gender", true)
        }

        moreViewModel.onSuccessOccurred.consume(viewLifecycleOwner)
        {
            SharedPrefData(requireContext()).saveDataString(Constants.ONBOARD_GENDER, "3")
            findNavController().navigate(R.id.nav_whome_date_fragment)
        }
    }


    private fun gender() {
        binding.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.women -> {
                    moreViewModel.gender.value = binding.women.text.toString()
                }
                R.id.men -> {
                    moreViewModel.gender.value = binding.men.text.toString()
                }
                R.id.gay -> {
                    moreViewModel.gender.value = binding.gay.text.toString()
                }
                R.id.asexual -> {
                    moreViewModel.gender.value = binding.asexual.text.toString()
                }
                R.id.bisexual -> {
                    moreViewModel.gender.value = binding.bisexual.text.toString()
                }
                R.id.lesbian -> {
                    moreViewModel.gender.value = binding.lesbian.text.toString()
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