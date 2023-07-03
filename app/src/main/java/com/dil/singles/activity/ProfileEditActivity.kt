package com.dil.singles.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.dil.singles.R
import com.dil.singles.helper.Constants
import com.dil.singles.helper.Constants.BIO

class ProfileEditActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_edit)
        val id = intent.getStringExtra("id")
        if (id != null) {
            when (id) {
                BIO -> Navigation.findNavController(this, R.id.my_nav_host_profile)
                    .navigate(R.id.nav_change_bio_fragment)
                Constants.PASSION -> Navigation.findNavController(this, R.id.my_nav_host_profile)
                    .navigate(R.id.nav_change_interested_fragment)
                Constants.EDUCATION -> Navigation.findNavController(this, R.id.my_nav_host_profile)
                    .navigate(R.id.nav_change_education_fragment)
                Constants.HEIGHT -> Navigation.findNavController(this, R.id.my_nav_host_profile)
                    .navigate(R.id.nav_change_Height_fragment)
                Constants.SMOKING -> Navigation.findNavController(this, R.id.my_nav_host_profile)
                    .navigate(R.id.nav_change_smoking_fragment)
                Constants.DRINKING -> Navigation.findNavController(this, R.id.my_nav_host_profile)
                    .navigate(R.id.nav_change_drinking_fragment)
                Constants.Zodiac -> Navigation.findNavController(this, R.id.my_nav_host_profile)
                    .navigate(R.id.nav_change_zodiac_fragment)
                Constants.LANGUAGE -> Navigation.findNavController(this, R.id.my_nav_host_profile)
                    .navigate(R.id.nav_change_language_fragment)
            }
        }
    }
}