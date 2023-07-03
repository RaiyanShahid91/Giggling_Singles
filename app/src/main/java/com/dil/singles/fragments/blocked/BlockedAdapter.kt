package com.dil.singles.fragments.blocked

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.RecyclerView
import com.dil.singles.R
import com.dil.singles.databinding.LayoutBlockedItemsBinding
import com.dil.singles.helper.convertBase64ToBitmap
import com.dil.singles.helper.showCustomToastSuccess
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class BlockedAdapter(var context: Context, var users: ArrayList<BlockedModel>) :
    RecyclerView.Adapter<BlockedAdapter.UsersViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersViewHolder {
        val view: View =
            LayoutInflater.from(context).inflate(R.layout.layout_blocked_items, parent, false)
        return UsersViewHolder(view)
    }

    override fun onBindViewHolder(holder: UsersViewHolder, position: Int) {
        val user = users[position]
        holder.binding.profile.setImageBitmap(convertBase64ToBitmap(user.firstPhoto.toString()))
        holder.binding.username.text = "${user.firstName} ${user.lastName}"

        holder.itemView.setOnClickListener {
            tapToUnblock(user.uid.toString())
        }

    }

    override fun getItemCount(): Int {
        return users.size
    }

    inner class UsersViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var binding: LayoutBlockedItemsBinding = LayoutBlockedItemsBinding.bind(itemView)

    }

    private fun tapToUnblock(uid: String) {
        val bottomSheetDialog =
            BottomSheetDialog(context, R.style.BottomSheetDialogTheme)
        val bottomSheetView: View = LayoutInflater.from(context).inflate(
            R.layout.layout_tap_to_unblock,
            bottomSheetDialog.findViewById(R.id.bottomTapToUnBlock)
        )
        val no = bottomSheetView.findViewById<AppCompatButton>(R.id.cancelBtn)
        val yes = bottomSheetView.findViewById<AppCompatButton>(R.id.removeBtn)

        yes.setOnClickListener {
            FirebaseDatabase.getInstance().reference.child("Blocked")
                .child(FirebaseAuth.getInstance().uid.toString()).child(uid)
                .removeValue().addOnSuccessListener {
                    bottomSheetDialog.dismiss()
                }
        }

        no.setOnClickListener {
            bottomSheetDialog.dismiss()
        }

        bottomSheetDialog.setContentView(bottomSheetView)
        bottomSheetDialog.show()
    }
}
