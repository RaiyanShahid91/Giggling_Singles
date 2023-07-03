package com.dil.singles.fragments.home

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.dil.singles.R
import com.dil.singles.activity.ChatActivity
import com.dil.singles.helper.*
import com.dil.singles.helper.Constants.UID
import java.util.*
import kotlin.collections.ArrayList


class HomeLandingAdapter(items: ArrayList<HomeLandingModel>, context: Context) :
    RecyclerView.Adapter<HomeLandingAdapter.ViewHolder>() {
    private var items: ArrayList<HomeLandingModel>
    private var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.layout_landing_user, parent, false)
        return ViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setData(items[position])

        holder.itemView.setOnClickListener {
            val bundle = Bundle()
            bundle.putString(UID, items[position].uid.toString())
            holder.itemView.findNavController().navigate(R.id.nav_user_profile_fragment, bundle)
        }

        holder.chat.setOnClickListener {
            val intent = Intent(context, ChatActivity::class.java)
            intent.putExtra(UID, items[position].uid.toString())
            context.startActivity(intent)
        }

        holder.mic.setOnClickListener {
            audioPermission(context)
            recordingLayout(context)
        }

    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var profileImage: ImageView = itemView.findViewById(R.id.firstImage)
        var name: TextView = itemView.findViewById(R.id.name_age_txt)
        var location: TextView = itemView.findViewById(R.id.location_txt)
        var age: TextView = itemView.findViewById(R.id.name_age)
        var chat: ImageView = itemView.findViewById(R.id.card_message_layout)
        var mic: ImageButton = itemView.findViewById(R.id.mic_btn)

        @RequiresApi(Build.VERSION_CODES.O)
        fun setData(data: HomeLandingModel) {
            profileImage.setImageBitmap(convertBase64ToBitmap(data.firstPhoto.toString()))
            var convertedAge = getAge(data.year!!.toInt(), data.month!!.toInt(), data.day!!.toInt())
            age.text = "${convertedAge} yr"

            var latitude = data.latitude
            var longitude = data.longitude
            location.text = getAddress(latitude, longitude, context)
            name.text = "${data.firstName},"
        }

    }

    fun getItems(): ArrayList<HomeLandingModel> {
        return items
    }

    fun setItems(items: ArrayList<HomeLandingModel>) {
        this.items = items
    }

    init {
        this.items = items
        this.context = context
    }

}