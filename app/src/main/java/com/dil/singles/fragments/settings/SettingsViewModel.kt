package com.dil.singles.fragments.settings

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.cardview.widget.CardView
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.MutableLiveData
import com.dil.singles.BuildConfig
import com.dil.singles.R
import com.dil.singles.helper.*
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SettingsViewModel : BaseViewModel() {
    val onBackPressed = SingleEvent()
    val logoutObserver = SingleEvent()
    val blockedObserver = SingleEvent()
    val versionText = MutableLiveData("")
    val emailText = MutableLiveData("")
    val interestedTxt = MutableLiveData("")
    val locationTxt = MutableLiveData("")
    val globalTxt = MutableLiveData("")
    val noBlocked = ObservableBoolean()
    val showLoader = ObservableBoolean()

    fun onBack() {
        onBackPressed.actionOccured()
    }

    fun blocked() {
        blockedObserver.actionOccured()
    }

    fun getUserDetails(uid: String, context: Context) {
        versionText.value = BuildConfig.VERSION_NAME + " version"
        firebaseDatabase.reference.child(Constants.USERS)
            .child(uid)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val email = snapshot.child(Constants.EMAIL).getValue(String::class.java)
                    val interestedIn =
                        snapshot.child(Constants.WHOMTODATE).getValue(String::class.java)
                    val city = snapshot.child(Constants.STATE).getValue(String::class.java)
                    val country = snapshot.child(Constants.COUNTRY).getValue(String::class.java)

                    emailText.value = email
                    interestedTxt.value = interestedIn
                    locationTxt.value = "$city, $country"
                    when {
                        SharedPrefData(context).getDataString(Constants.SHOWUSERLIST) == Constants.GLOBAL -> {
                            globalTxt.value = "On"
                        }
                        SharedPrefData(context).getDataString(Constants.SHOWUSERLIST) == Constants.LOCALITY -> {
                            globalTxt.value = "Off"
                        }
                        else -> {
                            globalTxt.value = "Off"
                        }
                    }

                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
    }

    fun changeEmail(context: Context) {
        val bottomSheetDialog = BottomSheetDialog(context, R.style.BottomSheetDialogTheme)
        val bottomSheetView: View = LayoutInflater.from(context).inflate(
            R.layout.layout_edit_email,
            bottomSheetDialog.findViewById(R.id.bottomEditEmail)
        )
        var etEmail = bottomSheetView.findViewById<EditText>(R.id.et_email)
        var updateBtn = bottomSheetView.findViewById<AppCompatButton>(R.id.updateBtn)
        var updatingLayout = bottomSheetView.findViewById<CardView>(R.id.updating_layout)
        var cardLayout = bottomSheetView.findViewById<CardView>(R.id.card_layout)

        bottomSheetView.findViewById<TextView>(R.id.updateBtn)?.setOnClickListener {
            if (isValidEmail(etEmail.text.trim().toString())) {
                updateBtn.visibility = View.GONE
                cardLayout.visibility = View.GONE
                updatingLayout.visibility = View.VISIBLE
                firebaseAuth.currentUser?.updateEmail(etEmail.text.trim().toString())
                    ?.addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            SharedPrefData(context).saveDataString(
                                Constants.EMAIL,
                                etEmail.text.trim().toString()
                            )
                            val hashMap = HashMap<String, Any>()
                            hashMap[Constants.EMAIL] = etEmail.text.trim().toString()
                            FirebaseDatabase.getInstance().reference.child(Constants.USERS)
                                .child(FirebaseAuth.getInstance().uid.toString())
                                .updateChildren(hashMap)
                                .addOnSuccessListener {
                                    bottomSheetDialog.dismiss()
                                }
                        }
                    }?.addOnFailureListener {
                        updateBtn.visibility = View.VISIBLE
                        cardLayout.visibility = View.VISIBLE
                        updatingLayout.visibility = View.GONE
                    }
            }
        }

        bottomSheetView.findViewById<TextView>(R.id.cancelBtn).setOnClickListener {
            bottomSheetDialog.dismiss()
        }
        bottomSheetDialog.setContentView(bottomSheetView)
        bottomSheetDialog.show()
    }

    fun goGlobal(context: Context) {
        val bottomSheetDialog = BottomSheetDialog(context, R.style.BottomSheetDialogTheme)
        val bottomSheetView: View = LayoutInflater.from(context).inflate(
            R.layout.layout_change_global,
            bottomSheetDialog.findViewById(R.id.bottomGlobal)
        )
        var goGlobalBtn = bottomSheetView.findViewById<TextView>(R.id.goGlobalBtn)
        var turnOffGobal = bottomSheetView.findViewById<TextView>(R.id.turnoffGlobal)

        when {
            SharedPrefData(context).getDataString(Constants.SHOWUSERLIST) == Constants.GLOBAL -> {
                goGlobalBtn.visibility = View.GONE
                turnOffGobal.visibility = View.VISIBLE

            }
            SharedPrefData(context).getDataString(Constants.SHOWUSERLIST) == Constants.LOCALITY -> {
                goGlobalBtn.visibility = View.VISIBLE
                turnOffGobal.visibility = View.GONE
            }
            else -> {
                goGlobalBtn.visibility = View.VISIBLE
                turnOffGobal.visibility = View.GONE
            }
        }

        goGlobalBtn.setOnClickListener {
            SharedPrefData(context).saveDataString(
                Constants.SHOWUSERLIST,
                Constants.GLOBAL
            )
            globalTxt.value = "On"
            bottomSheetDialog.dismiss()
        }

        turnOffGobal.setOnClickListener {
            SharedPrefData(context).saveDataString(
                Constants.SHOWUSERLIST,
                Constants.LOCALITY
            )
            globalTxt.value = "Off"
            bottomSheetDialog.dismiss()
        }

        bottomSheetView.findViewById<TextView>(R.id.cancelBtn).setOnClickListener {
            bottomSheetDialog.dismiss()
        }
        bottomSheetDialog.setContentView(bottomSheetView)
        bottomSheetDialog.show()
    }

    fun interestedIn(context: Context) {
        val bottomSheetDialog = BottomSheetDialog(context, R.style.BottomSheetDialogTheme)
        val bottomSheetView: View = LayoutInflater.from(context).inflate(
            R.layout.layout_interestedin,
            bottomSheetDialog.findViewById(R.id.bottomInterestedIn)
        )
        var saveBtn = bottomSheetView.findViewById<TextView>(R.id.saveBtn)
        var radioGroup = bottomSheetView.findViewById<RadioGroup>(R.id.radioGroup)
        var men = bottomSheetView.findViewById<RadioButton>(R.id.date_men)
        var women = bottomSheetView.findViewById<RadioButton>(R.id.date_women)
        var everyone = bottomSheetView.findViewById<RadioButton>(R.id.date_everyone)
        var selectedString: String? = null

        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.date_women -> {
                    selectedString = ""
                    selectedString = women.text.toString()
                }
                R.id.date_men -> {
                    selectedString = ""
                    selectedString = men.text.toString()
                }
                R.id.date_everyone -> {
                    selectedString = ""
                    selectedString = everyone.text.toString()
                }
            }
        }

        saveBtn.setOnClickListener {
            if (selectedString?.isNotEmpty() == true) {
                val hashMap = HashMap<String, Any>()
                hashMap[Constants.WHOMTODATE] = selectedString.toString()
                firebaseDatabase.reference.child(Constants.USERS)
                    .child(firebaseAuth.uid.toString()).updateChildren(hashMap)
                    .addOnSuccessListener {
                        bottomSheetDialog.dismiss()
                    }
            }
        }

        bottomSheetView.findViewById<TextView>(R.id.cancelBtn).setOnClickListener {
            bottomSheetDialog.dismiss()
        }
        bottomSheetDialog.setContentView(bottomSheetView)
        bottomSheetDialog.show()
    }

    fun signOut(context: Context) {
        val bottomSheetDialog =
            BottomSheetDialog(context, R.style.BottomSheetDialogTheme)
        val bottomSheetView: View = LayoutInflater.from(context).inflate(
            R.layout.layout_logout,
            bottomSheetDialog.findViewById(R.id.bottomLogout)
        )

        bottomSheetView.findViewById<TextView>(R.id.logoutBtn)?.setOnClickListener {
            SharedPrefData(context).sharedPreferences.edit().remove(Constants.EMAIL)
                .apply()
            SharedPrefData(context).sharedPreferences.edit()
                .remove(Constants.PASSWORD).apply()
            FirebaseAuth.getInstance().signOut()
            bottomSheetDialog.dismiss()
            logoutObserver.actionOccured()
        }

        bottomSheetView.findViewById<AppCompatButton>(R.id.cancelBtn).setOnClickListener {
            bottomSheetDialog.dismiss()
        }
        bottomSheetDialog.setContentView(bottomSheetView)
        bottomSheetDialog.show()
    }

    fun feedback(context: Context) {
        val rateIntent = rateIntentForUrl("https://play.google.com/store/apps/details", context)
        context.startActivity(rateIntent)
    }

    private fun rateIntentForUrl(url: String, context: Context): Intent {
        val intent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse(java.lang.String.format("%s?id=%s", url, context.packageName))
        )
        var flags = Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_ACTIVITY_MULTIPLE_TASK
        flags = if (Build.VERSION.SDK_INT >= 21) {
            flags or Intent.FLAG_ACTIVITY_NEW_DOCUMENT
        } else {
            flags or Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET
        }
        intent.addFlags(flags)
        return intent
    }

    fun shareApp(context: Context) {
        try {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Giggling Singles")
            var shareMessage = "\nLet me recommend you this application\n\n"
            shareMessage =
                "${shareMessage}\n https://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID}"
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
            context.startActivity(Intent.createChooser(shareIntent, "choose one"))
        } catch (e: Exception) {
            e.toString()
        }
    }
}