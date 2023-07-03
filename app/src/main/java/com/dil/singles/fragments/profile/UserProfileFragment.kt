package com.dil.singles.fragments.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.AppCompatButton
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.dil.singles.R
import com.dil.singles.activity.ChatActivity
import com.dil.singles.databinding.FragmentUserProfileBinding
import com.dil.singles.helper.*
import com.dil.singles.helper.Constants.UID
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import java.util.*

class UserProfileFragment : BaseFragment() {

    private var fragmentUserProfileBinding: FragmentUserProfileBinding? = null
    private val binding get() = fragmentUserProfileBinding!!
    private val profileViewModel: ProfileViewModel by viewModels()
    var uid : String ?= null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentUserProfileBinding =
            FragmentUserProfileBinding.inflate(layoutInflater, container, false)
        binding.viewModel = profileViewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        uid = arguments?.getString(UID)
        profileViewModel.getUserProfileInfo(uid.toString())
        getProfileImage(uid.toString())
        listener(uid.toString())
    }

    private fun getProfileImage(uid: String) {
        firebaseDatabase.reference.child(Constants.USERS).child(uid)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val photoOne =
                        snapshot.child(Constants.PROFILEPHOTO).getValue(String::class.java)
                    val photoTwo =
                        snapshot.child(Constants.BACKGROUNDPHOTO).getValue(String::class.java)
                    binding.firstImage.setImageBitmap(convertBase64ToBitmap(photoTwo.toString()))
                    binding.profileImage.setImageBitmap(convertBase64ToBitmap(photoOne.toString()))
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
    }

    private fun listener(uid: String) {
        profileViewModel.onBackOccured.consume(viewLifecycleOwner)
        {
            findNavController().popBackStack()
        }

        profileViewModel.onMessageOccured.consume(viewLifecycleOwner)
        {
            val intent = Intent(context, ChatActivity::class.java)
            intent.putExtra(UID, uid)
            startActivity(intent)
        }

        profileViewModel.onCloseOccured.consume(viewLifecycleOwner)
        {
            findNavController().popBackStack()
        }

        requireActivity()
            .onBackPressedDispatcher
            .addCallback(requireActivity(), object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().popBackStack()
                }
            })

        binding.reports.setOnClickListener {
            report()
        }

        binding.micBtn.setOnClickListener {
            recordingLayout(requireContext())
        }
    }

    private fun report() {
        val bottomSheetDialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme)
        val bottomSheetView: View = LayoutInflater.from(requireContext()).inflate(
            R.layout.layout_report,
            bottomSheetDialog.findViewById<View>(R.id.bottomsheetMoreDetails) as NestedScrollView?
        )

        bottomSheetView.findViewById<View>(R.id.first).setOnClickListener {
            val time = Date()
            val hashMap = HashMap<String?, String?>()
            hashMap["report"] = "It's spam"
            hashMap["time"] = time.toString()
            hashMap["uid"] = uid
            firebaseDatabase.reference.child("Report&Hide").child(firebaseAuth.uid.toString()).child(uid.toString()).setValue(hashMap)
                .addOnSuccessListener {
                    bottomSheetDialog.dismiss()
                }
        }

        bottomSheetView.findViewById<View>(R.id.second).setOnClickListener {
            val time = Date()
            val hashMap = HashMap<String?, String?>()
            hashMap["report"] = "Nudity or sexual activity"
            hashMap["time"] = time.toString()
            hashMap["uid"] = uid
            firebaseDatabase.reference.child("Report&Hide").child(firebaseAuth.uid.toString()).child(uid.toString()).setValue(hashMap)
                .addOnSuccessListener {
                    bottomSheetDialog.dismiss()
                }
        }

        bottomSheetView.findViewById<View>(R.id.third).setOnClickListener {
            val time = Date()
            val hashMap = HashMap<String?, String?>()
            hashMap["report"] = "I just don't like it"
            hashMap["time"] = time.toString()
            hashMap["uid"] = uid
            firebaseDatabase.reference.child("Report&Hide").child(firebaseAuth.uid.toString()).child(uid.toString()).setValue(hashMap)
                .addOnSuccessListener {
                    bottomSheetDialog.dismiss()
                }
        }

        bottomSheetView.findViewById<View>(R.id.fourth).setOnClickListener {
            val time = Date()
            val hashMap = HashMap<String?, String?>()
            hashMap["report"] = "Scam or fraud"
            hashMap["time"] = time.toString()
            hashMap["uid"] = uid
            firebaseDatabase.reference.child("Report&Hide").child(firebaseAuth.uid.toString()).child(uid.toString()).setValue(hashMap)
                .addOnSuccessListener {
                    bottomSheetDialog.dismiss()
                }
        }

        bottomSheetView.findViewById<View>(R.id.fifth).setOnClickListener {
            val time = Date()
            val hashMap = HashMap<String?, String?>()
            hashMap["report"] = "Hate speech or symbols"
            hashMap["time"] = time.toString()
            hashMap["uid"] = uid
            firebaseDatabase.reference.child("Report&Hide").child(firebaseAuth.uid.toString()).child(uid.toString()).setValue(hashMap)
                .addOnSuccessListener {
                    bottomSheetDialog.dismiss()
                }
        }

        bottomSheetView.findViewById<View>(R.id.sixth).setOnClickListener {
            val time = Date()
            val hashMap = HashMap<String?, String?>()
            hashMap["report"] = "False information"
            hashMap["time"] = time.toString()
            hashMap["uid"] = uid
            firebaseDatabase.reference.child("Report&Hide").child(firebaseAuth.uid.toString()).child(uid.toString()).setValue(hashMap)
                .addOnSuccessListener {
                    bottomSheetDialog.dismiss()
                }
        }

        bottomSheetView.findViewById<View>(R.id.seventh).setOnClickListener {
            val time = Date()
            val hashMap = HashMap<String?, String?>()
            hashMap["report"] = "Bullying or harassment"
            hashMap["time"] = time.toString()
            hashMap["uid"] = uid
            firebaseDatabase.reference.child("Report&Hide").child(firebaseAuth.uid.toString()).child(uid.toString()).setValue(hashMap)
                .addOnSuccessListener {
                    bottomSheetDialog.dismiss()
                }
        }

        bottomSheetView.findViewById<View>(R.id.eight).setOnClickListener {
            val time = Date()
            val hashMap = HashMap<String?, String?>()
            hashMap["report"] = "Violence or dangerous organisations"
            hashMap["time"] = time.toString()
            hashMap["uid"] = uid
            firebaseDatabase.reference.child("Report&Hide").child(firebaseAuth.uid.toString()).child(uid.toString()).setValue(hashMap)
                .addOnSuccessListener {
                    bottomSheetDialog.dismiss()
                }
        }

        bottomSheetView.findViewById<View>(R.id.ninth).setOnClickListener {
            val time = Date()
            val hashMap = HashMap<String?, String?>()
            hashMap["report"] = "Intellectual property violation"
            hashMap["time"] = time.toString()
            hashMap["uid"] = uid
            firebaseDatabase.reference.child("Report&Hide").child(firebaseAuth.uid.toString()).child(uid.toString()).setValue(hashMap)
                .addOnSuccessListener {
                    bottomSheetDialog.dismiss()
                }
        }

        bottomSheetView.findViewById<View>(R.id.tenth).setOnClickListener {
            val time = Date()
            val hashMap = HashMap<String?, String?>()
            hashMap["report"] = "Sale of illegal or regulated goods"
            hashMap["time"] = time.toString()
            hashMap["uid"] = uid
            firebaseDatabase.reference.child("Report&Hide").child(firebaseAuth.uid.toString()).child(uid.toString()).setValue(hashMap)
                .addOnSuccessListener {
                    bottomSheetDialog.dismiss()
                }
        }

        bottomSheetView.findViewById<View>(R.id.eleventh).setOnClickListener {
            val time = Date()
            val hashMap = HashMap<String?, String?>()
            hashMap["report"] = "Suicide or self-injury"
            hashMap["time"] = time.toString()
            hashMap["uid"] = uid
            firebaseDatabase.reference.child("Report&Hide").child(firebaseAuth.uid.toString()).child(uid.toString()).setValue(hashMap)
                .addOnSuccessListener {
                    bottomSheetDialog.dismiss()
                }
        }

        bottomSheetView.findViewById<View>(R.id.twelve).setOnClickListener {
            val time = Date()
            val hashMap = HashMap<String?, String?>()
            hashMap["report"] = "Eating disorders"
            hashMap["time"] = time.toString()
            hashMap["uid"] = uid
            firebaseDatabase.reference.child("Report&Hide").child(firebaseAuth.uid.toString()).child(uid.toString()).setValue(hashMap)
                .addOnSuccessListener {
                    bottomSheetDialog.dismiss()
                }
        }

        bottomSheetView.findViewById<AppCompatButton>(R.id.close_label).setOnClickListener {
            bottomSheetDialog.dismiss()
        }

        bottomSheetDialog.setContentView(bottomSheetView)
        bottomSheetDialog.show()
    }

    override fun onResume() {
        super.onResume()
        getLastLocation(requireContext())

    }
}