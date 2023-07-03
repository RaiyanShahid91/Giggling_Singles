package com.dil.singles.helper

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationManager
import android.os.Looper
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.dil.singles.activity.DashboardActivity
import com.dil.singles.activity.MainActivity
import com.dil.singles.helper.Constants.AGREE
import com.dil.singles.helper.Constants.BACKGROUNDPHOTO
import com.dil.singles.helper.Constants.BIO
import com.dil.singles.helper.Constants.COUNTRY
import com.dil.singles.helper.Constants.DATEOFBIRTH
import com.dil.singles.helper.Constants.DRINKING
import com.dil.singles.helper.Constants.EDUCATION
import com.dil.singles.helper.Constants.FIRSTNAME
import com.dil.singles.helper.Constants.GENDER
import com.dil.singles.helper.Constants.HEIGHT
import com.dil.singles.helper.Constants.LANGUAGE
import com.dil.singles.helper.Constants.LASTNAME
import com.dil.singles.helper.Constants.PASSION
import com.dil.singles.helper.Constants.PROFILEPHOTO
import com.dil.singles.helper.Constants.RELIGION
import com.dil.singles.helper.Constants.SMOKING
import com.dil.singles.helper.Constants.STATE
import com.dil.singles.helper.Constants.WHATSLOOKINGFOR
import com.dil.singles.helper.Constants.WHOMTODATE
import com.dil.singles.helper.Constants.Zodiac
import com.google.android.gms.location.*
import com.dil.singles.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

abstract class BaseFragment : Fragment() {

    val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    val firebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()

    var isPassionSelected1: Boolean = true
    var isPassionSelected2: Boolean = true
    var isPassionSelected3: Boolean = true
    var isPassionSelected4: Boolean = true
    var isPassionSelected5: Boolean = true
    var isPassionSelected6: Boolean = true
    var isPassionSelected7: Boolean = true
    var isPassionSelected8: Boolean = true
    var isPassionSelected9: Boolean = true
    var isPassionSelected10: Boolean = true
    var isPassionSelected11: Boolean = true
    var isPassionSelected12: Boolean = true
    var isPassionSelected13: Boolean = true
    var isPassionSelected14: Boolean = true
    var isPassionSelected15: Boolean = true
    var isPassionSelected16: Boolean = true
    var isPassionSelected17: Boolean = true
    private var isReadPermission = false
    private var isWritePermission = false
    private var isCourseLocationPermission = false
    private var isFineLocationPermission = false
    private var isRecordAudioPermission = false

    var passionateString1 = ""
    var passionateString2 = ""
    var passionateString3 = ""
    var passionateString4 = ""
    var passionateString5 = ""
    var passionateString6 = ""
    var passionateString7 = ""
    var passionateString8 = ""
    var passionateString9 = ""
    var passionateString10 = ""
    var passionateString11 = ""
    var passionateString12 = ""
    var passionateString13 = ""
    var passionateString14 = ""
    var passionateString15 = ""
    var passionateString16 = ""
    var passionateString17 = ""
    var mFusedLocationClient: FusedLocationProviderClient? = null

