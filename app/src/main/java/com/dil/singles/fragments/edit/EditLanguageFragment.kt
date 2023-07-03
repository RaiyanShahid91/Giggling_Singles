package com.dil.singles.fragments.edit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.dil.singles.databinding.FragmentEditLanguageBinding
import com.dil.singles.helper.BaseFragment

class EditLanguageFragment : BaseFragment() {

    private var fragmentLanguageBinding: FragmentEditLanguageBinding? = null
    private val binding get() = fragmentLanguageBinding!!
    private val moreViewModel: EditViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentLanguageBinding =
            FragmentEditLanguageBinding.inflate(layoutInflater, container, false)
        binding.viewModel = moreViewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listener()
    }

    private fun listener() {

        moreViewModel.onBackOccured.consume(viewLifecycleOwner)
        {
            activity?.finish()
        }

        moreViewModel.onSuccessOccurred.consume(viewLifecycleOwner)
        {
            activity?.finish()
        }

        requireActivity()
            .onBackPressedDispatcher
            .addCallback(requireActivity(), object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    activity?.finish()
                }
            })
    }
    override fun onResume() {
        super.onResume()
        getLastLocation(requireContext())
    }
}