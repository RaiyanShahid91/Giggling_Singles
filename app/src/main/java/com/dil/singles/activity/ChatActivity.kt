package com.dil.singles.activity

import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.dil.singles.R
import com.dil.singles.databinding.ActivityChatBinding
import com.dil.singles.fragments.blocked.BlockedModel
import com.dil.singles.fragments.chats.Message
import com.dil.singles.fragments.chats.MessagesAdapter
import com.dil.singles.helper.Constants
import com.dil.singles.helper.Constants.FIRSTNAME
import com.dil.singles.helper.Constants.LASTNAME
import com.dil.singles.helper.Constants.PROFILEPHOTO
import com.dil.singles.helper.Constants.TIME
import com.dil.singles.helper.Constants.TOKEN
import com.dil.singles.helper.Constants.UID
import com.dil.singles.helper.convertBase64ToBitmap
import com.dil.singles.helper.firebasemessaging.NotificationData
import com.dil.singles.helper.firebasemessaging.PushNotification
import com.dil.singles.helper.firebasemessaging.RetrofitInstance
import com.dil.singles.helper.loadUserProfile
import com.dil.singles.helper.showKeyboard
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import com.vanniktech.emoji.EmojiManager
import com.vanniktech.emoji.EmojiPopup
import com.vanniktech.emoji.EmojiTextView
import com.vanniktech.emoji.google.GoogleEmojiProvider
import kotlinx.coroutines.*
import org.json.JSONObject
import java.lang.Runnable
import java.util.*


class ChatActivity : AppCompatActivity() {

    var uid: String? = null
    lateinit var chatBinding: ActivityChatBinding
    var senderRoom: String? = null
    var receiverRoom: String? = null
    var senderUID: String? = null
    lateinit var database: FirebaseDatabase
    var isBlocked: Boolean = false
    var photoOne: String? = null
    var firstName: String? = null
    var lastName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        chatBinding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(chatBinding.root)
        EmojiManager.install(GoogleEmojiProvider())

        uid = intent.getStringExtra(UID)

        senderRoom = FirebaseAuth.getInstance().uid.toString() + uid
        receiverRoom = uid + FirebaseAuth.getInstance().uid.toString()
        database = FirebaseDatabase.getInstance()
        senderUID = FirebaseAuth.getInstance().uid

