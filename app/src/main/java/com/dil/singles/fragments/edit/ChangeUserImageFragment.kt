package com.dil.singles.fragments.edit

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.dil.singles.databinding.FragmentChangeUserImageBinding
import com.dil.singles.helper.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class ChangeUserImageFragment : BaseFragment() {

    private var fragmentChangeImageBinding: FragmentChangeUserImageBinding? = null
    private val binding get() = fragmentChangeImageBinding!!
    private val profileViewModel: EditViewModel by viewModels()
    var base64First : String ?= null
    var base64Second : String ?= null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentChangeImageBinding = FragmentChangeUserImageBinding.inflate(layoutInflater, container, false)
        binding.viewModel = profileViewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listener()
        getImage()
    }



    private fun listener()
    {
        profileViewModel.onBackOccured.consume(viewLifecycleOwner){
            findNavController().popBackStack()
        }

        profileViewModel.profileImageObserved.consume(viewLifecycleOwner) {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), 10)
        }

        profileViewModel.coverImageObserver.consume(viewLifecycleOwner) {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), 20)
        }

        requireActivity()
            .onBackPressedDispatcher
            .addCallback(requireActivity(), object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().popBackStack()
                }
            })

        binding.saveBtn.setOnClickListener {
            changeFirstPhoto(Constants.PROFILEPHOTO, base64First)
            changeSecondPhoto(Constants.BACKGROUNDPHOTO, base64Second)
        }

        requireActivity()
            .onBackPressedDispatcher
            .addCallback(requireActivity(), object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().popBackStack()
                }
            })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == 10) {
            val uri = data!!.data
            binding.profileImage.setImageURI(uri)
            base64First = convertBase64(Uri.parse(uri.toString()), requireContext())
        }

        if (resultCode == Activity.RESULT_OK && requestCode == 20) {
            val uri = data!!.data
            binding.backgroundProfileImage.setImageURI(uri)
            base64Second = convertBase64(Uri.parse(uri.toString()), requireContext())
        }

    }

    private fun getImage() {
        firebaseDatabase.reference.child(Constants.USERS).child(firebaseAuth.uid.toString())
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val photoOne =
                        snapshot.child(Constants.PROFILEPHOTO).getValue(String::class.java)
                    val photoTwo =
                        snapshot.child(Constants.BACKGROUNDPHOTO).getValue(String::class.java)
                    binding.backgroundProfileImage.setImageBitmap(convertBase64ToBitmap(photoTwo.toString()))
                    binding.profileImage.setImageBitmap(convertBase64ToBitmap(photoOne.toString()))
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
    }
    override fun onResume() {
        super.onResume()
        getLastLocation(requireContext())
    }
}