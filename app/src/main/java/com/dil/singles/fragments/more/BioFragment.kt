package com.dil.singles.fragments.more

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.dil.singles.BuildConfig
import com.dil.singles.activity.MainActivity
import com.dil.singles.databinding.FragmentBioBinding
import com.dil.singles.fragments.more.viewModel.MoreViewModel
import com.dil.singles.helper.BaseFragment
import com.dil.singles.helper.Constants
import com.dil.singles.helper.Constants.VERSION
import com.dil.singles.helper.SharedPrefData
import com.dil.singles.helper.locationPermission

class BioFragment : BaseFragment() {

    private var fragmentBioBinding: FragmentBioBinding? = null
    private val binding get() = fragmentBioBinding!!
    private val moreViewModel: MoreViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentBioBinding =
            FragmentBioBinding.inflate(layoutInflater, container, false)
        binding.viewModel = moreViewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listener()
    }

    private fun listener() {
        moreViewModel.onSuccessOccurred.consume(viewLifecycleOwner)
        {
            SharedPrefData(requireContext()).saveDataString(VERSION, BuildConfig.VERSION_NAME)
            val intent = Intent(context, MainActivity::class.java)
            context?.startActivity(intent)
        }

        moreViewModel.onSkip.consume(viewLifecycleOwner) {
            SharedPrefData(requireContext()).saveDataString(VERSION, BuildConfig.VERSION_NAME)
            val intent = Intent(context, MainActivity::class.java)
            context?.startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        moreViewModel.showLoader.set(false)
        getLastLocation(requireContext())
    }

}