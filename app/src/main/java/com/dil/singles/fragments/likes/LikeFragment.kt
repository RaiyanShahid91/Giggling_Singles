package com.dil.singles.fragments.likes

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dil.singles.databinding.FragmentLikeBinding
import com.dil.singles.helper.BaseFragment

class LikeFragment : BaseFragment() {

    private var fragmentLikeBinding: FragmentLikeBinding? = null
    private val binding get() = fragmentLikeBinding!!
    private val homeViewModel: LikeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentLikeBinding = FragmentLikeBinding.inflate(layoutInflater, container, false)
        binding.viewModel = homeViewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadLikes()
    }

    private fun loadLikes() {
        val usersAdapter = LikesAdapter(requireContext(), homeViewModel.loadLikes())
        val layoutManager = LinearLayoutManager(context)
        binding.likedRecycleView.layoutManager = layoutManager
        binding.likedRecycleView.adapter = usersAdapter

        binding.searchContacts.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable) {
                if (s.isNotEmpty()) {
                    homeViewModel.textObserver.set(true)
                } else {
                    homeViewModel.textObserver.set(false)
                }
                usersAdapter.filter.filter(s)
            }
        })

        binding.closeImg.setOnClickListener {
            homeViewModel.searchText.value = ""
        }
    }

    override fun onResume() {
        super.onResume()
        getLastLocation(requireContext())
    }

}