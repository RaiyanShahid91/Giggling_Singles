package com.dil.singles.fragments.more

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.dil.singles.R
import com.dil.singles.databinding.FragmentWhatsLookingBinding
import com.dil.singles.fragments.more.viewModel.MoreViewModel
import com.dil.singles.helper.BaseFragment
import com.dil.singles.helper.Constants
import com.dil.singles.helper.SharedPrefData
import com.dil.singles.helper.locationPermission

class WhatsLookingFragment : BaseFragment() {

    private var fragmentWhatsLookingFor: FragmentWhatsLookingBinding? = null
    private val binding get() = fragmentWhatsLookingFor!!
    private val moreViewModel: MoreViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentWhatsLookingFor =
            FragmentWhatsLookingBinding.inflate(layoutInflater, container, false)
        binding.viewModel = moreViewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listener()
        whatsLookingFor()
    }

    private fun listener() {

        moreViewModel.onSuccessOccurred.consume(viewLifecycleOwner)
        {
            SharedPrefData(requireContext()).saveDataString(Constants.ONBOARD_WHATS_LOOKING_FOR, "5")
            findNavController().navigate(R.id.nav_drink_fragment)
        }
    }

    private fun whatsLookingFor() {
        binding.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.relationship -> {
                    moreViewModel.whatsLookingFor.value = binding.relationship.text.toString()
                }
                R.id.something_casual -> {
                    moreViewModel.whatsLookingFor.value = binding.somethingCasual.text.toString()
                }
                R.id.m_not_sure -> {
                    moreViewModel.whatsLookingFor.value = binding.mNotSure.text.toString()
                }
                R.id.prefer_not_to_say -> {
                    moreViewModel.whatsLookingFor.value = binding.preferNotToSay.text.toString()
                }
            }
        }

    }

    override fun onResume() {
        super.onResume()
        moreViewModel.showLoader.set(false)
        getLastLocation(requireContext())
    }

}