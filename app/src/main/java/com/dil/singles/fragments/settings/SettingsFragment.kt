package com.dil.singles.fragments.settings

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.dil.singles.R
import com.dil.singles.activity.DashboardActivity
import com.dil.singles.databinding.FragmentSettingsBinding
import com.dil.singles.helper.BaseFragment
import com.dil.singles.helper.Constants

class SettingsFragment : BaseFragment() {

    private var fragmentSettingsBinding: FragmentSettingsBinding? = null
    private val binding get() = fragmentSettingsBinding!!
    private val settingsViewModel: SettingsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentSettingsBinding = FragmentSettingsBinding.inflate(layoutInflater, container, false)
        binding.viewModel = settingsViewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        settingsViewModel.getUserDetails(firebaseAuth.uid.toString(), requireContext())
        listener()
    }

    private fun listener() {
        settingsViewModel.logoutObserver.consume(viewLifecycleOwner)
        {
            val intent = Intent(context, DashboardActivity::class.java)
            intent.putExtra(Constants.LOGOUT, Constants.LOGOUT)
            context?.startActivity(intent)
            activity?.finishAffinity()
        }

        settingsViewModel.onBackPressed.consume(viewLifecycleOwner)
        {
            activity?.finish()
        }

        settingsViewModel.blockedObserver.consume(viewLifecycleOwner)
        {
            findNavController().navigate(R.id.nav_blocked)
        }

        binding.changePasswordLabel.setOnClickListener {
            findNavController().navigate(R.id.nav_edit_change_password)
        }
    }

    override fun onResume() {
        super.onResume()
        getLastLocation(requireContext())
    }
}