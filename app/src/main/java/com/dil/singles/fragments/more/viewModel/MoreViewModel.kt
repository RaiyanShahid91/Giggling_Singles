package com.dil.singles.fragments.more.viewModel

import android.text.TextUtils
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dil.singles.helper.Constants.AGREE
import com.dil.singles.helper.Constants.BIO
import com.dil.singles.helper.Constants.DATEOFBIRTH
import com.dil.singles.helper.Constants.DAY
import com.dil.singles.helper.Constants.DRINKING
import com.dil.singles.helper.Constants.EDUCATION
import com.dil.singles.helper.Constants.FIRSTNAME
import com.dil.singles.helper.Constants.GENDER
import com.dil.singles.helper.Constants.HEIGHT
import com.dil.singles.helper.Constants.LANGUAGE
import com.dil.singles.helper.Constants.LASTNAME
import com.dil.singles.helper.Constants.MONTH
import com.dil.singles.helper.Constants.PASSION
import com.dil.singles.helper.Constants.RELIGION
import com.dil.singles.helper.Constants.SMOKING
import com.dil.singles.helper.Constants.USERS
import com.dil.singles.helper.Constants.WHATSLOOKINGFOR
import com.dil.singles.helper.Constants.WHOMTODATE
import com.dil.singles.helper.Constants.YEAR
import com.dil.singles.helper.Constants.Zodiac
import com.dil.singles.helper.SingleEvent
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.util.regex.Pattern


class MoreViewModel : ViewModel() {
    val firstNameMutableData = MutableLiveData("")
    val lastNameMutableData = MutableLiveData("")
    val firstPhotoMutable = MutableLiveData("")
    val secondPhotoMutable = MutableLiveData("")
    val thirdPhotoMutable = MutableLiveData("")
    val fourthPhotoMutable = MutableLiveData("")
    val day = MutableLiveData("")
    val month = MutableLiveData("")
    val year = MutableLiveData("")
    var gender = MutableLiveData("")
    var whomeToDate = MutableLiveData("")
    var whatsLookingFor = MutableLiveData("")
    var doYouDrink = MutableLiveData("")
    var doYouSmoke = MutableLiveData("")
    var religion = MutableLiveData("")
    var height = MutableLiveData("")
    var zodiac = MutableLiveData("")
    var stringPassion = MutableLiveData("")
    var errorString = MutableLiveData("")
    var education = MutableLiveData("")
    var email = MutableLiveData("")
    var bio = MutableLiveData("")
    var language = MutableLiveData("")
    var addGender = SingleEvent()
    val onSuccessOccurred = SingleEvent()
    val onFailedOccurred = SingleEvent()
    val onSkip = SingleEvent()
    private var firebaseAuth = FirebaseAuth.getInstance()
    private var firebaseDatabase = FirebaseDatabase.getInstance()
    val showLoader = ObservableBoolean(false)
    val firstPhoto = ObservableBoolean(false)
    val secondPhoto = ObservableBoolean(false)
    val thirdPhoto = ObservableBoolean(false)
    val fourthPhoto = ObservableBoolean(false)
    val emailErrorEvent = ObservableBoolean(false)

    fun addName() {
        showLoader.set(true)
        val hashMap = HashMap<String, Any>()
        hashMap[FIRSTNAME] = firstNameMutableData.value.toString()
        hashMap[LASTNAME] = lastNameMutableData.value.toString()
        firebaseDatabase.reference.child(USERS)
            .child(firebaseAuth.uid.toString()).updateChildren(hashMap)
            .addOnSuccessListener {
                onSuccessOccurred.actionOccured()
            }
            .addOnFailureListener {
                onFailedOccurred.actionOccured()
                showLoader.set(false)
            }
    }

    fun photoUploaded() {
        onSuccessOccurred.actionOccured()
    }

