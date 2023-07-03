package com.dil.singles.fragments.more

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.dil.singles.R
import com.dil.singles.databinding.FragmentRelegionBinding
import com.dil.singles.fragments.more.viewModel.MoreViewModel
import com.dil.singles.helper.BaseFragment
import com.dil.singles.helper.Constants
import com.dil.singles.helper.SharedPrefData
import com.dil.singles.helper.locationPermission

class RelegionFragment : BaseFragment() {

    private var relegionBinidng: FragmentRelegionBinding? = null
    private val binding get() = relegionBinidng!!
    private val moreViewModel: MoreViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        relegionBinidng =
            FragmentRelegionBinding.inflate(layoutInflater, container, false)
        binding.viewModel = moreViewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listener()
        religion()
    }

    private fun listener() {

        moreViewModel.onSuccessOccurred.consume(viewLifecycleOwner)
        {
            SharedPrefData(requireContext()).saveDataString(Constants.ONBOARD_RELIGION, "8")
            findNavController().navigate(R.id.nav_height_fragment)
        }
    }

    private fun religion() {
        binding.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.agnostic -> {
                    moreViewModel.religion.value = binding.agnostic.text.toString()
                }
                R.id.atheist -> {
                    moreViewModel.religion.value = binding.atheist.text.toString()
                }
                R.id.buddhist -> {
                    moreViewModel.religion.value = binding.buddhist.text.toString()
                }
                R.id.catholic -> {
                    moreViewModel.religion.value = binding.catholic.text.toString()
                }
                R.id.christian -> {
                    moreViewModel.religion.value = binding.christian.text.toString()
                }
                R.id.hindu -> {
                    moreViewModel.religion.value = binding.hindu.text.toString()
                }
                R.id.jain -> {
                    moreViewModel.religion.value = binding.jain.text.toString()
                }
                R.id.jewish -> {
                    moreViewModel.religion.value = binding.jewish.text.toString()
                }
                R.id.mormon -> {
                    moreViewModel.religion.value = binding.mormon.text.toString()
                }
                R.id.muslim -> {
                    moreViewModel.religion.value = binding.muslim.text.toString()
                }
                R.id.sikh -> {
                    moreViewModel.religion.value = binding.sikh.text.toString()
                }
                R.id.spiritual -> {
                    moreViewModel.religion.value = binding.spiritual.text.toString()
                }
                R.id.prefer_not_to_say -> {
                    moreViewModel.religion.value = binding.preferNotToSay.text.toString()
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