package com.dil.singles.fragments.register

import android.content.Context
import android.text.TextUtils
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dil.singles.helper.*
import com.dil.singles.helper.Constants.BANNED
import com.dil.singles.helper.Constants.EMAIL
import com.dil.singles.helper.Constants.PASSWORD
import com.dil.singles.helper.Constants.TIME
import com.dil.singles.helper.Constants.UID
import com.dil.singles.helper.Constants.USERS
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern

class RegisterViewModel : BaseViewModel() {
    private val _navigationLiveData = SingleLiveEvent<Int>()
    val navigationLiveData: LiveData<Int> = _navigationLiveData
    val emailMutableData = MutableLiveData("")
    val passwordMutableData = MutableLiveData("")
    val onSuccessOccurred = SingleEvent()
    val onFailedOccurred = SingleEvent()
    val showLoader = ObservableBoolean(false)

    fun onNavigationClicked(actionId: Int) {
        _navigationLiveData.value = actionId
    }

    fun register(context: Context, email: String, password: String) {
        showLoader.set(true)
        firebaseAuth.signInWithEmailAndPassword(
            email,
            password
        ).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    SharedPrefData(context).saveDataString(EMAIL, email)
                    SharedPrefData(context).saveDataString(PASSWORD, password)
                    onSuccessOccurred.actionOccured()
                } else {
                        firebaseAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val time = Date().time
                                val hashMap = HashMap<String, String>()
                                hashMap[UID] = firebaseAuth.uid.toString()
                                hashMap[EMAIL] = email
                                hashMap[PASSWORD] = password
                                hashMap[TIME] = time.toString()
                                hashMap[BANNED] = "No"
                                firebaseDatabase.reference.child(USERS)
                                    .child(firebaseAuth.uid.toString()).setValue(hashMap)
                                    .addOnSuccessListener {
                                        SharedPrefData(context).saveDataString(EMAIL, email)
                                        SharedPrefData(context).saveDataString(PASSWORD, password)
                                        onSuccessOccurred.actionOccured()
                                    }
                                    .addOnFailureListener {
                                        onFailedOccurred.actionOccured()
                                        showLoader.set(false)
                                    }
                            }else
                            {
                                showLoader.set(false)
                            }
                        }

                }
            }
    }

    fun isValidEmail(email: String): Boolean {
        val emailPattern =
            "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@" + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?" + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\." + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?" + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|" + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$"
        return !TextUtils.isEmpty(email) && Pattern.compile(emailPattern).matcher(email).matches();
    }

    fun isValidPassword(password: String): Boolean {
        val pattern: Pattern
        val passwordPattern = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$"
        pattern = Pattern.compile(passwordPattern)
        val matcher: Matcher = pattern.matcher(password)
        return !TextUtils.isEmpty(password) && matcher.matches()
    }

}