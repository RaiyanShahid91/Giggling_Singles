package com.dil.singles.fragments.splashscreen

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.dil.singles.R
import com.dil.singles.activity.MainActivity
import com.dil.singles.databinding.FragmentSplashScreenBinding
import com.dil.singles.fragments.register.RegisterViewModel
import com.dil.singles.helper.BaseFragment
import com.dil.singles.helper.Constants
import com.dil.singles.helper.Constants.BANNED
import com.dil.singles.helper.Constants.EMAIL
import com.dil.singles.helper.Constants.PASSWORD
import com.dil.singles.helper.SharedPrefData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class SplashScreenFragment : BaseFragment() {

    private var fragmentSplashBinding: FragmentSplashScreenBinding? = null
    private val loginViewModel: RegisterViewModel by viewModels()
    private val ref = firebaseDatabase.reference.child(Constants.USERS)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentSplashBinding =
            FragmentSplashScreenBinding.inflate(layoutInflater, container, false)
        return fragmentSplashBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Handler().postDelayed(Runnable {
            if (context?.let { SharedPrefData(it).getDataString(EMAIL) } != ""
                && context?.let { SharedPrefData(it).getDataString(PASSWORD) } != ""
            ) {
                context?.let {
                    loginViewModel.register(
                        it, context?.let { SharedPrefData(it).getDataString(EMAIL) }.toString(),
                        context?.let { SharedPrefData(it).getDataString(PASSWORD) }.toString()
                    )
                }
            } else {
                findNavController().navigate(R.id.nav_welcome_fragment)
            }
        }, 5000)

        loginViewModel.onSuccessOccurred.consume(viewLifecycleOwner)
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
                                    context?.startActivity(intent)
                                    activity?.finishAffinity()
                                }
                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                    }
                })
        }
    }


}