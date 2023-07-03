package com.dil.singles.fragments.likes

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.MutableLiveData
import com.dil.singles.helper.BaseViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class LikeViewModel : BaseViewModel() {
    val showLoader = ObservableBoolean(true)
    val noLikesObserver = ObservableBoolean(true)
    val textObserver = ObservableBoolean()
    val searchText = MutableLiveData("")
    val searchObserver = ObservableBoolean()

    fun loadLikes(): ArrayList<LikesModel> {
        noLikesObserver.set(false)
        showLoader.set(true)
        val users: ArrayList<LikesModel> = ArrayList()
        firebaseDatabase.reference.child("userLikes").child(firebaseAuth.uid.toString())
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    users.clear()
                    if (snapshot.exists()) {
                        noLikesObserver.set(false)
                        for (snapshot in snapshot.children) {
                            val user = snapshot.getValue(LikesModel::class.java)
                            if (user != null) {
                                searchObserver.set(true)
                                users.add(user)
                            }
                        }
                    } else {
                        noLikesObserver.set(true)
                        searchObserver.set(false)
                    }
                    showLoader.set(false)
                }

                override fun onCancelled(error: DatabaseError) {}
            })

        return users
    }
}