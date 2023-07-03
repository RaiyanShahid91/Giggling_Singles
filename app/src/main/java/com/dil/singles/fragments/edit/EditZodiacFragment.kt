package com.dil.singles.fragments.edit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.dil.singles.R
import com.dil.singles.databinding.FragmentEditZodiacBinding
import com.dil.singles.helper.BaseFragment

class EditZodiacFragment : BaseFragment() {

    private var zodiacBinding: FragmentEditZodiacBinding? = null
    private val binding get() = zodiacBinding!!
    private val moreViewModel: EditViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        zodiacBinding =
            FragmentEditZodiacBinding.inflate(layoutInflater, container, false)
        binding.viewModel = moreViewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listener()
        zodiac()
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

    private fun zodiac() {
        binding.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.aries -> {
                    moreViewModel.zodiac.value = binding.aries.text.toString()
                }
                R.id.taurus -> {
                    moreViewModel.zodiac.value = binding.taurus.text.toString()
                }
                R.id.gemini -> {
                    moreViewModel.zodiac.value = binding.gemini.text.toString()
                }
                R.id.cancer -> {
                    moreViewModel.zodiac.value = binding.cancer.text.toString()
                }
                R.id.leo -> {
                    moreViewModel.zodiac.value = binding.leo.text.toString()
                }
                R.id.virgo -> {
                    moreViewModel.zodiac.value = binding.virgo.text.toString()
                }
                R.id.libra -> {
                    moreViewModel.zodiac.value = binding.libra.text.toString()
                }
                R.id.scorpio -> {
                    moreViewModel.zodiac.value = binding.scorpio.text.toString()
                }
                R.id.sagittarius -> {
                    moreViewModel.zodiac.value = binding.sagittarius.text.toString()
                }
                R.id.capricorn -> {
                    moreViewModel.zodiac.value = binding.capricorn.text.toString()
                }
                R.id.aquarius -> {
                    moreViewModel.zodiac.value = binding.aquarius.text.toString()
                }
                R.id.pisces -> {
                    moreViewModel.zodiac.value = binding.pisces.text.toString()
                }
            }
        }

    }

    override fun onResume() {
        super.onResume()
        getLastLocation(requireContext())
    }
}