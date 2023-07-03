package com.dil.singles.fragments.edit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.dil.singles.R
import com.dil.singles.databinding.FragmentEditDrinkingBinding
import com.dil.singles.helper.BaseFragment

class EditDrinkingFragment : BaseFragment() {

    private var fragmentDrinkBinding: FragmentEditDrinkingBinding? = null
    private val binding get() = fragmentDrinkBinding!!
    private val moreViewModel: EditViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentDrinkBinding =
            FragmentEditDrinkingBinding.inflate(layoutInflater, container, false)
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
            activity?.finish()
        }

        moreViewModel.onBackOccured.consume(viewLifecycleOwner)
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
    }
}