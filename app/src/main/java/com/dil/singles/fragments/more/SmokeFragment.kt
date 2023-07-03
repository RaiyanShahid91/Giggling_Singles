package com.dil.singles.fragments.more

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.dil.singles.R
import com.dil.singles.databinding.FragmentSmokeBinding
import com.dil.singles.fragments.more.viewModel.MoreViewModel
import com.dil.singles.helper.BaseFragment
import com.dil.singles.helper.Constants
import com.dil.singles.helper.SharedPrefData
import com.dil.singles.helper.locationPermission

class SmokeFragment : BaseFragment() {

    private var fragmentSmoke: FragmentSmokeBinding? = null
    private val binding get() = fragmentSmoke!!
    private val moreViewModel: MoreViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentSmoke =
            FragmentSmokeBinding.inflate(layoutInflater, container, false)
        binding.viewModel = moreViewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listener()
        smoking()
    }

    private fun listener() {

        moreViewModel.onSuccessOccurred.consume(viewLifecycleOwner)
        {
            SharedPrefData(requireContext()).saveDataString(Constants.ONBOARD_SMOKING, "7")
            findNavController().navigate(R.id.nav_religion_fragment)
        }
    }

    private fun smoking() {
        binding.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.socially -> {
                    moreViewModel.doYouSmoke.value = binding.socially.text.toString()
                }
                R.id.never -> {
                    moreViewModel.doYouSmoke.value = binding.never.text.toString()
                }
                R.id.regularly -> {
                    moreViewModel.doYouSmoke.value = binding.regularly.text.toString()
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
