package com.dil.singles.fragments.profile

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.dil.singles.activity.ProfileEditActivity
import com.dil.singles.activity.SettingsActivity
import com.dil.singles.databinding.FragmentProfileBinding
import com.dil.singles.helper.*
import com.dil.singles.helper.Constants.BIO
import com.dil.singles.helper.Constants.DRINKING
import com.dil.singles.helper.Constants.EDUCATION
import com.dil.singles.helper.Constants.HEIGHT
import com.dil.singles.helper.Constants.LANGUAGE
import com.dil.singles.helper.Constants.PASSION
import com.dil.singles.helper.Constants.SMOKING
import com.dil.singles.helper.Constants.Zodiac
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class ProfileFragment : BaseFragment() {

    private var fragmentProfileBinding: FragmentProfileBinding? = null
    private val binding get() = fragmentProfileBinding!!
    private val profileViewModel: ProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentProfileBinding = FragmentProfileBinding.inflate(layoutInflater, container, false)
        binding.viewModel = profileViewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        profileViewModel.getProfileInfo()
        getUserDetails()
        listener()

        binding.logoutBtn.setOnClickListener {
            logout(requireContext())
        }
    }

    private fun getUserDetails() {
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

    private fun listener() {
        binding.bioTxt.setOnClickListener {
            val intent = Intent(context, ProfileEditActivity::class.java)
            intent.putExtra("id", BIO)
            startActivity(intent)
        }

        binding.interestEditLabel.setOnClickListener {
            val intent = Intent(context, ProfileEditActivity::class.java)
            intent.putExtra("id", PASSION)
            startActivity(intent)
        }

        binding.educationEditLabel.setOnClickListener {
            val intent = Intent(context, ProfileEditActivity::class.java)
            intent.putExtra("id", EDUCATION)
            startActivity(intent)
        }

        binding.heightEditLabel.setOnClickListener {
            val intent = Intent(context, ProfileEditActivity::class.java)
            intent.putExtra("id", HEIGHT)
            startActivity(intent)
        }

        binding.smokingEditLabel.setOnClickListener {
            val intent = Intent(context, ProfileEditActivity::class.java)
            intent.putExtra("id", SMOKING)
            startActivity(intent)
        }

        binding.drinkingEditLabel.setOnClickListener {
            val intent = Intent(context, ProfileEditActivity::class.java)
            intent.putExtra("id", DRINKING)
            startActivity(intent)
        }

        binding.zodiacEditLabel.setOnClickListener {
            val intent = Intent(context, ProfileEditActivity::class.java)
            intent.putExtra("id", Zodiac)
            startActivity(intent)
        }

        binding.languageEditLabel.setOnClickListener {
            val intent = Intent(context, ProfileEditActivity::class.java)
            intent.putExtra("id", LANGUAGE)
            startActivity(intent)
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

        binding.settingsImg.setOnClickListener {
            val intent = Intent(context, SettingsActivity::class.java)
            startActivity(intent)
        }

        val items : java.util.ArrayList<Items> = ArrayList()
        val foods : java.util.ArrayList<String> = ArrayList()
        foods.add("Apple")
        foods.add("Banana")
        foods.add("Grapes")
        items.add(Items(foods))


    }

    data class Foods(
        val itemsList: List<Items>
    )

    data class Items(
        val items: List<String>
    )

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == 10) {
            val uri = data!!.data
            binding.profileImage.setImageURI(uri)
            var base64 = convertBase64(Uri.parse(uri.toString()), requireContext())
            changeFirstPhoto(Constants.PROFILEPHOTO, base64)
        }

        if (resultCode == Activity.RESULT_OK && requestCode == 20) {
            val uri = data!!.data
            binding.backgroundProfileImage.setImageURI(uri)
            var base64 = convertBase64(Uri.parse(uri.toString()), requireContext())
            changeFirstPhoto(Constants.BACKGROUNDPHOTO, base64)
        }

    }


}