    fun addDateOfBirth() {
        showLoader.set(true)
        val hashMap = HashMap<String, Any>()
        hashMap[DAY] = day.value.toString()
        hashMap[MONTH] = month.value.toString()
        hashMap[YEAR] = year.value.toString()
        hashMap[DATEOFBIRTH] =
            "${day.value.toString()}/${month.value.toString()}/${year.value.toString()}"
        firebaseDatabase.reference.child(USERS)
            .child(firebaseAuth.uid.toString()).updateChildren(hashMap)
            .addOnSuccessListener {
                onSuccessOccurred.actionOccured()
            }
            .addOnFailureListener {
                onFailedOccurred.actionOccured()
                showLoader.set(false)
            }
    }

    fun addGender() {
        showLoader.set(true)
        val hashMap = HashMap<String, Any>()
        hashMap[GENDER] = gender.value.toString()
        firebaseDatabase.reference.child(USERS)
            .child(firebaseAuth.uid.toString()).updateChildren(hashMap)
            .addOnSuccessListener {
                onSuccessOccurred.actionOccured()
            }
            .addOnFailureListener {
                onFailedOccurred.actionOccured()
                showLoader.set(false)
            }
    }

    fun addWhomToDate() {
        showLoader.set(true)
        val hashMap = HashMap<String, Any>()
        hashMap[WHOMTODATE] = whomeToDate.value.toString()
        firebaseDatabase.reference.child(USERS)
            .child(firebaseAuth.uid.toString()).updateChildren(hashMap)
            .addOnSuccessListener {
                onSuccessOccurred.actionOccured()
            }
            .addOnFailureListener {
                onFailedOccurred.actionOccured()
                showLoader.set(false)
            }
    }

    fun addWhatsLookingFor() {
        showLoader.set(true)
        val hashMap = HashMap<String, Any>()
        hashMap[WHATSLOOKINGFOR] = whatsLookingFor.value.toString()
        firebaseDatabase.reference.child(USERS)
            .child(firebaseAuth.uid.toString()).updateChildren(hashMap)
            .addOnSuccessListener {
                onSuccessOccurred.actionOccured()
            }
            .addOnFailureListener {
                onFailedOccurred.actionOccured()
                showLoader.set(false)
            }
    }

    fun addDoYouDrink() {
        showLoader.set(true)
        val hashMap = HashMap<String, Any>()
        hashMap[DRINKING] = doYouDrink.value.toString()
        firebaseDatabase.reference.child(USERS)
            .child(firebaseAuth.uid.toString()).updateChildren(hashMap)
            .addOnSuccessListener {
                onSuccessOccurred.actionOccured()
            }
            .addOnFailureListener {
                onFailedOccurred.actionOccured()
                showLoader.set(false)
            }
    }

    fun addDoYouSmoke() {
        showLoader.set(true)
        val hashMap = HashMap<String, Any>()
        hashMap[SMOKING] = doYouSmoke.value.toString()
        firebaseDatabase.reference.child(USERS)
            .child(firebaseAuth.uid.toString()).updateChildren(hashMap)
            .addOnSuccessListener {
                onSuccessOccurred.actionOccured()
            }
            .addOnFailureListener {
                onFailedOccurred.actionOccured()
                showLoader.set(false)
            }
    }

    fun addReligion() {
        showLoader.set(true)
        val hashMap = HashMap<String, Any>()
        hashMap[RELIGION] = religion.value.toString()
        firebaseDatabase.reference.child(USERS)
            .child(firebaseAuth.uid.toString()).updateChildren(hashMap)
            .addOnSuccessListener {
                onSuccessOccurred.actionOccured()
            }
            .addOnFailureListener {
                onFailedOccurred.actionOccured()
                showLoader.set(false)
            }
    }

    fun addHeight() {
        showLoader.set(true)
        val hashMap = HashMap<String, Any>()
        hashMap[HEIGHT] = height.value.toString()
        firebaseDatabase.reference.child(USERS)
            .child(firebaseAuth.uid.toString()).updateChildren(hashMap)
            .addOnSuccessListener {
                onSuccessOccurred.actionOccured()
            }
            .addOnFailureListener {
                onFailedOccurred.actionOccured()
                showLoader.set(false)
            }
    }