    fun onBoarding(context: Context) {
        firebaseDatabase.reference.child(Constants.USERS).child(firebaseAuth.uid.toString())
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (data in dataSnapshot.children) {
                        when {
                            !dataSnapshot.child(AGREE).exists() -> {
                                findNavController().navigate(R.id.nav_Welcome_Agree_fragment)
                            }
                            !dataSnapshot.child(FIRSTNAME).exists() -> {
                                findNavController().navigate(R.id.nav_add_name_fragment)
                            }
                            !dataSnapshot.child(LASTNAME).exists() -> {
                                findNavController().navigate(R.id.nav_add_name_fragment)
                            }
                            !dataSnapshot.child(PROFILEPHOTO).exists() -> {
                                findNavController().navigate(R.id.nav_add_photo_fragment)
                            }
                            !dataSnapshot.child(BACKGROUNDPHOTO).exists() -> {
                                findNavController().navigate(R.id.nav_add_photo_fragment)
                            }
                            !dataSnapshot.child(DATEOFBIRTH).exists() -> {
                                findNavController().navigate(R.id.nav_date_of_birth_fragment)
                            }
                            !dataSnapshot.child(GENDER).exists() -> {
                                findNavController().navigate(R.id.nav_gender_fragment)
                            }
                            !dataSnapshot.child(WHOMTODATE).exists() -> {
                                findNavController().navigate(R.id.nav_whome_date_fragment)
                            }
                            !dataSnapshot.child(WHATSLOOKINGFOR).exists() -> {
                                findNavController().navigate(R.id.nav_whats_looking_fragment)
                            }
                            !dataSnapshot.child(DRINKING).exists() -> {
                                findNavController().navigate(R.id.nav_drink_fragment)
                            }
                            !dataSnapshot.child(SMOKING).exists() -> {
                                findNavController().navigate(R.id.nav_smoke_fragment)
                            }
                            !dataSnapshot.child(RELIGION).exists() -> {
                                findNavController().navigate(R.id.nav_religion_fragment)
                            }
                            !dataSnapshot.child(HEIGHT).exists() -> {
                                findNavController().navigate(R.id.nav_height_fragment)
                            }
                            !dataSnapshot.child(Zodiac).exists() -> {
                                findNavController().navigate(R.id.nav_zodiac_fragment)
                            }
                            !dataSnapshot.child(EDUCATION).exists() -> {
                                findNavController().navigate(R.id.nav_education_fragment)
                            }
                            !dataSnapshot.child(PASSION).exists() -> {
                                findNavController().navigate(R.id.nav_your_interested_fragment)
                            }
                            !dataSnapshot.child(LANGUAGE).exists() -> {
                                findNavController().navigate(R.id.nav_language_fragment)
                            }
                            !dataSnapshot.child(BIO).exists() -> {
                                findNavController().navigate(R.id.nav_bio_fragment)
                            }
                            else -> {
                                val intent = Intent(context, MainActivity::class.java)
                                context.startActivity(intent)
                            }
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })

    }

    @SuppressLint("MissingPermission")
    fun getLastLocation(context: Context) {
        locationPermission(context)
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        if (isLocationEnabled(context)) {
            mFusedLocationClient!!.lastLocation.addOnCompleteListener(OnCompleteListener<Location?> { task ->
                val location = task.result
                if (location == null) {
                    requestNewLocationData(context)
                } else {
                    val hashMap = HashMap<String, Any>()
                    hashMap[Constants.LATITUDE] = location.latitude
                    hashMap[Constants.LONGITUDE] = location.longitude
                    firebaseDatabase.reference.child(Constants.USERS)
                        .child(firebaseAuth.uid.toString()).updateChildren(hashMap)
                        .addOnSuccessListener {
                            val address = HashMap<String, Any>()
                            address[STATE] =
                                getCity(location.latitude, location.longitude, context)
                            address[COUNTRY] =
                                getState(location.latitude, location.longitude, context)
                            firebaseDatabase.reference.child(Constants.USERS)
                                .child(firebaseAuth.uid.toString()).updateChildren(address)
                                .addOnSuccessListener {

                                }
                        }
                        .addOnFailureListener {}
                }
            })
        } else {
            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivity(intent)
        }
    }

    @SuppressLint("MissingPermission")
    fun requestNewLocationData(context: Context) {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        val mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 5
        mLocationRequest.fastestInterval = 0
        mLocationRequest.numUpdates = 1
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        mFusedLocationClient!!.requestLocationUpdates(
            mLocationRequest,
            mLocationCallback,
            Looper.myLooper()
        )
    }

