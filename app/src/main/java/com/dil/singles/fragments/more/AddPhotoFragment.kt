package com.dil.singles.fragments.more

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.dil.singles.R
import com.dil.singles.databinding.FragmentAddPhotoBinding
import com.dil.singles.fragments.more.viewModel.MoreViewModel
import com.dil.singles.helper.*
import com.dil.singles.helper.Constants.PROFILEPHOTO
import com.dil.singles.helper.Constants.BACKGROUNDPHOTO


class AddPhotoFragment : BaseFragment() {
    private var fragmentAddPhoto: FragmentAddPhotoBinding? = null
    private val binding get() = fragmentAddPhoto!!
    private val moreViewModel: MoreViewModel by viewModels()
    var base64First: String? = null
    var base64Second: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentAddPhoto = FragmentAddPhotoBinding.inflate(layoutInflater, container, false)
        binding.viewModel = moreViewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        clickEvents()
    }

    private fun clickEvents() {
        binding.addPhotoOne.setOnClickListener {
            selectPhotoOne()
        }

        binding.addPhotoTwo.setOnClickListener {
            selectPhotoTwo()
        }

        moreViewModel.onSuccessOccurred.consume(viewLifecycleOwner)
        {
            uploadImageFirst(PROFILEPHOTO, base64First)
            uploadImageSecond(BACKGROUNDPHOTO, base64Second)
            findNavController().navigate(R.id.nav_date_of_birth_fragment)
        }

        binding.addYourFirstPhotosDes.setOnClickListener {
            showPhotoGuidLine(requireContext())
        }
    }

    private fun selectPhotoOne() {
        storagePermission(requireContext())
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 10)
    }

    private fun selectPhotoTwo() {
        storagePermission(requireContext())
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 20)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == 10) {
            Log.d("Boolean of Image: ", "" + moreViewModel.firstPhoto.get())
            val uri = data!!.data
            binding.addPhotoOne.setImageURI(uri)
            moreViewModel.firstPhoto.set(true)
            base64First = convertBase64(Uri.parse(uri.toString()), requireContext())
            moreViewModel.firstPhotoMutable.value = base64First
        }

        if (resultCode == RESULT_OK && requestCode == 20) {
            val uri = data!!.data
            binding.addPhotoTwo.setImageURI(uri)
            moreViewModel.secondPhoto.set(true)
            base64Second = convertBase64(Uri.parse(uri.toString()), requireContext())
            moreViewModel.secondPhotoMutable.value = base64Second
        }

    }

    private fun uploadImageFirst(photoIndex: String, base64: String?) {
        moreViewModel.showLoader.set(true)
        val hashMap = HashMap<String, Any>()
        hashMap[photoIndex] = base64.toString()
        firebaseDatabase.reference.child(Constants.USERS)
            .child(firebaseAuth.uid.toString()).updateChildren(hashMap)
            .addOnSuccessListener {
                moreViewModel.showLoader.set(false)
            }
            .addOnFailureListener {
                moreViewModel.showLoader.set(false)
            }
    }

    private fun uploadImageSecond(photoIndex: String, base64: String?) {
        moreViewModel.showLoader.set(true)
        val hashMap = HashMap<String, Any>()
        hashMap[photoIndex] = base64.toString()
        firebaseDatabase.reference.child(Constants.USERS)
            .child(firebaseAuth.uid.toString()).updateChildren(hashMap)
            .addOnSuccessListener {
                moreViewModel.showLoader.set(false)
            }
            .addOnFailureListener {
                moreViewModel.showLoader.set(false)
            }
    }

    override fun onResume() {
        moreViewModel.showLoader.set(false)
        getLastLocation(requireContext())
        super.onResume()
    }
}