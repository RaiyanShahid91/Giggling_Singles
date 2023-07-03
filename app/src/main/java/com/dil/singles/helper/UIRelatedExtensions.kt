package com.dil.singles.helper

import android.Manifest
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.location.Address
import android.location.Geocoder
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.net.Uri
import android.os.Build
import android.os.SystemClock
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Base64
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatButton
import androidx.cardview.widget.CardView
import com.airbnb.lottie.LottieAnimationView
import com.dil.singles.R
import com.dil.singles.helper.Constants.TOKEN
import com.dil.singles.helper.Constants.USERS
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.messaging.FirebaseMessaging
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.Period
import java.util.*
import java.util.regex.Pattern
import kotlin.jvm.internal.Intrinsics

fun isValidEmail(email: String): Boolean {
    val emailPattern =
        "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@" + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?" + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\." + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?" + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|" + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$"
    return !TextUtils.isEmpty(email) && Pattern.compile(emailPattern).matcher(email).matches();
}

fun storagePermission(context: Context) {
    Dexter.withContext(context)
        .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
        .withListener(object : PermissionListener {
            override fun onPermissionGranted(permissionGrantedResponse: PermissionGrantedResponse?) {}
            override fun onPermissionDenied(permissionDeniedResponse: PermissionDeniedResponse?) {}

            override fun onPermissionRationaleShouldBeShown(
                permissionRequest: PermissionRequest?,
                permissionToken: PermissionToken
            ) {
                permissionToken.continuePermissionRequest()
            }
        }).check()

    Dexter.withContext(context)
        .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        .withListener(object : PermissionListener {
            override fun onPermissionGranted(permissionGrantedResponse: PermissionGrantedResponse?) {}
            override fun onPermissionDenied(permissionDeniedResponse: PermissionDeniedResponse?) {
            }

            override fun onPermissionRationaleShouldBeShown(
                permissionRequest: PermissionRequest?,
                permissionToken: PermissionToken
            ) {
                permissionToken.continuePermissionRequest()
            }
        }).check()
}

fun audioPermission(
    context: Context,
) {
    Dexter.withContext(context)
        .withPermission(Manifest.permission.RECORD_AUDIO)
        .withListener(object : PermissionListener {
            override fun onPermissionGranted(permissionGrantedResponse: PermissionGrantedResponse?) {}
            override fun onPermissionDenied(permissionDeniedResponse: PermissionDeniedResponse?) {
            }

            override fun onPermissionRationaleShouldBeShown(
                permissionRequest: PermissionRequest?,
                permissionToken: PermissionToken
            ) {
                permissionToken.continuePermissionRequest()
            }
        }).check()

}

fun locationPermission(
    context: Context
) {
    Dexter.withContext(context)
        .withPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
        .withListener(object : PermissionListener {
            override fun onPermissionGranted(permissionGrantedResponse: PermissionGrantedResponse?) {}
            override fun onPermissionDenied(permissionDeniedResponse: PermissionDeniedResponse?) {}
            override fun onPermissionRationaleShouldBeShown(
                permissionRequest: PermissionRequest?,
                permissionToken: PermissionToken
            ) {
                permissionToken.continuePermissionRequest()
            }
        }).check()

    Dexter.withContext(context)
        .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
        .withListener(object : PermissionListener {
            override fun onPermissionGranted(permissionGrantedResponse: PermissionGrantedResponse?) {}
            override fun onPermissionDenied(permissionDeniedResponse: PermissionDeniedResponse?) {}
            override fun onPermissionRationaleShouldBeShown(
                permissionRequest: PermissionRequest?,
                permissionToken: PermissionToken
            ) {
                permissionToken.continuePermissionRequest()
            }
        }).check()
}

fun showCustomToastSuccess(
    context: Context,
    parentLayout: View,
    message: String,
    isWarning: Boolean
): Snackbar {
    val snackbar: Snackbar = Snackbar.make(parentLayout, "Snackbar", Snackbar.LENGTH_LONG)
    val snackView = LayoutInflater.from(context).inflate(R.layout.layout_custom_snakbar, null)
    snackbar.view.setBackgroundColor(Color.TRANSPARENT)
    snackView.findViewById<TextView>(R.id.snackbarMessage).text = message
    if (isWarning) {
        snackView.findViewById<ImageView>(R.id.snakbarIcon)
            .setImageResource(R.drawable.ic_baseline_info_24)
    } else {
        snackView.findViewById<ImageView>(R.id.snakbarIcon)
            .setImageResource(R.drawable.ic_baseline_check_circle_green)

    }
    val snackbarLayout = snackbar.view as Snackbar.SnackbarLayout
    snackbarLayout.setPadding(20, 0, 20, 20)
    snackbarLayout.addView(snackView, 0)
    val params = snackbar.view.layoutParams as FrameLayout.LayoutParams
    params.gravity = Gravity.TOP
    snackbar.view.layoutParams = params
    snackbar.show()
    return snackbar
}

