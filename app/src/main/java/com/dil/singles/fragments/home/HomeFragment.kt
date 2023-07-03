package com.dil.singles.fragments.home

import android.content.ContentValues.TAG
import android.media.MediaRecorder
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DiffUtil
import com.dil.singles.R
import com.dil.singles.databinding.FragmentHomeBinding
import com.dil.singles.helper.*
import com.dil.singles.helper.Constants.COUNTRY
import com.dil.singles.helper.Constants.FIRSTNAME
import com.dil.singles.helper.Constants.GLOBAL
import com.dil.singles.helper.Constants.LASTNAME
import com.dil.singles.helper.Constants.LOCALITY
import com.dil.singles.helper.Constants.PROFILEPHOTO
import com.dil.singles.helper.Constants.SHOWUSERLIST
import com.dil.singles.helper.Constants.STATE
import com.dil.singles.helper.Constants.TIME
import com.dil.singles.helper.Constants.UID
import com.dil.singles.helper.Constants.VERSION
import com.dil.singles.helper.Constants.WHOMTODATE
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.yuyakaido.android.cardstackview.*
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList

class HomeFragment : BaseFragment() {

    private var fragmentHomeBinding: FragmentHomeBinding? = null
    private val binding get() = fragmentHomeBinding!!
    private val homeViewModel: HomeViewModel by viewModels()
    lateinit var adapter: HomeLandingAdapter
    lateinit var manager: CardStackLayoutManager
    var position = 0
    var firstName: String? = null
    var profileImage: String? = null
    var lastName: String? = null
    var userUid: String? = null
    var backPressedTime: Long = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentHomeBinding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        binding.viewModel = homeViewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getProfileInfo()
        cardSwipe()
        listener()

