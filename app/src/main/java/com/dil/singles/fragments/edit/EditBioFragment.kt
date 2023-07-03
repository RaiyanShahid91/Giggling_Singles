package com.dil.singles.fragments.edit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import com.dil.singles.databinding.FragmentEditBioBinding
import com.dil.singles.helper.BaseFragment

class EditBioFragment : BaseFragment() {

    private var bioBinding: FragmentEditBioBinding? = null
    private val binding get() = bioBinding!!
    private val moreViewModel: EditViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bioBinding =
            FragmentEditBioBinding.inflate(layoutInflater, container, false)
        binding.viewModel = moreViewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listener()
    }


    private fun listener() {

        requireActivity()
            .onBackPressedDispatcher
            .addCallback(requireActivity(), object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    activity?.finish()
                }
            })

        moreViewModel.onBackOccured.consume(viewLifecycleOwner)
        {
            activity?.finish()
        }
        moreViewModel.onSuccessOccurred.consume(viewLifecycleOwner)
        {
            activity?.finish()
        }

    }
    override fun onResume() {
        super.onResume()
        getLastLocation(requireContext())
    }
}