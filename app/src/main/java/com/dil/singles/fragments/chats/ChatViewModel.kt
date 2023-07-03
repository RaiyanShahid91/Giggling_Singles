package com.dil.singles.fragments.chats

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.MutableLiveData
import com.dil.singles.helper.BaseViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class ChatViewModel : BaseViewModel() {

    val showLoader = ObservableBoolean(true)
    val noChatsObserver = ObservableBoolean(true)
    val textObserver = ObservableBoolean()
    val searchText = MutableLiveData("")
    val searchObserver = ObservableBoolean()

    fun loadChats(): ArrayList<UserList> {
        showLoader.set(true)
        noChatsObserver.set(false)
        val users: ArrayList<UserList> = ArrayList()
        firebaseDatabase.reference.child("ChatList").child(firebaseAuth.uid.toString())
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    users.clear()
                    if (snapshot.exists()) {
                        noChatsObserver.set(false)
                        for (snapshot in snapshot.children) {
                            val user = snapshot.getValue(UserList::class.java)
                            if (user != null) {
                                if (!user.uid.equals(firebaseAuth.uid)) {
                                    searchObserver.set(true)
                                    users.add(user)
                                }
                            }
                        }
                    } else {
                        noChatsObserver.set(true)
                        searchObserver.set(false)
                    }
                    showLoader.set(false)
                }

                override fun onCancelled(error: DatabaseError) {}
            })

        return users
    }
}