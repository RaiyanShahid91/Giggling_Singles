package com.dil.singles.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.dil.singles.BuildConfig
import com.dil.singles.R
import com.dil.singles.databinding.ActivityMainBinding
import com.dil.singles.helper.Constants
import com.dil.singles.helper.Constants.TOKEN
import com.dil.singles.helper.Constants.USERS
import com.dil.singles.helper.Constants.VERSION
import com.dil.singles.helper.SharedPrefData
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessaging
import java.util.ArrayList

class MainActivity : AppCompatActivity() {

    lateinit var fragmentMainBinding: ActivityMainBinding
    private var isReadPermission = false
    private var isWritePermission = false
    private var isCourseLocationPermission = false
    private var isFineLocationPermission = false
    private var isRecordAudioPermission = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(fragmentMainBinding.root)
        val navController = this.findNavController(R.id.my_nav_host_main)
        val navView: BottomNavigationView = findViewById(R.id.bottom_nav_view)
        navView.setupWithNavController(navController)
        requestPermission()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        for (fragment in supportFragmentManager.fragments) {
            fragment.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onResume() {
        super.onResume()
        FirebaseDatabase.getInstance().reference.child("presence")
            .child(FirebaseAuth.getInstance().uid.toString()).setValue("Online")
    }

    override fun onPause() {
        super.onPause()
        FirebaseDatabase.getInstance().reference.child("presence")
            .child(FirebaseAuth.getInstance().uid.toString()).setValue("Offline")
    }

    override fun onDestroy() {
        super.onDestroy()
        FirebaseDatabase.getInstance().reference.child("presence")
            .child(FirebaseAuth.getInstance().uid.toString()).setValue("Offline")
    }

    private fun requestPermission() {
        var permissionLauncher: ActivityResultLauncher<Array<String>> =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
                isReadPermission =
                    permissions[Manifest.permission.READ_EXTERNAL_STORAGE] ?: isReadPermission
                isWritePermission =
                    permissions[Manifest.permission.WRITE_EXTERNAL_STORAGE] ?: isWritePermission
                isCourseLocationPermission = permissions[Manifest.permission.ACCESS_COARSE_LOCATION]
                    ?: isCourseLocationPermission
                isFineLocationPermission = permissions[Manifest.permission.ACCESS_FINE_LOCATION]
                    ?: isFineLocationPermission
                isRecordAudioPermission =
                    permissions[Manifest.permission.RECORD_AUDIO] ?: isRecordAudioPermission
            }

        allowMultiplePermission(permissionLauncher)
    }

    private fun allowMultiplePermission(permissionLauncher: ActivityResultLauncher<Array<String>>) {

        isReadPermission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED

        isWritePermission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED

        isCourseLocationPermission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        isFineLocationPermission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        isRecordAudioPermission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED

        val permissionRequest: MutableList<String> = ArrayList()

        if (!isReadPermission) {
            permissionRequest.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        }

        if (!isWritePermission) {
            permissionRequest.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }

        if (!isFineLocationPermission) {
            permissionRequest.add(Manifest.permission.ACCESS_FINE_LOCATION)
        }

        if (!isCourseLocationPermission) {
            permissionRequest.add(Manifest.permission.ACCESS_COARSE_LOCATION)
        }

        if (!isRecordAudioPermission) {
            permissionRequest.add(Manifest.permission.RECORD_AUDIO)
        }

        if (permissionRequest.isNotEmpty()) {
            permissionLauncher.launch(permissionRequest.toTypedArray())
        }
    }
}