package com.dil.singles.activity

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.dil.singles.databinding.ActivitySettingsBinding
import java.util.ArrayList

class SettingsActivity : AppCompatActivity() {

    lateinit var binding: ActivitySettingsBinding
    private var isReadPermission = false
    private var isWritePermission = false
    private var isCourseLocationPermission = false
    private var isFineLocationPermission = false
    private var isRecordAudioPermission = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        requestPermission()
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