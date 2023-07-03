package com.dil.singles.fragments.edit

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.MutableLiveData
import com.dil.singles.R
import com.dil.singles.helper.BaseViewModel
import com.dil.singles.helper.Constants
import com.dil.singles.helper.SingleEvent
import com.google.android.material.bottomsheet.BottomSheetDialog

class EditViewModel : BaseViewModel() {
    val profileImageObserved = SingleEvent()
    val coverImageObserver = SingleEvent()
    val onBackOccured = SingleEvent()
    var stringPassion = MutableLiveData("")
    val onSuccessOccurred = SingleEvent()
    val onFailedOccurred = SingleEvent()
    var education = MutableLiveData("")
    var doYouSmoke = MutableLiveData("")
    var doYouDrink = MutableLiveData("")
    var zodiac = MutableLiveData("")
    var height = MutableLiveData("")
    var language = MutableLiveData("")
    var bio = MutableLiveData("")

    fun onBack() {
        onBackOccured.actionOccured()
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

    fun addPassion() {
        val hashMap = HashMap<String, Any>()
        hashMap[Constants.PASSION] = stringPassion.value.toString()
        firebaseDatabase.reference.child(Constants.USERS)
            .child(firebaseAuth.uid.toString()).updateChildren(hashMap)
            .addOnSuccessListener {
                onSuccessOccurred.actionOccured()
            }
            .addOnFailureListener {
                onFailedOccurred.actionOccured()
            }
    }

    fun addEducation() {
        firebaseDatabase.reference.child(Constants.USERS).child(firebaseAuth.uid.toString()).child(
            Constants.EDUCATION
        ).removeValue()
        val hashMap = HashMap<String, Any>()
        hashMap[Constants.EDUCATION] = education.value.toString()
        firebaseDatabase.reference.child(Constants.USERS)
            .child(firebaseAuth.uid.toString()).updateChildren(hashMap)
            .addOnSuccessListener {
                onSuccessOccurred.actionOccured()
            }
            .addOnFailureListener {
                onFailedOccurred.actionOccured()
            }
    }

    fun addDoYouSmoke() {
        firebaseDatabase.reference.child(Constants.USERS).child(firebaseAuth.uid.toString())
            .child(Constants.SMOKING).removeValue()
        val hashMap = HashMap<String, Any>()
        hashMap[Constants.SMOKING] = doYouSmoke.value.toString()
        firebaseDatabase.reference.child(Constants.USERS)
            .child(firebaseAuth.uid.toString()).updateChildren(hashMap)
            .addOnSuccessListener {
                onSuccessOccurred.actionOccured()
            }
            .addOnFailureListener {
                onFailedOccurred.actionOccured()
            }
    }

    fun addDoYouDrink() {
        firebaseDatabase.reference.child(Constants.USERS).child(firebaseAuth.uid.toString())
            .child(Constants.DRINKING).removeValue()
        val hashMap = HashMap<String, Any>()
        hashMap[Constants.DRINKING] = doYouDrink.value.toString()
        firebaseDatabase.reference.child(Constants.USERS)
            .child(firebaseAuth.uid.toString()).updateChildren(hashMap)
            .addOnSuccessListener {
                onSuccessOccurred.actionOccured()
            }
            .addOnFailureListener {
                onFailedOccurred.actionOccured()
            }
    }

    fun addZodiac() {
        firebaseDatabase.reference.child(Constants.USERS).child(firebaseAuth.uid.toString())
            .child(Constants.Zodiac).removeValue()
        val hashMap = HashMap<String, Any>()
        hashMap[Constants.Zodiac] = zodiac.value.toString()
        firebaseDatabase.reference.child(Constants.USERS)
            .child(firebaseAuth.uid.toString()).updateChildren(hashMap)
            .addOnSuccessListener {
                onSuccessOccurred.actionOccured()
            }
            .addOnFailureListener {
                onFailedOccurred.actionOccured()
            }
    }

    fun addHeight() {
        firebaseDatabase.reference.child(Constants.USERS).child(firebaseAuth.uid.toString())
            .child(Constants.HEIGHT).removeValue()
        val hashMap = HashMap<String, Any>()
        hashMap[Constants.HEIGHT] = height.value.toString()
        firebaseDatabase.reference.child(Constants.USERS)
            .child(firebaseAuth.uid.toString()).updateChildren(hashMap)
            .addOnSuccessListener {
                onSuccessOccurred.actionOccured()
            }
            .addOnFailureListener {
                onFailedOccurred.actionOccured()
            }
    }

    fun addLanguage() {
        firebaseDatabase.reference.child(Constants.USERS).child(firebaseAuth.uid.toString())
            .child(Constants.LANGUAGE).removeValue()
        val hashMap = HashMap<String, Any>()
        hashMap[Constants.LANGUAGE] = language.value.toString().trim()
        firebaseDatabase.reference.child(Constants.USERS)
            .child(firebaseAuth.uid.toString()).updateChildren(hashMap)
            .addOnSuccessListener {
                onSuccessOccurred.actionOccured()
            }
            .addOnFailureListener {
                onFailedOccurred.actionOccured()
            }
    }

    fun addBio() {
        firebaseDatabase.reference.child(Constants.USERS).child(firebaseAuth.uid.toString()).child(Constants.BIO).removeValue()
        val hashMap = HashMap<String, Any>()
        hashMap[Constants.BIO] = bio.value.toString().trim()
        firebaseDatabase.reference.child(Constants.USERS)
            .child(firebaseAuth.uid.toString()).updateChildren(hashMap)
            .addOnSuccessListener {
                onSuccessOccurred.actionOccured()
            }
            .addOnFailureListener {
                onFailedOccurred.actionOccured()
            }
    }
}