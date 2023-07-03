package com.dil.singles.fragments.chats

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.dil.singles.R
import com.dil.singles.activity.ChatActivity
import com.dil.singles.databinding.LayoutChatItemsBinding
import com.dil.singles.helper.Constants.FIRSTNAME
import com.dil.singles.helper.Constants.LASTNAME
import com.dil.singles.helper.Constants.TOKEN
import com.dil.singles.helper.Constants.UID
import com.dil.singles.helper.TimeAgo
import com.dil.singles.helper.convertBase64ToBitmap
import com.dil.singles.helper.loadUserProfile
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*

class ChatListAdapter(var context: Context, users: ArrayList<UserList>) :
    RecyclerView.Adapter<ChatListAdapter.UsersViewHolder>(), Filterable {

    var users: ArrayList<UserList> = users
    var searchList: ArrayList<UserList>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersViewHolder {
        val view: View =
            LayoutInflater.from(context).inflate(R.layout.layout_chat_items, parent, false)
        return UsersViewHolder(view)
    }

    override fun onBindViewHolder(holder: UsersViewHolder, position: Int) {
        val user = searchList[position]
        val senderId = FirebaseAuth.getInstance().uid
        val senderRoom = senderId + user.uid

        FirebaseDatabase.getInstance().reference
            .child("chats")
            .child(senderRoom)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val lastMsg = snapshot.child("lastMsg").getValue(String::class.java)
                        val time = snapshot.child("lastMsgTime").getValue(Long::class.java)
                        val timeAgo = TimeAgo.getTimeAgo(time.toString().toLong())
                        holder.binding.msgTime.text = timeAgo
                        holder.binding.lastMsg.text = lastMsg
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            })
        holder.binding.profile.setImageBitmap(convertBase64ToBitmap(user.firstPhoto.toString()))

        "${user.firstName} ${user.lastName}".also { holder.binding.username.text = it }


        holder.itemView.setOnClickListener {
            val intent = Intent(context, ChatActivity::class.java)
            intent.putExtra(UID, user.uid)
            intent.putExtra(FIRSTNAME, user.firstName)
            intent.putExtra(LASTNAME, user.lastName)
            context.startActivity(intent)
        }

        holder.binding.profile.setOnClickListener {
            loadUserProfile(user.uid.toString(), context)
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence): FilterResults {
                val character = constraint.toString()
                searchList = if (character.isEmpty()) {
                    users
                } else {
                    val filterList: ArrayList<UserList> =
                        ArrayList()
                    for (row in users) {
                        if (row.firstName.toString().lowercase(Locale.getDefault())
                                .contains(character.lowercase(Locale.getDefault())) || row.lastName.toString()
                                .lowercase(Locale.getDefault())
                                .contains(character.lowercase(Locale.getDefault()))
                        ) {
                            filterList.add(row)
                        }
                    }
                    filterList
                }
                val filterResults = FilterResults()
                filterResults.values = searchList
                return filterResults
            }

            override fun publishResults(contraints: CharSequence?, results: FilterResults) {
                searchList = results.values as ArrayList<UserList>
                notifyDataSetChanged()
            }
        }
    }


    override fun getItemCount(): Int {
        return searchList.size
    }

    inner class UsersViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var binding: LayoutChatItemsBinding = LayoutChatItemsBinding.bind(itemView)

    }

    init {
        this.searchList = users
    }
}