fun convertBase64(imageUri: Uri, context: Context): String {
    var bitmap: Bitmap? = null
    try {
        bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, imageUri)
    } catch (e: IOException) {
        e.printStackTrace()
    }
    val stream = ByteArrayOutputStream()
    bitmap!!.compress(Bitmap.CompressFormat.JPEG, 50, stream as OutputStream)
    val bytes = stream.toByteArray()
    val var10000 = Base64.encodeToString(bytes, 0)
    Intrinsics.checkNotNullExpressionValue(var10000, "encodeToString(bytes, Base64.DEFAULT)")
    return var10000
}

fun showPhotoGuidLine(context: Context) {
    val bottomSheetDialog = BottomSheetDialog(context, R.style.BottomSheetDialogTheme)
    val bottomsheetView: View = LayoutInflater.from(context).inflate(
        R.layout.layout_guidlines,
        bottomSheetDialog.findViewById(R.id.layout_guideline_bottom)
    )

    bottomSheetDialog.setContentView(bottomsheetView)
    bottomSheetDialog.show()
}

fun hideKeyboardWithout(context: Context, editText: EditText) {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
    imm!!.hideSoftInputFromWindow(editText.windowToken, 0)
}

fun convertBase64ToBitmap(base64: String): Bitmap? {
    val imageAsBytes = Base64.decode(base64.toByteArray(), Base64.DEFAULT)
    return BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.size)
}

fun getAddress(latitude: Double, longitude: Double, context: Context): String {
    val result = StringBuilder()
    var mainAddress: Address
    var city: String? = null
    var state: String? = null

    try {
        val geocoder = Geocoder(context, Locale.getDefault())
        val addresses: List<Address> = geocoder.getFromLocation(latitude, longitude, 1)
        if (addresses.isNotEmpty()) {
            mainAddress = addresses[0]
            result.append(mainAddress.locality).append("\n")
            result.append(mainAddress.countryName)
            city = addresses[0].locality
            state = addresses[0].adminArea
        }
    } catch (e: IOException) {
        Log.e("tag", e.message.toString())
    }
    return "${city}, $state"
}

fun getCurrentAddress(latitude: Double, longitude: Double, context: Context): String {
    val result = StringBuilder()
    var mainAddress: Address
    var fullAddress: String? = null

    try {
        val geocoder = Geocoder(context, Locale.getDefault())
        val addresses: List<Address> = geocoder.getFromLocation(latitude, longitude, 1)
        if (addresses.isNotEmpty()) {
            mainAddress = addresses[0]
            result.append(mainAddress.locality).append("\n")
            result.append(mainAddress.countryName)
            fullAddress = addresses[0].getAddressLine(0)
        }
    } catch (e: IOException) {
        Log.e("tag", e.message.toString())
    }
    return fullAddress.toString()
}

fun getCity(latitude: Double, longitude: Double, context: Context): String {
    val result = StringBuilder()
    val mainAddress: Address
    var city: String? = null

    try {
        val geocoder = Geocoder(context, Locale.getDefault())
        val addresses: List<Address> = geocoder.getFromLocation(latitude, longitude, 1)
        if (addresses.isNotEmpty()) {
            mainAddress = addresses[0]
            result.append(mainAddress.locality).append("\n")
            result.append(mainAddress.countryName)
            city = addresses[0].locality
        }
    } catch (e: IOException) {
        Log.e("tag", e.message.toString())
    }
    return city.toString()
}

fun getState(latitude: Double, longitude: Double, context: Context): String {
    val result = StringBuilder()
    val mainAddress: Address
    var state: String? = null

    try {
        val geocoder = Geocoder(context, Locale.getDefault())
        val addresses: List<Address> = geocoder.getFromLocation(latitude, longitude, 1)
        if (addresses.isNotEmpty()) {
            mainAddress = addresses[0]
            result.append(mainAddress.locality).append("\n")
            result.append(mainAddress.countryName)
            state = addresses[0].countryName
        }
    } catch (e: IOException) {
        Log.e("tag", e.message.toString())
    }
    return state.toString()
}

@RequiresApi(Build.VERSION_CODES.O)
fun getAge(year: Int, month: Int, dayOfMonth: Int): Int {
    return Period.between(
        LocalDate.of(year, month, dayOfMonth),
        LocalDate.now()
    ).years
}