        listener()
        loadChats()
        blockedUser()
    }

    private fun listener() {

        val popup: EmojiPopup = EmojiPopup.Builder
            .fromRootView(findViewById(R.id.rootView)).build(chatBinding.etMessage)

        database.reference.child("presence").child(uid.toString())
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val status = snapshot.getValue(String::class.java)
                        if (status?.isNotEmpty() == true) {
                            if (status == "Offline") {
                                chatBinding.txtStatus.visibility = View.GONE
                            } else {
                                chatBinding.txtStatus.text = status
                                chatBinding.txtStatus.visibility = View.VISIBLE
                            }
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            })

        chatBinding.sendBtn.setOnClickListener {
            val messageTxt = chatBinding.etMessage.text.toString()
            val date = Date()
            val message = Message(messageTxt, FirebaseAuth.getInstance().uid.toString(), date.time)
            chatBinding.etMessage.setText("")

            val randomKey = database.reference.push().key

            val lastMsgObj = HashMap<String, Any>()
            lastMsgObj["lastMsg"] = message.message.toString()
            lastMsgObj["lastMsgTime"] = date.time

            senderRoom?.let { it ->
                database.reference.child("chats").child(it).updateChildren(lastMsgObj)
            }
            receiverRoom?.let { it ->
                database.reference.child("chats").child(it).updateChildren(lastMsgObj)
            }

            database.reference.child("chats")
                .child(senderRoom.toString())
                .child("messages")
                .child(randomKey.toString())
                .setValue(message).addOnSuccessListener {}

            database.reference.child("chats")
                .child(receiverRoom.toString())
                .child("messages")
                .child(randomKey.toString())
                .setValue(message).addOnSuccessListener {}


            sentChatList()
            receiverChatList()
        }

        chatBinding.back.setOnClickListener {
            finish()
        }

        val handler = Handler()
        chatBinding.etMessage.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.isNotEmpty()) {
                    chatBinding.sendBtn.visibility = View.VISIBLE
                    database.reference.child("presence").child(senderUID.toString())
                        .setValue("typing...")
                    handler.removeCallbacksAndMessages(null)
                    handler.postDelayed(userStoppedTyping, 1000)
                } else {
                    chatBinding.sendBtn.visibility = View.GONE
                }
            }

            override fun afterTextChanged(s: Editable) {}
            var userStoppedTyping =
                Runnable {
                    database.reference.child("presence").child(senderUID.toString())
                        .setValue("Online")
                }
        })

        chatBinding.addEmoticons.setOnClickListener {
            popup.toggle()
            chatBinding.rootView.addView(getEmojiTextView())
            chatBinding.addEmoticons.visibility = View.GONE
            chatBinding.addTextKeyboard.visibility = View.VISIBLE
        }

        chatBinding.addTextKeyboard.setOnClickListener {
            chatBinding.addEmoticons.visibility = View.VISIBLE
            chatBinding.addTextKeyboard.visibility = View.GONE
            popup.dismiss()
            showKeyboard(this, chatBinding.etMessage)
        }

        chatBinding.more.setOnClickListener {
            showMoreOption()
        }

        chatBinding.blockedTv.setOnClickListener {
            database.reference.child("Blocked")
                .child(senderUID.toString()).child(uid.toString())
                .removeValue().addOnSuccessListener {
                    chatBinding.blockedTv.visibility = View.GONE
                    isBlocked = false
                }
        }

        chatBinding.profileImage.setOnClickListener {
            loadUserProfile(uid.toString(), this)
        }

        chatBinding.hiAnim.setOnClickListener {
            val date = Date()
            val message = Message("Hi", FirebaseAuth.getInstance().uid.toString(), date.time)
            chatBinding.etMessage.setText("")

            val randomKey = database.reference.push().key

            val lastMsgObj = HashMap<String, Any>()
            lastMsgObj["lastMsg"] = message.message.toString()
            lastMsgObj["lastMsgTime"] = date.time

            chatBinding.chatRecycler.visibility = View.VISIBLE
            chatBinding.hiLayout.visibility = View.GONE

            senderRoom?.let { it ->
                database.reference.child("chats").child(it).updateChildren(lastMsgObj)
            }
            receiverRoom?.let { it ->
                database.reference.child("chats").child(it).updateChildren(lastMsgObj)
            }

            database.reference.child("chats")
                .child(senderRoom.toString())
                .child("messages")
                .child(randomKey.toString())
                .setValue(message).addOnSuccessListener {}

            database.reference.child("chats")
                .child(receiverRoom.toString())
                .child("messages")
                .child(randomKey.toString())
                .setValue(message).addOnSuccessListener {}

            var topic = "/topics/$uid"
            PushNotification(
                NotificationData( "$firstName $lastName",message.message.toString()),
                topic).also {
                sendNotification(it)
            }

            sentChatList()
            receiverChatList()
        }
    }

    private fun getEmojiTextView(): EmojiTextView {
        val tvEmoji = LayoutInflater
            .from(applicationContext)
            .inflate(R.layout.text_view_emoji, chatBinding.rootView, false) as EmojiTextView
        tvEmoji.text = chatBinding.etMessage.text.toString()
        return tvEmoji
    }

    private fun sentChatList() {
        FirebaseDatabase.getInstance().reference.child(Constants.USERS)
            .child(uid.toString())
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val photoOne =
                        snapshot.child(PROFILEPHOTO).getValue(String::class.java)
                    val firstName = snapshot.child(FIRSTNAME).getValue(String::class.java)
                    val lastName = snapshot.child(LASTNAME).getValue(String::class.java)
                    val userUid = snapshot.child(UID).getValue(String::class.java)

                    val date = Date()
                    val userList = HashMap<String, Any>()
                    userList[FIRSTNAME] = firstName.toString()
                    userList[LASTNAME] = lastName.toString()
                    userList[PROFILEPHOTO] = photoOne.toString()
                    userList[UID] = userUid.toString()
                    userList[TIME] = date.time.toString()

                    database.reference.child("ChatList")
                        .child(FirebaseAuth.getInstance().uid.toString()).child(userUid.toString())
                        .setValue(userList)
                        .addOnSuccessListener { }
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
    }

    private fun receiverChatList() {
        FirebaseDatabase.getInstance().reference.child(Constants.USERS)
            .child(FirebaseAuth.getInstance().uid.toString())
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val photoOne = snapshot.child(PROFILEPHOTO).getValue(String::class.java)
                    val firstName = snapshot.child(FIRSTNAME).getValue(String::class.java)
                    val lastName = snapshot.child(LASTNAME).getValue(String::class.java)
                    val userUid = snapshot.child(UID).getValue(String::class.java)

                    val date = Date()
                    val userList = HashMap<String, Any>()
                    userList[FIRSTNAME] = firstName.toString()
                    userList[LASTNAME] = lastName.toString()
                    userList[PROFILEPHOTO] = photoOne.toString()
                    userList[UID] = userUid.toString()
                    userList[TIME] = date.time.toString()

                    database.reference.child("ChatList")
                        .child(uid.toString()).child(userUid.toString()).setValue(userList)
                        .addOnSuccessListener { }
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
    }

    private fun loadChats() {
        var messages: ArrayList<Message> = ArrayList()
        val adapter = MessagesAdapter(this, messages, senderRoom, receiverRoom)
        database.reference.child("chats")
            .child(senderRoom.toString())
            .child("messages")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    messages.clear()
                    for (snapshot1 in snapshot.children) {
                        val message = snapshot1.getValue(Message::class.java)
                        snapshot1.key?.let { message?.setMessageId(it) }
                        messages.add(message!!)
                    }
                    adapter.notifyDataSetChanged()
                    chatBinding.chatRecycler.invalidate()
                    if (messages.size > 0) {
                        chatBinding.chatRecycler.smoothScrollToPosition(messages.size - 1)
                    }

                    if (messages.size > 0) {
                        chatBinding.hiLayout.visibility = View.GONE
                        chatBinding.chatRecycler.visibility = View.VISIBLE

                    } else {
                        chatBinding.hiLayout.visibility = View.VISIBLE
                        chatBinding.chatRecycler.visibility = View.GONE
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            })

        val layoutManager = LinearLayoutManager(this)
        layoutManager.stackFromEnd = true
        chatBinding.chatRecycler.layoutManager = layoutManager
        chatBinding.chatRecycler.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        FirebaseDatabase.getInstance().reference.child(Constants.USERS)
            .child(uid.toString())
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    photoOne =
                        snapshot.child(PROFILEPHOTO).getValue(String::class.java)
                    firstName = snapshot.child(FIRSTNAME).getValue(String::class.java)
                    lastName = snapshot.child(LASTNAME).getValue(String::class.java)
                    chatBinding.textName.text = "$firstName $lastName"
                    chatBinding.profileImage.setImageBitmap(convertBase64ToBitmap(photoOne.toString()))
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
    }

    private fun showMoreOption() {
        val bottomSheetDialog =
            BottomSheetDialog(this, R.style.BottomSheetDialogTheme)
        val bottomSheetView: View = LayoutInflater.from(this).inflate(
            R.layout.layout_more_details,
            bottomSheetDialog.findViewById(R.id.bottomsheetMoreDetails)
        )
        var blockedTv = bottomSheetView.findViewById<TextView>(R.id.blockTv)
        if (isBlocked) {
            blockedTv.visibility = View.GONE
        } else {
            blockedTv.visibility = View.VISIBLE
        }

        bottomSheetView.findViewById<AppCompatButton>(R.id.cancelBtn).setOnClickListener {
            bottomSheetDialog.dismiss()
        }

        bottomSheetView.findViewById<TextView>(R.id.clearTv).setOnClickListener {
            clearChat()
            bottomSheetDialog.dismiss()
        }

        bottomSheetView.findViewById<TextView>(R.id.reportTv).setOnClickListener {
            report()
            bottomSheetDialog.dismiss()
        }

        bottomSheetView.findViewById<TextView>(R.id.wallpaperTv).setOnClickListener {
            bottomSheetDialog.dismiss()
        }

        bottomSheetView.findViewById<TextView>(R.id.profileTv).setOnClickListener {
            bottomSheetDialog.dismiss()
            loadUserProfile(uid.toString(), this)
        }

        blockedTv.setOnClickListener {
            val time = Date()
            val hashMap = HashMap<String?, String?>()
            hashMap[TIME] = time.toString()
            hashMap[UID] = uid
            hashMap[FIRSTNAME] = firstName
            hashMap[LASTNAME] = lastName
            hashMap[PROFILEPHOTO] = photoOne
            database.reference.child("Blocked").child(senderUID.toString()).child(uid.toString())
                .setValue(hashMap)
                .addOnSuccessListener {
                    bottomSheetDialog.dismiss()
                }
        }

        bottomSheetDialog.setContentView(bottomSheetView)
        bottomSheetDialog.show()
    }

    private fun report() {
        val bottomSheetDialog = BottomSheetDialog(this, R.style.BottomSheetDialogTheme)
        val bottomsheetView: View = LayoutInflater.from(this).inflate(
            R.layout.layout_report,
            bottomSheetDialog.findViewById<View>(R.id.bottomsheetMoreDetails) as NestedScrollView?
        )

        bottomsheetView.findViewById<View>(R.id.first).setOnClickListener {
            val time = Date()
            val hashMap = HashMap<String?, String?>()
            hashMap["report"] = "It's spam"
            hashMap["time"] = time.toString()
            hashMap["uid"] = uid
            database.reference.child("Report").child(senderUID.toString()).child(uid.toString())
                .setValue(hashMap)
                .addOnSuccessListener {
                    bottomSheetDialog.dismiss()
                }
        }

        bottomsheetView.findViewById<View>(R.id.second).setOnClickListener {
            val time = Date()
            val hashMap = HashMap<String?, String?>()
            hashMap["report"] = "Nudity or sexual activity"
            hashMap["time"] = time.toString()
            hashMap["uid"] = uid
            database.reference.child("Report").child(senderUID.toString()).child(uid.toString())
                .setValue(hashMap)
                .addOnSuccessListener {
                    bottomSheetDialog.dismiss()
                }
        }

        bottomsheetView.findViewById<View>(R.id.third).setOnClickListener {
            val time = Date()
            val hashMap = HashMap<String?, String?>()
            hashMap["report"] = "I just don't like it"
            hashMap["time"] = time.toString()
            hashMap["uid"] = uid
            database.reference.child("Report").child(senderUID.toString()).child(uid.toString())
                .setValue(hashMap)
                .addOnSuccessListener {
                    bottomSheetDialog.dismiss()
                }
        }

        bottomsheetView.findViewById<View>(R.id.fourth).setOnClickListener {
            val time = Date()
            val hashMap = HashMap<String?, String?>()
            hashMap["report"] = "Scam or fraud"
            hashMap["time"] = time.toString()
            hashMap["uid"] = uid
            database.reference.child("Report").child(senderUID.toString()).child(uid.toString())
                .setValue(hashMap)
                .addOnSuccessListener {
                    bottomSheetDialog.dismiss()
                }
        }

        bottomsheetView.findViewById<View>(R.id.fifth).setOnClickListener {
            val time = Date()
            val hashMap = HashMap<String?, String?>()
            hashMap["report"] = "Hate speech or symbols"
            hashMap["time"] = time.toString()
            hashMap["uid"] = uid
            database.reference.child("Report").child(senderUID.toString()).child(uid.toString())
                .setValue(hashMap)
                .addOnSuccessListener {
                    bottomSheetDialog.dismiss()
                }
        }

        bottomsheetView.findViewById<View>(R.id.sixth).setOnClickListener {
            val time = Date()
            val hashMap = HashMap<String?, String?>()
            hashMap["report"] = "False information"
            hashMap["time"] = time.toString()
            hashMap["uid"] = uid
            database.reference.child("Report").child(senderUID.toString()).child(uid.toString())
                .setValue(hashMap)
                .addOnSuccessListener {
                    bottomSheetDialog.dismiss()
                }
        }

        bottomsheetView.findViewById<View>(R.id.seventh).setOnClickListener {
            val time = Date()
            val hashMap = HashMap<String?, String?>()
            hashMap["report"] = "Bullying or harassment"
            hashMap["time"] = time.toString()
            hashMap["uid"] = uid
            database.reference.child("Report").child(senderUID.toString()).child(uid.toString())
                .setValue(hashMap)
                .addOnSuccessListener {
                    bottomSheetDialog.dismiss()
                }
        }

        bottomsheetView.findViewById<View>(R.id.eight).setOnClickListener {
            val time = Date()
            val hashMap = HashMap<String?, String?>()
            hashMap["report"] = "Violence or dangerous organisations"
            hashMap["time"] = time.toString()
            hashMap["uid"] = uid
            database.reference.child("Report").child(senderUID.toString()).child(uid.toString())
                .setValue(hashMap)
                .addOnSuccessListener {
                    bottomSheetDialog.dismiss()
                }
        }

        bottomsheetView.findViewById<View>(R.id.ninth).setOnClickListener {
            val time = Date()
            val hashMap = HashMap<String?, String?>()
            hashMap["report"] = "Intellectual property violation"
            hashMap["time"] = time.toString()
            hashMap["uid"] = uid
            database.reference.child("Report").child(senderUID.toString()).child(uid.toString())
                .setValue(hashMap)
                .addOnSuccessListener {
                    bottomSheetDialog.dismiss()
                }
        }

        bottomsheetView.findViewById<View>(R.id.tenth).setOnClickListener {
            val time = Date()
            val hashMap = HashMap<String?, String?>()
            hashMap["report"] = "Sale of illegal or regulated goods"
            hashMap["time"] = time.toString()
            hashMap["uid"] = uid
            database.reference.child("Report").child(senderUID.toString()).child(uid.toString())
                .setValue(hashMap)
                .addOnSuccessListener {
                    bottomSheetDialog.dismiss()
                }
        }

        bottomsheetView.findViewById<View>(R.id.eleventh).setOnClickListener {
            val time = Date()
            val hashMap = HashMap<String?, String?>()
            hashMap["report"] = "Suicide or self-injury"
            hashMap["time"] = time.toString()
            hashMap["uid"] = uid
            database.reference.child("Report").child(senderUID.toString()).child(uid.toString())
                .setValue(hashMap)
                .addOnSuccessListener {
                    bottomSheetDialog.dismiss()
                }
        }

        bottomsheetView.findViewById<View>(R.id.twelve).setOnClickListener {
            val time = Date()
            val hashMap = HashMap<String?, String?>()
            hashMap["report"] = "Eating disorders"
            hashMap["time"] = time.toString()
            hashMap["uid"] = uid
            database.reference.child("Report").child(senderUID.toString()).child(uid.toString())
                .setValue(hashMap)
                .addOnSuccessListener {
                    bottomSheetDialog.dismiss()
                }
        }

        bottomsheetView.findViewById<AppCompatButton>(R.id.close_label).setOnClickListener {
            bottomSheetDialog.dismiss()
        }

        bottomSheetDialog.setContentView(bottomsheetView)
        bottomSheetDialog.show()
    }

    private fun clearChat() {
        val bottomSheetDialog = BottomSheetDialog(this, R.style.BottomSheetDialogTheme)
        val bottomSheetView: View = LayoutInflater.from(this).inflate(
            R.layout.layout_clear_chat,
            bottomSheetDialog.findViewById(R.id.bottomSheetClearChat)
        )

        bottomSheetView.findViewById<AppCompatButton>(R.id.clearChatBtn).setOnClickListener {
            bottomSheetDialog.setCancelable(false)
            database.reference.child("ChatList")
                .child(FirebaseAuth.getInstance().uid.toString()).child(uid.toString())
                .removeValue()
            database.reference.child("chats")
                .child(senderRoom.toString())
                .removeValue()
            bottomSheetDialog.dismiss()
        }

        bottomSheetView.findViewById<AppCompatButton>(R.id.cancelBtn).setOnClickListener {
            bottomSheetDialog.dismiss()
        }
        bottomSheetDialog.setContentView(bottomSheetView)
        bottomSheetDialog.show()
    }

    private fun blockedUser() {
        database.reference.child("Blocked").child(senderUID.toString())
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (snapshot in dataSnapshot.children) {
                        val blockedUser = snapshot.getValue(BlockedModel::class.java)
                        if (blockedUser?.uid.equals(uid)) {
                            chatBinding.blockedTv.visibility = View.VISIBLE
                            isBlocked = true
                        } else {
                            chatBinding.blockedTv.visibility = View.GONE
                            isBlocked = false
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
    }

    private fun sendNotification(notification: PushNotification) = CoroutineScope(Dispatchers.IO).launch {
        try {
            val response = RetrofitInstance.api.postNotification(notification)
            if(response.isSuccessful) {
                Log.d("TAG", "Response: ${Gson().toJson(response)}")
            } else {
                Log.e("TAG", response.errorBody().toString())
            }
        } catch(e: Exception) {
            Log.e("TAG", e.toString())
        }
    }

}