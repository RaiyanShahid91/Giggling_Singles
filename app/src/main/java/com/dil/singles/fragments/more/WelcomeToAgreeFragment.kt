package com.dil.singles.fragments.more

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.dil.singles.R
import com.dil.singles.databinding.FragmentWelcomeToAgreeBinding
import com.dil.singles.fragments.more.viewModel.MoreViewModel
import com.dil.singles.helper.BaseFragment
import com.dil.singles.helper.locationPermission

class WelcomeToAgreeFragment : BaseFragment() {

    private var fragmentWelcomeBinding: FragmentWelcomeToAgreeBinding? = null
    private val binding get() = fragmentWelcomeBinding!!
    private val moreViewModel: MoreViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentWelcomeBinding = FragmentWelcomeToAgreeBinding.inflate(layoutInflater, container, false)
        binding.viewModel = moreViewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        clickEvent()
    }

    private fun clickEvent() {
        moreViewModel.onSuccessOccurred.consume(viewLifecycleOwner) {
            findNavController().navigate(R.id.nav_add_name_fragment)
        }
    }
    override fun onResume() {
        super.onResume()
        getLastLocation(requireContext())
        moreViewModel.showLoader.set(false)
    }

}