fun showKeyboard(context: Context, edittext: EditText) {
    val keyboard = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
    keyboard!!.showSoftInput(edittext, 0)
}

fun getMonthByNumber(month: Int): String {
    val cal = Calendar.getInstance()
    val formatter = SimpleDateFormat("MMMM")
    cal.set(Calendar.MONTH, month)
    return formatter.format(cal.time)
}

fun recordingLayout(context: Context) {
    val bottomSheetDialog =
        BottomSheetDialog(context, R.style.BottomSheetDialogTheme)
    val bottomSheetView: View = LayoutInflater.from(context).inflate(
        R.layout.layout_record,
        bottomSheetDialog.findViewById(R.id.bottomRecordingLayout)
    )
    bottomSheetDialog.setCancelable(false)
    val fileName = FileUtils.genAudioFile().toString()
    var recorder = MediaRecorder()
    var running = false
    var pauseOffset: Long = 0
    var mp = MediaPlayer()

    val startRecord = bottomSheetView.findViewById<CardView>(R.id.startRecCard)
    val stopRecord = bottomSheetView.findViewById<CardView>(R.id.stopRecCard)
    val playRecording = bottomSheetView.findViewById<ImageView>(R.id.playCard)
    val stopRecording = bottomSheetView.findViewById<ImageView>(R.id.stopCard)
    val runtime = bottomSheetView.findViewById<Chronometer>(R.id.recordedTime)
    val close = bottomSheetView.findViewById<AppCompatButton>(R.id.cancelBtn)
    val sendButton = bottomSheetView.findViewById<AppCompatButton>(R.id.send_btn)
    val audioLayout = bottomSheetView.findViewById<LinearLayout>(R.id.audioLayout)
    val fileNameTv = bottomSheetView.findViewById<TextView>(R.id.fileName)
    val waveAnimation = bottomSheetView.findViewById<LottieAnimationView>(R.id.wave_animation)

    fun startRecording() {
        Log.i("Filename: ", fileName)
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC)
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
        recorder.setOutputFile(fileName)
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
        try {
            recorder.prepare()
        } catch (e: IOException) {
            Log.i("Filename: ", e.message.toString())
        }
        recorder.start()
        Log.i("Filename: ", fileName)
    }

    fun startChronometer() {
        if (!running) {
            runtime.base = SystemClock.elapsedRealtime() - pauseOffset
            runtime.start()
            running = true
        }
    }

    fun resetChronometer() {
        runtime.base = SystemClock.elapsedRealtime()
        pauseOffset = 0
    }

    fun stopRecording() {
        recorder.release()
        Log.i("Audio Path", "name $fileName")
    }

    fun playAudio() {
        try {
            mp.setDataSource(fileName)
            mp.prepare()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        mp.start()
    }


    fun stopAudio() {
        mp.stop()
    }

    startRecord.setOnClickListener {
        stopRecord.visibility = View.VISIBLE
        waveAnimation.visibility = View.VISIBLE
        runtime.visibility = View.VISIBLE
        sendButton.visibility = View.GONE
        startRecord.visibility = View.GONE
        audioLayout.visibility = View.GONE
        startRecording()
        startChronometer()
    }

    stopRecord.setOnClickListener {
        audioLayout.visibility = View.VISIBLE
        sendButton.visibility = View.VISIBLE
        waveAnimation.visibility = View.GONE
        runtime.visibility = View.GONE
        startRecord.visibility = View.GONE
        stopRecord.visibility = View.GONE
        stopRecording()
        resetChronometer()
        val audioFileName: String = fileName.substring(fileName.lastIndexOf("/") + 1)
        fileNameTv.text = audioFileName
    }

    sendButton.setOnClickListener {
        bottomSheetDialog.dismiss()
    }

    playRecording.setOnClickListener {
        stopRecording.visibility = View.VISIBLE
        playRecording.visibility = View.GONE
        playAudio()
    }

    stopRecording.setOnClickListener {
        stopRecording.visibility = View.GONE
        playRecording.visibility = View.VISIBLE
        stopAudio()
    }

    close.setOnClickListener {
        if (mp.isPlaying) {
            stopAudio()
        }
        bottomSheetDialog.dismiss()
    }


    bottomSheetDialog.setContentView(bottomSheetView)
    bottomSheetDialog.show()

}