    private val mLocationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val mLastLocation: Location? = locationResult.lastLocation
            val hashMap = HashMap<String, Any>()
            if (mLastLocation != null) {
                hashMap[Constants.LATITUDE] = mLastLocation.latitude.toString().trim()
                hashMap[Constants.LONGITUDE] = mLastLocation.longitude.toString().trim()
                firebaseDatabase.reference.child(Constants.USERS)
                    .child(firebaseAuth.uid.toString()).updateChildren(hashMap)
                    .addOnSuccessListener {}
                    .addOnFailureListener {}
            }
        }
    }

    private fun isLocationEnabled(context: Context): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager?
        return locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    fun changeFirstPhoto(photoIndex: String, base64: String?) {
        val hashMap = HashMap<String, Any>()
        hashMap[photoIndex] = base64.toString()
        firebaseDatabase.reference.child(Constants.USERS).child(firebaseAuth.uid.toString())
            .updateChildren(hashMap)
    }

    fun changeSecondPhoto(photoIndex: String, base64: String?) {
        val hashMap = HashMap<String, Any>()
        hashMap[photoIndex] = base64.toString()
        firebaseDatabase.reference.child(Constants.USERS).child(firebaseAuth.uid.toString())
            .updateChildren(hashMap)
    }

    fun logout(context: Context) {
        val bottomSheetDialog = BottomSheetDialog(context, R.style.BottomSheetDialogTheme)
        val bottomSheetView: View = LayoutInflater.from(context).inflate(
            R.layout.layout_logout,
            bottomSheetDialog.findViewById(R.id.bottomLogout)
        )

        bottomSheetView.findViewById<TextView>(R.id.logoutBtn)?.setOnClickListener {
            SharedPrefData(context).sharedPreferences.edit().remove(Constants.EMAIL).apply()
            SharedPrefData(context).sharedPreferences.edit().remove(Constants.PASSWORD).apply()
            firebaseAuth.signOut()
            val intent = Intent(context, DashboardActivity::class.java)
            intent.putExtra(Constants.LOGOUT, Constants.LOGOUT)
            context.startActivity(intent)
            activity?.finishAffinity()
            bottomSheetDialog.dismiss()
        }

        bottomSheetView.findViewById<AppCompatButton>(R.id.cancelBtn).setOnClickListener {
            bottomSheetDialog.dismiss()
        }
        bottomSheetDialog.setContentView(bottomSheetView)
        bottomSheetDialog.show()
    }

//    fun requestPermission()
//    {
//        var permissionLauncher: ActivityResultLauncher<Array<String>> =
//            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){ permissions ->
//            isReadPermission = permissions[Manifest.permission.READ_EXTERNAL_STORAGE] ?: isReadPermission
//            isWritePermission = permissions[Manifest.permission.WRITE_EXTERNAL_STORAGE] ?: isWritePermission
//            isCourseLocationPermission = permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: isCourseLocationPermission
//            isFineLocationPermission = permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: isFineLocationPermission
//            isRecordAudioPermission = permissions[Manifest.permission.RECORD_AUDIO] ?: isRecordAudioPermission
//        }
//
//        allowMultiplePermission(permissionLauncher)
//    }
//
//    private fun allowMultiplePermission(permissionLauncher: ActivityResultLauncher<Array<String>>) {
//
//        isReadPermission = ContextCompat.checkSelfPermission(
//            requireContext(),
//            Manifest.permission.READ_EXTERNAL_STORAGE
//        ) == PackageManager.PERMISSION_GRANTED
//
//        isWritePermission = ContextCompat.checkSelfPermission(
//            requireContext(),
//            Manifest.permission.WRITE_EXTERNAL_STORAGE
//        ) == PackageManager.PERMISSION_GRANTED
//
//        isCourseLocationPermission = ContextCompat.checkSelfPermission(
//            requireContext(),
//            Manifest.permission.ACCESS_COARSE_LOCATION
//        ) == PackageManager.PERMISSION_GRANTED
//
//        isFineLocationPermission = ContextCompat.checkSelfPermission(
//            requireContext(),
//            Manifest.permission.ACCESS_FINE_LOCATION
//        ) == PackageManager.PERMISSION_GRANTED
//
//        isRecordAudioPermission = ContextCompat.checkSelfPermission(
//            requireContext(),
//            Manifest.permission.RECORD_AUDIO
//        ) == PackageManager.PERMISSION_GRANTED
//
//        val permissionRequest : MutableList<String> = ArrayList()
//
//        if (!isReadPermission)
//        {
//            permissionRequest.add(Manifest.permission.READ_EXTERNAL_STORAGE)
//        }
//
//        if (!isWritePermission)
//        {
//            permissionRequest.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
//        }
//
//        if (!isFineLocationPermission)
//        {
//            permissionRequest.add(Manifest.permission.ACCESS_FINE_LOCATION)
//        }
//
//        if (!isCourseLocationPermission)
//        {
//            permissionRequest.add(Manifest.permission.ACCESS_COARSE_LOCATION)
//        }
//
//        if (!isRecordAudioPermission)
//        {
//            permissionRequest.add(Manifest.permission.RECORD_AUDIO)
//        }
//
//        if (permissionRequest.isNotEmpty())
//        {
//            permissionLauncher.launch(permissionRequest.toTypedArray())
//        }
//    }
}