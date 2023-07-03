package com.dil.singles.fragments.more

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.dil.singles.R
import com.dil.singles.databinding.FragmentWhomeToDateBinding
import com.dil.singles.fragments.more.viewModel.MoreViewModel
import com.dil.singles.helper.BaseFragment
import com.dil.singles.helper.Constants
import com.dil.singles.helper.SharedPrefData

class WhomToDateFragment : BaseFragment() {

    private var fragmentWhomToDate: FragmentWhomeToDateBinding? = null
    private val binding get() = fragmentWhomToDate!!
    private val moreViewModel: MoreViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentWhomToDate =
            FragmentWhomeToDateBinding.inflate(layoutInflater, container, false)
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
        moreViewModel.onSuccessOccurred.consume(viewLifecycleOwner)
        {
            SharedPrefData(requireContext()).saveDataString(Constants.ONBOARD_WHOM_TO_DATE, "4")
            findNavController().navigate(R.id.nav_whats_looking_fragment)
        }
    }


    private fun gender() {
        binding.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.date_women -> {
                    moreViewModel.whomeToDate.value = binding.dateWomen.text.toString()
                }
                R.id.date_men -> {
                    moreViewModel.whomeToDate.value = binding.dateMen.text.toString()
                }
                R.id.date_everyone -> {
                    moreViewModel.whomeToDate.value = binding.dateEveryone.text.toString()
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