    fun addZodiac() {
        showLoader.set(true)
        val hashMap = HashMap<String, Any>()
        hashMap[Zodiac] = zodiac.value.toString()
        firebaseDatabase.reference.child(USERS)
            .child(firebaseAuth.uid.toString()).updateChildren(hashMap)
            .addOnSuccessListener {
                onSuccessOccurred.actionOccured()
            }
            .addOnFailureListener {
                onFailedOccurred.actionOccured()
                showLoader.set(false)
            }
    }

    fun addPassion() {
        showLoader.set(true)
        val hashMap = HashMap<String, Any>()
        hashMap[PASSION] = stringPassion.value.toString()
        firebaseDatabase.reference.child(USERS)
            .child(firebaseAuth.uid.toString()).updateChildren(hashMap)
            .addOnSuccessListener {
                onSuccessOccurred.actionOccured()
            }
            .addOnFailureListener {
                onFailedOccurred.actionOccured()
                showLoader.set(false)
            }
    }

    fun addEducation() {
        showLoader.set(true)
        val hashMap = HashMap<String, Any>()
        hashMap[EDUCATION] = education.value.toString()
        firebaseDatabase.reference.child(USERS)
            .child(firebaseAuth.uid.toString()).updateChildren(hashMap)
            .addOnSuccessListener {
                onSuccessOccurred.actionOccured()
            }
            .addOnFailureListener {
                onFailedOccurred.actionOccured()
                showLoader.set(false)
            }
    }

    fun addBio() {
        showLoader.set(true)
        val hashMap = HashMap<String, Any>()
        hashMap[BIO] = bio.value.toString().trim()
        firebaseDatabase.reference.child(USERS)
            .child(firebaseAuth.uid.toString()).updateChildren(hashMap)
            .addOnSuccessListener {
                onSuccessOccurred.actionOccured()
            }
            .addOnFailureListener {
                onFailedOccurred.actionOccured()
                showLoader.set(false)
            }
    }

    fun addLanguage() {
        showLoader.set(true)
        val hashMap = HashMap<String, Any>()
        hashMap[LANGUAGE] = language.value.toString().trim()
        firebaseDatabase.reference.child(USERS)
            .child(firebaseAuth.uid.toString()).updateChildren(hashMap)
            .addOnSuccessListener {
                onSuccessOccurred.actionOccured()
            }
            .addOnFailureListener {
                onFailedOccurred.actionOccured()
                showLoader.set(false)
            }
    }

    fun IAgree() {
        showLoader.set(true)
        val hashMap = HashMap<String, Any>()
        hashMap[AGREE] = "I Agree to follow the rules."
        firebaseDatabase.reference.child(USERS)
            .child(firebaseAuth.uid.toString()).updateChildren(hashMap)
            .addOnSuccessListener {
                onSuccessOccurred.actionOccured()
            }
            .addOnFailureListener {
                onFailedOccurred.actionOccured()
                showLoader.set(false)
            }
    }

    fun skipBio() {
        onSkip.actionOccured()
    }

    fun changePassword() {
        showLoader.set(true)
        firebaseAuth.fetchSignInMethodsForEmail(email.value.toString())
            .addOnCompleteListener { task ->
                val isNewUser = task.result.signInMethods?.isEmpty()
                if (isNewUser == true) {
                    showLoader.set(false)
                    emailErrorEvent.set(true)
                    errorString.value = "Couldn't find your Amored account. Please create a account"
                } else {
                    emailErrorEvent.set(false)
                    sendChangePasswordLink(email.value.toString())
                }
            }
    }

    private fun sendChangePasswordLink(email: String) {
        firebaseAuth.sendPasswordResetEmail(email)
            .addOnCompleteListener(OnCompleteListener<Void?> { task ->
                if (task.isSuccessful) {
                    showLoader.set(false)
                    onSuccessOccurred.actionOccured()
                } else {
                    showLoader.set(false)
                    onFailedOccurred.actionOccured()
                }
            })
    }

    fun isValidEmail(email: String): Boolean {
        val emailPattern =
            "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@" + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?" + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\." + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?" + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|" + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$"
        return !TextUtils.isEmpty(email) && Pattern.compile(emailPattern).matcher(email).matches();
    }

}