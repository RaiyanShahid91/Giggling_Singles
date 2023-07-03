package com.dil.singles.fragments.welcome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.dil.singles.databinding.FragmentWelcomeBinding
import com.dil.singles.fragments.register.RegisterViewModel
import com.dil.singles.helper.BaseFragment

class WelcomeFragment : BaseFragment() {

    private var fragmentWelcomeBinding: FragmentWelcomeBinding? = null
    private val binding get() = fragmentWelcomeBinding!!
    private val registerViewModel: RegisterViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentWelcomeBinding = FragmentWelcomeBinding.inflate(layoutInflater, container, false)
        binding.viewModel = registerViewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        clickEvents()
    }

    private fun clickEvents() {
        registerViewModel.navigationLiveData.observe(viewLifecycleOwner) {
            findNavController().navigate(it)
        }
    }


    override fun onResume() {
        super.onResume()
    }
}