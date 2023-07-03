package com.dil.singles.fragments.home

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.Chronometer
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.cardview.widget.CardView
import androidx.databinding.ObservableBoolean
import com.dil.singles.R
import com.dil.singles.helper.*
import com.dil.singles.helper.Constants.COUNTRY
import com.dil.singles.helper.Constants.GLOBAL
import com.dil.singles.helper.Constants.SHOWUSERLIST
import com.dil.singles.helper.Constants.STATE
import com.dil.singles.helper.Constants.WHOMTODATE
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class HomeViewModel : BaseViewModel() {

    val showLoader = ObservableBoolean(true)
    val noUserFound = ObservableBoolean(false)
    val recyclerViewObserver = ObservableBoolean(false)
    val globalOccurred = SingleEvent()

    fun likeUser(context: Context) {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.layout_like)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
        Handler().postDelayed(Runnable {
            dialog.dismiss()
        }, 1000)
    }

    fun disLikeUser(context: Context) {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.layout_dislike)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
        Handler().postDelayed(Runnable {
            dialog.dismiss()
        }, 1000)
    }

    fun showCurrentAreaList(context: Context): ArrayList<HomeLandingModel> {
        showLoader.set(true)
        recyclerViewObserver.set(false)
        noUserFound.set(false)
        val items: ArrayList<HomeLandingModel> = ArrayList()
        firebaseDatabase.reference.child(Constants.USERS)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
//                    items.clear()
                    showLoader.set(false)
                    for (snapshot in snapshot.children) {
                        val user: HomeLandingModel? =
                            snapshot.getValue(HomeLandingModel::class.java)
                        if (user != null) {
                            if (!user.uid.equals(firebaseAuth.uid) && user.gender.equals(SharedPrefData(context).getDataString(WHOMTODATE))) {
                                if (user.state.equals(SharedPrefData(context).getDataString(STATE)) && user.country.equals(SharedPrefData(context).getDataString(COUNTRY))) {
                                    recyclerViewObserver.set(true)
                                    noUserFound.set(false)
                                    items.add(user)
                                } else {
                                    noUserFound.set(true)
                                    recyclerViewObserver.set(false)
                                }
                            }
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
        return items
    }

    fun showGlobalList(context: Context): ArrayList<HomeLandingModel> {
        showLoader.set(true)
        noUserFound.set(false)
        recyclerViewObserver.set(false)
        val items: ArrayList<HomeLandingModel> = ArrayList()
        firebaseDatabase.reference.child(Constants.USERS)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
//                    items.clear()
                    showLoader.set(false)
                    for (snapshot in snapshot.children) {
                        val user: HomeLandingModel? =
                            snapshot.getValue(HomeLandingModel::class.java)
                        if (user != null) {
                            if (!user.uid.equals(firebaseAuth.uid) && user.gender.equals(SharedPrefData(context).getDataString(WHOMTODATE))) {
                                items.add(user)
                                recyclerViewObserver.set(true)
                            }
                        }
                    }
                }
                    override fun onCancelled(error: DatabaseError) {
                    }
                })
                return items
            }

                fun getCurrentLocation(context: Context) {
                    val bottomSheetDialog =
                        BottomSheetDialog(context, R.style.BottomSheetDialogTheme)
                    val bottomSheetView: View = LayoutInflater.from(context).inflate(
                        R.layout.layout_bottom_map,
                        bottomSheetDialog.findViewById(R.id.bottomSheetMap)
                    )
                    bottomSheetDialog.setCancelable(false)

                    firebaseDatabase.reference.child(Constants.USERS)
                        .child(firebaseAuth.uid.toString())
                        .addValueEventListener(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                val latitude =
                                    snapshot.child("latitude").getValue(Double::class.java)
                                val longitude =
                                    snapshot.child("longitude").getValue(Double::class.java)
                                bottomSheetView.findViewById<TextView>(R.id.map_location).text =
                                    getCurrentAddress(
                                        latitude!!, longitude!!, context
                                    )
                            }

                            override fun onCancelled(error: DatabaseError) {
                            }
                        })

                    bottomSheetView.findViewById<ImageView>(R.id.close).setOnClickListener {
                        bottomSheetDialog.dismiss()
                    }
                    bottomSheetDialog.setContentView(bottomSheetView)
                    bottomSheetDialog.show()
                }

                fun goGlobal(context: Context) {
                    SharedPrefData(context).saveDataString(SHOWUSERLIST, GLOBAL)
                    globalOccurred.actionOccured()
                }
    }