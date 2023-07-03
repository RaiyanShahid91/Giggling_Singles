package com.dil.singles.fragments.register

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.dil.singles.R
import com.dil.singles.activity.MainActivity
import com.dil.singles.databinding.FragmentRegisterBinding
import com.dil.singles.helper.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener


class RegisterFragment : BaseFragment() {

    private var fragmentRegisterBinding: FragmentRegisterBinding? = null
    private val binding get() = fragmentRegisterBinding!!
    private val registerViewModel: RegisterViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentRegisterBinding = FragmentRegisterBinding.inflate(layoutInflater, container, false)
        binding.viewModel = registerViewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        clickEvents()
    }

    private fun clickEvents() {
        registerViewModel.navigationLiveData.observe(viewLifecycleOwner)
        {
            findNavController().navigate(it)
        }

        registerViewModel.onSuccessOccurred.consume(viewLifecycleOwner)
        {
            firebaseDatabase.reference.child(Constants.USERS).child(firebaseAuth.uid.toString())
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        for (data in dataSnapshot.children) {
                            when {
                                !dataSnapshot.child(Constants.AGREE).exists() -> {
                                    findNavController().navigate(R.id.nav_Welcome_Agree_fragment)
                                }
                                !dataSnapshot.child(Constants.FIRSTNAME).exists() -> {
                                    findNavController().navigate(R.id.nav_add_name_fragment)
                                }
                                !dataSnapshot.child(Constants.LASTNAME).exists() -> {
                                    findNavController().navigate(R.id.nav_add_name_fragment)
                                }
                                !dataSnapshot.child(Constants.PROFILEPHOTO).exists() -> {
                                    findNavController().navigate(R.id.nav_add_photo_fragment)
                                }
                                !dataSnapshot.child(Constants.BACKGROUNDPHOTO).exists() -> {
                                    findNavController().navigate(R.id.nav_add_photo_fragment)
                                }
                                !dataSnapshot.child(Constants.DATEOFBIRTH).exists() -> {
                                    findNavController().navigate(R.id.nav_date_of_birth_fragment)
                                }
                                !dataSnapshot.child(Constants.GENDER).exists() -> {
                                    findNavController().navigate(R.id.nav_gender_fragment)
                                }
                                !dataSnapshot.child(Constants.WHOMTODATE).exists() -> {
                                    findNavController().navigate(R.id.nav_whome_date_fragment)
                                }
                                !dataSnapshot.child(Constants.WHATSLOOKINGFOR).exists() -> {
                                    findNavController().navigate(R.id.nav_whats_looking_fragment)
                                }
                                !dataSnapshot.child(Constants.DRINKING).exists() -> {
                                    findNavController().navigate(R.id.nav_drink_fragment)
                                }
                                !dataSnapshot.child(Constants.SMOKING).exists() -> {
                                    findNavController().navigate(R.id.nav_smoke_fragment)
                                }
                                !dataSnapshot.child(Constants.RELIGION).exists() -> {
                                    findNavController().navigate(R.id.nav_religion_fragment)
                                }
                                !dataSnapshot.child(Constants.HEIGHT).exists() -> {
                                    findNavController().navigate(R.id.nav_height_fragment)
                                }
                                !dataSnapshot.child(Constants.Zodiac).exists() -> {
                                    findNavController().navigate(R.id.nav_zodiac_fragment)
                                }
                                !dataSnapshot.child(Constants.EDUCATION).exists() -> {
                                    findNavController().navigate(R.id.nav_education_fragment)
                                }
                                !dataSnapshot.child(Constants.PASSION).exists() -> {
                                    findNavController().navigate(R.id.nav_your_interested_fragment)
                                }
                                !dataSnapshot.child(Constants.LANGUAGE).exists() -> {
                                    findNavController().navigate(R.id.nav_language_fragment)
                                }
                                !dataSnapshot.child(Constants.BIO).exists() -> {
                                    findNavController().navigate(R.id.nav_bio_fragment)
                                }
                                else -> {
                                    val intent = Intent(context, MainActivity::class.java)
                                    activity?.startActivity(intent)
                                    activity?.finishAffinity()
                                }
                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                    }
                })
        }

        binding.back.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.signInBtn.setOnClickListener {
            hideKeyboardWithout(requireContext(), binding.tfFilledPassword)
            registerViewModel.register(requireContext(), registerViewModel.emailMutableData.value.toString(),
                registerViewModel.passwordMutableData.value.toString())
        }
    }

    override fun onResume() {
        super.onResume()
        registerViewModel.showLoader.set(false)
    }
}