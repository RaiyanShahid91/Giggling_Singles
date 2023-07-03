package com.dil.singles.fragments.likes

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.dil.singles.R
import com.dil.singles.databinding.LayoutLikedBinding
import com.dil.singles.fragments.chats.UserList
import com.dil.singles.helper.Constants
import com.dil.singles.helper.TimeAgo
import com.dil.singles.helper.convertBase64ToBitmap
import java.util.*

class LikesAdapter(var context: Context, users: ArrayList<LikesModel>) :
    RecyclerView.Adapter<LikesAdapter.UsersViewHolder>(), Filterable {

    var users: ArrayList<LikesModel> = users
    var searchList: ArrayList<LikesModel>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.layout_liked, parent, false)
        return UsersViewHolder(view)
    }

    override fun onBindViewHolder(holder: UsersViewHolder, position: Int) {
        val user = searchList[position]

        holder.binding.profile.setImageBitmap(convertBase64ToBitmap(user.firstPhoto.toString()))
        val timeAgo = TimeAgo.getTimeAgo(user.time.toString().toLong())
        holder.binding.msgTime.text = timeAgo
        "${user.firstName} likes your profile.".also { holder.binding.username.text = it }


        holder.itemView.setOnClickListener {
            val bundle = Bundle()
            bundle.putString(Constants.UID, users[position].uid.toString())
            holder.itemView.findNavController().navigate(R.id.nav_user_profile_fragment, bundle)
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence): FilterResults {
                val character = constraint.toString()
                searchList = if (character.isEmpty()) {
                    users
                } else {
                    val filterList: ArrayList<LikesModel> =
                        ArrayList()
                    for (row in users) {
                        if (row.firstName.toString().lowercase(Locale.getDefault())
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
                searchList = results.values as ArrayList<LikesModel>
                notifyDataSetChanged()
            }
        }
    }


    override fun getItemCount(): Int {
        return searchList.size
    }

    inner class UsersViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var binding: LayoutLikedBinding = LayoutLikedBinding.bind(itemView)

    }

    init {
        this.searchList = users
    }
}