        requireActivity()
            .onBackPressedDispatcher
            .addCallback(requireActivity(), object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (backPressedTime + 3000 > System.currentTimeMillis()) {
                        activity?.finishAffinity()
                    } else {
                        Toast.makeText(context, "Press back again to leave the app.", Toast.LENGTH_LONG).show()
                    }
                    backPressedTime = System.currentTimeMillis()
                }
            })
    }

    override fun onResume() {
        super.onResume()
        getLastLocation(requireContext())
    }

    private fun listener() {
        homeViewModel.globalOccurred.consume(viewLifecycleOwner)
        {
            cardSwipe()
        }
    }

    private fun getProfileInfo() {
        firebaseDatabase.reference.child(Constants.USERS).child(firebaseAuth.uid.toString())
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    firstName = snapshot.child(FIRSTNAME).getValue(String::class.java)
                    lastName = snapshot.child(LASTNAME).getValue(String::class.java)
                    profileImage = snapshot.child(PROFILEPHOTO).getValue(String::class.java)
                    userUid = snapshot.child(UID).getValue(String::class.java)
                    val country = snapshot.child(COUNTRY).getValue(String::class.java)
                    val state = snapshot.child(STATE).getValue(String::class.java)
                    val interestedIn = snapshot.child(WHOMTODATE).getValue(String::class.java)
                    binding.profileImage.setImageBitmap(convertBase64ToBitmap(profileImage.toString()))
                    binding.txtFirstName.text = firstName
                    SharedPrefData(requireContext()).saveDataString(STATE, state.toString())
                    SharedPrefData(requireContext()).saveDataString(COUNTRY, country.toString())
                    SharedPrefData(requireContext()).saveDataString(WHOMTODATE, interestedIn.toString())
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
    }

    private fun cardSwipe() {
        manager = CardStackLayoutManager(context, object : CardStackListener {
            override fun onCardDragging(direction: Direction, ratio: Float) {
                Log.d(TAG, "onCardDragging: d=" + direction.name + " ratio=" + ratio);
            }

            override fun onCardSwiped(direction: Direction) {
                if (direction === Direction.Right) {
                    homeViewModel.likeUser(requireContext())
                    like(position)
                    position++
                }
                if (direction === Direction.Top) {
                }
                if (direction === Direction.Left) {
                    homeViewModel.disLikeUser(requireContext())
                    position++
                }
                if (direction === Direction.Bottom) {
                }

                if (manager.topPosition == adapter.itemCount) {
                    paginate()
                }
            }

            override fun onCardRewound() {
                Log.d(TAG, "onCardRewound: " + manager.topPosition)
            }

            override fun onCardCanceled() {
                Log.d(TAG, "onCardCanceled: " + manager.topPosition)
            }

            override fun onCardAppeared(view: View, position: Int) {
                Log.d(TAG, "onCardAppeared: " + manager.topPosition)
            }

            override fun onCardDisappeared(view: View, position: Int) {
                Log.d(TAG, "onCardDisappeared: " + manager.topPosition)
            }
        })

        manager.setStackFrom(StackFrom.None)
        manager.setVisibleCount(3)
        manager.setTranslationInterval(8.0f)
        manager.setScaleInterval(0.95f)
        manager.setSwipeThreshold(0.3f)
        manager.setMaxDegree(20.0f)
        manager.setDirections(Direction.FREEDOM)
        manager.setCanScrollHorizontal(true)
        manager.setSwipeableMethod(SwipeableMethod.Manual)
        manager.setOverlayInterpolator(LinearInterpolator())
        when {
            SharedPrefData(requireContext()).getDataString(SHOWUSERLIST) == GLOBAL -> {
                adapter = HomeLandingAdapter(homeViewModel.showGlobalList(requireContext()), requireContext())
            }
            SharedPrefData(requireContext()).getDataString(SHOWUSERLIST) == LOCALITY -> {
                adapter = HomeLandingAdapter(
                    homeViewModel.showCurrentAreaList(requireContext()),
                    requireContext()
                )
            }
            SharedPrefData(requireContext()).getDataString(SHOWUSERLIST).isEmpty() -> {
                adapter = HomeLandingAdapter(
                    homeViewModel.showCurrentAreaList(requireContext()),
                    requireContext()
                )
            }
        }
        binding.cardStackView.layoutManager = manager
        binding.cardStackView.adapter = adapter
        binding.cardStackView.itemAnimator = DefaultItemAnimator()

    }

    private fun paginate() {
        val oldList: ArrayList<HomeLandingModel> = adapter.getItems()
        val newList: ArrayList<HomeLandingModel> = ArrayList()
        when {
            SharedPrefData(requireContext()).getDataString(SHOWUSERLIST) == GLOBAL -> {
                ArrayList(homeViewModel.showGlobalList(requireContext()))
            }
            SharedPrefData(requireContext()).getDataString(SHOWUSERLIST) == LOCALITY -> {
                ArrayList(homeViewModel.showCurrentAreaList(requireContext()))
            }
            SharedPrefData(requireContext()).getDataString(SHOWUSERLIST).isEmpty() -> {
                ArrayList(homeViewModel.showCurrentAreaList(requireContext()))
            }
        }
        val callback = CardStackCallback(oldList, newList)
        val result = DiffUtil.calculateDiff(callback)
//        adapter.notifyDataSetChanged()
        adapter.setItems(newList)
        result.dispatchUpdatesTo(adapter)
    }

    private fun like(position: Int) {
        val date = Date()
        val old: ArrayList<HomeLandingModel> = adapter.getItems()
        val message = HashMap<String, Any>()
        message[FIRSTNAME] = firstName.toString()
        message[LASTNAME] = lastName.toString()
        message[PROFILEPHOTO] = profileImage.toString()
        message[UID] = userUid.toString()
        message[TIME] = date.time
        FirebaseDatabase.getInstance().reference
            .child("userLikes")
            .child(old[position].uid.toString())
            .child(FirebaseAuth.getInstance().uid.toString())
            .setValue(message).addOnSuccessListener {}

    }

}
