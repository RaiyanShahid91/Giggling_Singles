package com.dil.singles.fragments.more

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.dil.singles.R
import com.dil.singles.databinding.FragmentEducationBinding
import com.dil.singles.fragments.more.viewModel.MoreViewModel
import com.dil.singles.helper.BaseFragment
import com.dil.singles.helper.Constants
import com.dil.singles.helper.SharedPrefData
import com.dil.singles.helper.locationPermission

class EducationFragment : BaseFragment() {

    private var fragmentEducationBinding: FragmentEducationBinding? = null
    private val binding get() = fragmentEducationBinding!!
    private val moreViewModel: MoreViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentEducationBinding =
            FragmentEducationBinding.inflate(layoutInflater, container, false)
        binding.viewModel = moreViewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listener()
        education()
    }

    private fun listener() {
        moreViewModel.onSuccessOccurred.consume(viewLifecycleOwner)
        {
            SharedPrefData(requireContext()).saveDataString(Constants.ONBOARD_EDUCATION, "11")
            findNavController().navigate(R.id.nav_your_interested_fragment)
        }
    }

    private fun education() {
        binding.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.high_school -> {
                    moreViewModel.education.value = binding.highSchool.text.toString()
                }
                R.id.in_college -> {
                    moreViewModel.education.value = binding.inCollege.text.toString()
                }
                R.id.undergraduate -> {
                    moreViewModel.education.value = binding.undergraduate.text.toString()
                }
                R.id.graduate -> {
                    moreViewModel.education.value = binding.graduate.text.toString()
                }
                R.id.not_to_say -> {
                    moreViewModel.education.value = binding.notToSay.text.toString()
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