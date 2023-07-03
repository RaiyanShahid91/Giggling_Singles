package com.dil.singles.helper

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

open class BaseViewModel : ViewModel() {

    val genericErrorOccurred = SingleEvent()
    val noInternetOccurred = SingleEvent()

    var firebaseAuth = FirebaseAuth.getInstance()
    var firebaseDatabase = FirebaseDatabase.getInstance()
}