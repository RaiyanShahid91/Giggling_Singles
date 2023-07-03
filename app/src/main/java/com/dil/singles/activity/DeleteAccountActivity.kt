package com.dil.singles.activity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.dil.singles.R
import com.dil.singles.databinding.ActivityDeleteAccountBinding
import com.dil.singles.helper.Constants
import com.dil.singles.helper.Constants.EMAIL
import com.dil.singles.helper.Constants.FIRSTNAME
import com.dil.singles.helper.Constants.LASTNAME
import com.dil.singles.helper.Constants.OTHER
import com.dil.singles.helper.Constants.PASSWORD
import com.dil.singles.helper.Constants.TIME
import com.dil.singles.helper.Constants.UID
import com.dil.singles.helper.Constants.USERS
import com.dil.singles.helper.SharedPrefData
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*


class DeleteAccountActivity : AppCompatActivity() {

    lateinit var binding: ActivityDeleteAccountBinding
    var firstName: String? = null
    var lastName: String? = null
    var emailS: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDeleteAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)
        userDetails()
        listener()
    }

    private fun listener() {

        binding.txtGrpTitle.setOnClickListener { finish() }

        binding.somethingbrokenLable.setOnClickListener {
            deleteConfirm("Something is broken")
        }

        binding.idontlikelabel.setOnClickListener {
            deleteConfirm("I don't like Giggling Singles")
        }

        binding.ihavemeetLabel.setOnClickListener {
            deleteConfirm("I've met someone.")
        }

        binding.ineedbreakLabel.setOnClickListener {
            deleteConfirm("I need a break from Giggling Singles")
        }

        binding.iwantfresh.setOnClickListener {
            deleteConfirm("I want a fresh start")
        }

        binding.otherLabel.setOnClickListener {
            otherDeleteAccount()
        }
    }

    private fun deleteConfirm(message: String) {
        val bottomSheetDialog = BottomSheetDialog(this, R.style.BottomSheetDialogTheme)
        val bottomSheetView: View = LayoutInflater.from(this).inflate(
            R.layout.layout_account_delete,
            bottomSheetDialog.findViewById(R.id.bottomDeleteConfirm)
        )
        var deleteBtn = bottomSheetView.findViewById<TextView>(R.id.deleteBtn)


        deleteBtn.setOnClickListener {
            bottomSheetDialog.setCancelable(false)
            delete(SharedPrefData(this).getDataString(EMAIL),
                SharedPrefData(this).getDataString(PASSWORD),message)
        }

        bottomSheetView.findViewById<TextView>(R.id.cancelBtn).setOnClickListener {
            bottomSheetDialog.dismiss()
        }
        bottomSheetDialog.setContentView(bottomSheetView)
        bottomSheetDialog.show()
    }

    private fun otherDeleteAccount() {
        val bottomSheetDialog = BottomSheetDialog(this, R.style.BottomSheetDialogTheme)
        val bottomSheetView: View = LayoutInflater.from(this).inflate(
            R.layout.layout_delete_other,
            bottomSheetDialog.findViewById(R.id.bottomDeleteEdit)
        )
        var deleteBtn = bottomSheetView.findViewById<TextView>(R.id.deleteBtn)
        var other = bottomSheetView.findViewById<EditText>(R.id.et_email)

        deleteBtn.setOnClickListener {
            if (other.text.trim().toString().isNotEmpty()) {
                bottomSheetDialog.dismiss()
                deleteConfirm(other.text.toString())
            }

        }

        bottomSheetView.findViewById<TextView>(R.id.cancelBtn).setOnClickListener {
            bottomSheetDialog.dismiss()
        }
        bottomSheetDialog.setContentView(bottomSheetView)
        bottomSheetDialog.show()
    }

    private fun userDetails() {
        FirebaseDatabase.getInstance().reference.child(Constants.USERS)
            .child(FirebaseAuth.getInstance().uid.toString())
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    firstName = snapshot.child(FIRSTNAME).getValue(String::class.java)
                    lastName = snapshot.child(LASTNAME).getValue(String::class.java)
                    emailS = snapshot.child(EMAIL).getValue(String::class.java)
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
    }

    private fun delete(email: String, password: String,message : String) {
        val user = FirebaseAuth.getInstance().currentUser
        val auth = FirebaseAuth.getInstance()
        val credential = EmailAuthProvider.getCredential(email, password)
        val date = Date()
        val userList = HashMap<String, Any>()
        userList[FIRSTNAME] = firstName.toString()
        userList[LASTNAME] = lastName.toString()
        userList[EMAIL] = emailS.toString()
        userList[TIME] = date.toString()
        userList[UID] = auth.uid.toString()
        userList[OTHER] = message
        FirebaseDatabase.getInstance().reference.child("Deleted Account")
            .child(System.currentTimeMillis().toString()).setValue(userList)
            .addOnSuccessListener {}

        user?.reauthenticate(credential)?.addOnCompleteListener {
            user.delete()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        SharedPrefData(this@DeleteAccountActivity).sharedPreferences.edit().remove(EMAIL).apply()
                        SharedPrefData(this@DeleteAccountActivity).sharedPreferences.edit().remove(PASSWORD).apply()
                        FirebaseDatabase.getInstance().reference.child(USERS).child(auth.uid.toString()).removeValue()
                        FirebaseDatabase.getInstance().reference.child("presence").child(auth.uid.toString()).removeValue()
                        FirebaseAuth.getInstance().signOut()
                        val intent = Intent(this@DeleteAccountActivity, DashboardActivity::class.java)
                        startActivity(intent)
                        finishAffinity()
                    }
                }
        }
    }
}