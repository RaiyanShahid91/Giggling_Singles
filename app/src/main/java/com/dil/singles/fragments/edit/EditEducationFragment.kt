package com.dil.singles.fragments.edit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.dil.singles.R
import com.dil.singles.databinding.FragmentEditEducationBinding
import com.dil.singles.helper.BaseFragment

class EditEducationFragment : BaseFragment() {

    private var fragmentBinding: FragmentEditEducationBinding? = null
    private val binding get() = fragmentBinding!!
    private val editViewModel: EditViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentBinding = FragmentEditEducationBinding.inflate(layoutInflater, container, false)
        binding.viewModel = editViewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listener()
        education()
    }

    private fun listener()
    {
        editViewModel.onBackOccured.consume(viewLifecycleOwner)
        {
            activity?.finish()
        }

        editViewModel.onSuccessOccurred.consume(viewLifecycleOwner)
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

    private fun education() {
        binding.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.high_school -> {
                    editViewModel.education.value = binding.highSchool.text.toString()
                }
                R.id.in_college -> {
                    editViewModel.education.value = binding.inCollege.text.toString()
                }
                R.id.undergraduate -> {
                    editViewModel.education.value = binding.undergraduate.text.toString()
                }
                R.id.graduate -> {
                    editViewModel.education.value = binding.graduate.text.toString()
                }
                R.id.not_to_say -> {
                    editViewModel.education.value = binding.notToSay.text.toString()
                }
            }
        }

    }
    override fun onResume() {
        super.onResume()
        getLastLocation(requireContext())
    }
}