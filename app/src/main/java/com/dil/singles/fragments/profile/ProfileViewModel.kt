package com.dil.singles.fragments.profile

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatButton
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dil.singles.R
import com.dil.singles.helper.*
import com.dil.singles.helper.Constants.BIO
import com.dil.singles.helper.Constants.DATEOFBIRTH
import com.dil.singles.helper.Constants.DAY
import com.dil.singles.helper.Constants.DRINKING
import com.dil.singles.helper.Constants.EDUCATION
import com.dil.singles.helper.Constants.FIRSTNAME
import com.dil.singles.helper.Constants.PROFILEPHOTO
import com.dil.singles.helper.Constants.HEIGHT
import com.dil.singles.helper.Constants.LANGUAGE
import com.dil.singles.helper.Constants.LASTNAME
import com.dil.singles.helper.Constants.MONTH
import com.dil.singles.helper.Constants.PASSION
import com.dil.singles.helper.Constants.RELIGION
import com.dil.singles.helper.Constants.BACKGROUNDPHOTO
import com.dil.singles.helper.Constants.SMOKING
import com.dil.singles.helper.Constants.USERS
import com.dil.singles.helper.Constants.WHATSLOOKINGFOR
import com.dil.singles.helper.Constants.YEAR
import com.dil.singles.helper.Constants.Zodiac
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ProfileViewModel : ViewModel() {

    val firstName = MutableLiveData("")
    val lastName = MutableLiveData("")
    val name = MutableLiveData("")
    var bio = MutableLiveData("")
    var dateOfBirth = MutableLiveData("")
    var dob = MutableLiveData("")
    val interest = MutableLiveData("")
    val smoking = MutableLiveData("")
    val drinking = MutableLiveData("")
    val education = MutableLiveData("")
    val zodiac = MutableLiveData("")
    val height = MutableLiveData("")
    val photoOne = MutableLiveData("")
    val photoTwo = MutableLiveData("")
    val lanaguge = MutableLiveData("")
    private var firebaseAuth = FirebaseAuth.getInstance()
    private var firebaseDatabase = FirebaseDatabase.getInstance()
    val showLoader = ObservableBoolean(false)
    val onBackOccured = SingleEvent()
    val onMessageOccured = SingleEvent()
    val userFirstName = MutableLiveData("")
    val userLastName = MutableLiveData("")
    val userNameAge = MutableLiveData("")
    val userYear = MutableLiveData("")
    val userMonth = MutableLiveData("")
    val userDay = MutableLiveData("")
    var userAge: Int? = null
    val userSmoking = MutableLiveData("")
    val userDrinking = MutableLiveData("")
    val userEducation = MutableLiveData("")
    val userReligion = MutableLiveData("")
    val userZodiac = MutableLiveData("")
    val userHeight = MutableLiveData("")
    val lookingFor = MutableLiveData("")
    val userLanguage = MutableLiveData("")
    val userInterest = MutableLiveData("")
    val userBio = MutableLiveData("")
    val onCloseOccured = SingleEvent()
    val profileImageObserved = SingleEvent()
    val coverImageObserver = SingleEvent()

    fun getProfileInfo() {
        showLoader.set(true)
        firebaseDatabase.reference.child(USERS).child(firebaseAuth.uid.toString())
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    firstName.value = snapshot.child(FIRSTNAME).getValue(String::class.java)
                    lastName.value = snapshot.child(LASTNAME).getValue(String::class.java)
                    bio.value = snapshot.child(BIO).getValue(String::class.java)
                    dateOfBirth.value = snapshot.child(DATEOFBIRTH).getValue(String::class.java)
                    var month : String = snapshot.child(MONTH).getValue(String::class.java).toString()
                    var date = snapshot.child(DAY).getValue(String::class.java)
                    var year = snapshot.child(YEAR).getValue(String::class.java)
                    name.value = firstName.value + " " + lastName.value
                    dob.value = "Date of birth: $date ${getMonthByNumber(month.toInt())} $year"
                    education.value = snapshot.child(EDUCATION).getValue(String::class.java)
                    smoking.value = snapshot.child(SMOKING).getValue(String::class.java)
                    drinking.value = snapshot.child(DRINKING).getValue(String::class.java)
                    zodiac.value = snapshot.child(Zodiac).getValue(String::class.java)
                    height.value = snapshot.child(HEIGHT).getValue(String::class.java) + " cm"
                    var interested = snapshot.child(PASSION).getValue(String::class.java)
                    if (interested?.endsWith(",") == true) {
                        interested = interested.substring(0, interested.length - 1)
                        interest.value = interested
                    }
                    photoOne.value = snapshot.child(PROFILEPHOTO).getValue(String::class.java)
                    photoTwo.value = snapshot.child(BACKGROUNDPHOTO).getValue(String::class.java)
                    lanaguge.value = snapshot.child(LANGUAGE).getValue(String::class.java)
                    showLoader.set(false)
                }

                override fun onCancelled(error: DatabaseError) {
                    showLoader.set(false)
                }
            })
    }

    fun getUserProfileInfo(uid: String) {
        firebaseDatabase.reference.child(USERS).child(uid)
            .addValueEventListener(object : ValueEventListener {
                @RequiresApi(Build.VERSION_CODES.O)
                override fun onDataChange(snapshot: DataSnapshot) {
                    userFirstName.value = snapshot.child(FIRSTNAME).getValue(String::class.java)
                    userLastName.value = snapshot.child(LASTNAME).getValue(String::class.java)
                    userYear.value = snapshot.child(YEAR).getValue(String::class.java)
                    userMonth.value = snapshot.child(MONTH).getValue(String::class.java)
                    userDay.value = snapshot.child(DAY).getValue(String::class.java)
                    userAge = getAge(
                        userYear.value!!.toInt(),
                        userMonth.value!!.toInt(),
                        userDay.value!!.toInt()
                    )
                    userNameAge.value =
                        userFirstName.value + " " + userLastName.value + " / " + userAge + "yr"
                    userSmoking.value = snapshot.child(SMOKING).getValue(String::class.java)
                    userDrinking.value = snapshot.child(DRINKING).getValue(String::class.java)
                    userEducation.value = snapshot.child(EDUCATION).getValue(String::class.java)
                    userReligion.value = snapshot.child(RELIGION).getValue(String::class.java)
                    userZodiac.value = snapshot.child(Zodiac).getValue(String::class.java)
                    userHeight.value = snapshot.child(HEIGHT).getValue(String::class.java) + " cm"
                    userLanguage.value = snapshot.child(LANGUAGE).getValue(String::class.java)
                    lookingFor.value = snapshot.child(WHATSLOOKINGFOR).getValue(String::class.java)
                    var interested = snapshot.child(PASSION).getValue(String::class.java)
                    if (interested?.endsWith(",") == true) {
                        interested = interested.substring(0, interested.length - 1)
                        userInterest.value = interested
                    }
                    userBio.value = snapshot.child(BIO).getValue(String::class.java)
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
    }

    fun onBack() {
        onBackOccured.actionOccured()
    }

    fun onMessage() {
        onMessageOccured.actionOccured()
    }

    fun onClose() {
        onCloseOccured.actionOccured()
    }

    fun editImage(context: Context) {
        val bottomSheetDialog = BottomSheetDialog(context, R.style.BottomSheetDialogTheme)
        val bottomSheetView: View = LayoutInflater.from(context).inflate(
            R.layout.layout_add_image,
            bottomSheetDialog.findViewById(R.id.bottomSheetAddImage)
        )

        bottomSheetView.findViewById<TextView>(R.id.changeProfileImage)?.setOnClickListener {
            profileImageObserved.actionOccured()
            bottomSheetDialog.dismiss()
        }
        bottomSheetView.findViewById<TextView>(R.id.changeCoverImage)?.setOnClickListener {
            coverImageObserver.actionOccured()
            bottomSheetDialog.dismiss()
        }

        bottomSheetView.findViewById<AppCompatButton>(R.id.close_label).setOnClickListener {
            bottomSheetDialog.dismiss()
        }
        bottomSheetDialog.setContentView(bottomSheetView)
        bottomSheetDialog.show()
    }
}