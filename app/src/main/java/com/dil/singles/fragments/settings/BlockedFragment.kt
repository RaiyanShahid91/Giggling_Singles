package com.dil.singles.fragments.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.dil.singles.databinding.FragmentBlockedBinding
import com.dil.singles.databinding.FragmentSettingsBinding
import com.dil.singles.fragments.blocked.BlockedAdapter
import com.dil.singles.fragments.blocked.BlockedModel
import com.dil.singles.helper.BaseFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class BlockedFragment : BaseFragment() {

    private var fragmentBlockedBinding: FragmentBlockedBinding? = null
    private val binding get() = fragmentBlockedBinding!!
    private val settingsViewModel: SettingsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentBlockedBinding = FragmentBlockedBinding.inflate(layoutInflater, container, false)
        binding.viewModel = settingsViewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listener()
        loadBlockedUser()
    }

    private fun listener()
    {
        settingsViewModel.onBackPressed.consume(viewLifecycleOwner)
        {
            findNavController().popBackStack()
        }
    }

    private fun loadBlockedUser() {
        settingsViewModel.showLoader.set(true)
        val users: ArrayList<BlockedModel> = ArrayList()
        val usersAdapter = BlockedAdapter(requireContext(), users)
        FirebaseDatabase.getInstance().reference.child("Blocked")
            .child(FirebaseAuth.getInstance().uid.toString())
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    users.clear()
                    if (snapshot.exists()) {
                        for (snapshot in snapshot.children) {
                            settingsViewModel.showLoader.set(false)
                            settingsViewModel.noBlocked.set(false)
                            val user = snapshot.getValue(BlockedModel::class.java)
                            user?.let { users.add(it) }
                            usersAdapter.notifyDataSetChanged()
                        }
                    } else {
                        settingsViewModel.noBlocked.set(true)
                        settingsViewModel.showLoader.set(false)
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            })
        val layoutManager = LinearLayoutManager(requireContext())
        binding.blockedList.layoutManager = layoutManager
        binding.blockedList.adapter = usersAdapter
    }

    override fun onResume() {
        super.onResume()
        getLastLocation(requireContext())
    }
}