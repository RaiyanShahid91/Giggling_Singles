package com.dil.singles.fragments.changepassword

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.dil.singles.databinding.FragmentChangePasswordBinding
import com.dil.singles.fragments.more.viewModel.MoreViewModel
import com.dil.singles.helper.BaseFragment
import com.dil.singles.helper.showCustomToastSuccess

class ChangePassword : BaseFragment() {

    private var fragmentChangePassword: FragmentChangePasswordBinding? = null
    private val binding get() = fragmentChangePassword!!
    private val moreViewModel: MoreViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentChangePassword =
            FragmentChangePasswordBinding.inflate(layoutInflater, container, false)
        binding.viewModel = moreViewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listener()
    }

    private fun listener() {
        binding.back.setOnClickListener {
            findNavController().popBackStack()
        }

        moreViewModel.onSuccessOccurred.consume(viewLifecycleOwner)
        {
            showCustomToastSuccess(
                requireContext(),
                binding.rootLayout,
                "Password reset link has been sent on this email.",
                false
            )
        }


        moreViewModel.onFailedOccurred.consume(viewLifecycleOwner)
        {
            showCustomToastSuccess(
                requireContext(),
                binding.rootLayout,
                "Something went wrong. Try again.",
                true
            )
        }
//        binding.etEmail.addTextChangedListener(object : TextWatcher {
//            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
//                if (isValidEmail(moreViewModel.email.value.toString())) {
//                    moreViewModel.emailErrorEvent.set(false)
//                } else {
//                    moreViewModel.emailErrorEvent.set(true)
//                    moreViewModel. errorString.value = "Please enter valid email."
//                }
//                if (s.isEmpty()) {
//                    moreViewModel.emailErrorEvent.set(false)
//                }
//            }
//
//            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
//            override fun afterTextChanged(s: Editable) {}
//        })

    }

    override fun onResume() {
        super.onResume()
        moreViewModel.showLoader.set(false)
    }

}