fun loadUserProfile(uid: String, context: Context) {
    val bottomSheetDialog =
        BottomSheetDialog(context, R.style.BottomSheetDialogTheme)
    val bottomSheetView: View = LayoutInflater.from(context).inflate(
        R.layout.layout_profile,
        bottomSheetDialog.findViewById(R.id.bottomProfile)
    )

    val username = bottomSheetView.findViewById<TextView>(R.id.name_age_txt)
    val userSmoking = bottomSheetView.findViewById<TextView>(R.id.smoking_txt)
    val userZodiac = bottomSheetView.findViewById<TextView>(R.id.zodiac_txt)
    val userDrinking = bottomSheetView.findViewById<TextView>(R.id.drinking_txt)
    val userEducation = bottomSheetView.findViewById<TextView>(R.id.education)
    val userReligion = bottomSheetView.findViewById<TextView>(R.id.religion_txt)
    val userHeight = bottomSheetView.findViewById<TextView>(R.id.height_txt)
    val userLanguage = bottomSheetView.findViewById<TextView>(R.id.langauge_txt)
    val lookingFor = bottomSheetView.findViewById<TextView>(R.id.looking_for_txt)
    val userBio = bottomSheetView.findViewById<TextView>(R.id.gender_txt)
    val userInterest = bottomSheetView.findViewById<TextView>(R.id.interest_txt)
    val firstImage = bottomSheetView.findViewById<ImageView>(R.id.firstImage)
    val profileImage = bottomSheetView.findViewById<ImageView>(R.id.profile_image)
    bottomSheetView.findViewById<ImageView>(R.id.back).setOnClickListener {
        bottomSheetDialog.dismiss()
    }

    FirebaseDatabase.getInstance().reference.child(Constants.USERS).child(uid)
        .addValueEventListener(object : ValueEventListener {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onDataChange(snapshot: DataSnapshot) {
                val firstName = snapshot.child(Constants.FIRSTNAME).getValue(String::class.java)
                val lastName = snapshot.child(Constants.LASTNAME).getValue(String::class.java)
                val year = snapshot.child(Constants.YEAR).getValue(String::class.java)
                val month = snapshot.child(Constants.MONTH).getValue(String::class.java)
                val day = snapshot.child(Constants.DAY).getValue(String::class.java)
                val age = getAge(
                    year!!.toInt(),
                    month!!.toInt(),
                    day!!.toInt()
                )
                username.text = "$firstName $lastName  /  $age yr"
                val photoOne =
                    snapshot.child(Constants.PROFILEPHOTO).getValue(String::class.java)
                val photoTwo =
                    snapshot.child(Constants.BACKGROUNDPHOTO).getValue(String::class.java)
                firstImage.setImageBitmap(convertBase64ToBitmap(photoTwo.toString()))
                profileImage.setImageBitmap(convertBase64ToBitmap(photoOne.toString()))
                userSmoking.text = snapshot.child(Constants.SMOKING).getValue(String::class.java)
                userDrinking.text = snapshot.child(Constants.DRINKING).getValue(String::class.java)
                userEducation.text =
                    snapshot.child(Constants.EDUCATION).getValue(String::class.java)
                userReligion.text = snapshot.child(Constants.RELIGION).getValue(String::class.java)
                userZodiac.text = snapshot.child(Constants.Zodiac).getValue(String::class.java)
                userHeight.text =
                    snapshot.child(Constants.HEIGHT).getValue(String::class.java) + " cm"
                userLanguage.text = snapshot.child(Constants.LANGUAGE).getValue(String::class.java)
                lookingFor.text =
                    snapshot.child(Constants.WHATSLOOKINGFOR).getValue(String::class.java)
                var interested = snapshot.child(Constants.PASSION).getValue(String::class.java)
                if (interested?.endsWith(",") == true) {
                    interested = interested.substring(0, interested.length - 1)
                    userInterest.text = interested
                }
                userBio.text = snapshot.child(Constants.BIO).getValue(String::class.java)
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })

    bottomSheetDialog.setContentView(bottomSheetView)
    bottomSheetDialog.show()
}

fun addMessagingToken() {
    FirebaseMessaging.getInstance()
        .token
        .addOnSuccessListener { token ->
            val map = HashMap<String, Any>()
            map[TOKEN] = token
            FirebaseDatabase.getInstance().reference
                .child(USERS)
                .child(FirebaseAuth.getInstance().uid.toString())
                .updateChildren(map)
        }
}

fun tapToUnblock(context: Context) {
    val bottomSheetDialog =
        BottomSheetDialog(context, R.style.BottomSheetDialogTheme)
    val bottomSheetView: View = LayoutInflater.from(context).inflate(
        R.layout.layout_swipe_demo,
        bottomSheetDialog.findViewById(R.id.bottomSwipeDemo)
    )
    val no = bottomSheetView.findViewById<AppCompatButton>(R.id.okBtn)

    no.setOnClickListener {
        bottomSheetDialog.dismiss()
    }

    bottomSheetDialog.setContentView(bottomSheetView)
    bottomSheetDialog.show()
}