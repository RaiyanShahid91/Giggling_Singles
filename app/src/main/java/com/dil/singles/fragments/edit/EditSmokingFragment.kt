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
import com.dil.singles.databinding.FragmentEditSmokingBinding
import com.dil.singles.helper.BaseFragment

class EditSmokingFragment : BaseFragment() {

    private var fragmentSmokeBinding: FragmentEditSmokingBinding? = null
    private val binding get() = fragmentSmokeBinding!!
    private val viewModel: EditViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentSmokeBinding = FragmentEditSmokingBinding.inflate(layoutInflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listener()
        smoking()
    }

    private fun listener() {
        viewModel.onBackOccured.consume(viewLifecycleOwner) {
            activity?.finish()
        }

        viewModel.onSuccessOccurred.consume(viewLifecycleOwner)
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

    private fun smoking() {
        binding.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.socially -> {
                    viewModel.doYouSmoke.value = binding.socially.text.toString()
                }
                R.id.never -> {
                    viewModel.doYouSmoke.value = binding.never.text.toString()
                }
                R.id.regularly -> {
                    viewModel.doYouSmoke.value = binding.regularly.text.toString()
                }
            }
        }

    }
    override fun onResume() {
        super.onResume()
        getLastLocation(requireContext())
    }
}