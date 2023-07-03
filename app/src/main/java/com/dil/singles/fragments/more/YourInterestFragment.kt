package com.dil.singles.fragments.more

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.dil.singles.R
import com.dil.singles.databinding.FragmentYourInterestBinding
import com.dil.singles.fragments.more.viewModel.MoreViewModel
import com.dil.singles.helper.BaseFragment
import com.dil.singles.helper.Constants
import com.dil.singles.helper.SharedPrefData

class YourInterestFragment : BaseFragment() {

    private var fragmentYourInterest: FragmentYourInterestBinding? = null
    private val binding get() = fragmentYourInterest!!
    private val moreViewModel: MoreViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentYourInterest =
            FragmentYourInterestBinding.inflate(layoutInflater, container, false)
        binding.viewModel = moreViewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listener()
    }

    private fun listener() {

        moreViewModel.onSuccessOccurred.consume(viewLifecycleOwner)
        {
            SharedPrefData(requireContext()).saveDataString(Constants.ONBOARD_PASSION, "12")
            findNavController().navigate(R.id.nav_language_fragment)
        }

        binding.photographyTxt.setOnClickListener {
            isPassionSelected1 = if (isPassionSelected1) {
                binding.photographyTxt.setBackgroundResource(R.drawable.button_selected_design)
                passionateString1 = binding.photographyTxt.text.toString() + ","
                moreViewModel.stringPassion.value =
                    passionateString1 + passionateString2 + passionateString3 + passionateString4 +
                            passionateString5 + passionateString6 + passionateString7 + passionateString8 + passionateString9 +
                            passionateString10 + passionateString11 + passionateString12 + passionateString13 +
                            passionateString14 + passionateString15 + passionateString16 + passionateString17
                false
            } else {
                binding.photographyTxt.setBackgroundResource(R.drawable.button_selector_color_2)
                passionateString1 = ""
                moreViewModel.stringPassion.value =
                    passionateString1 + passionateString2 + passionateString3 + passionateString4 +
                            passionateString5 + passionateString6 + passionateString7 + passionateString8 + passionateString9 +
                            passionateString10 + passionateString11 + passionateString12 + passionateString13 +
                            passionateString14 + passionateString15 + passionateString16 + passionateString17
                true
            }
        }

        binding.singingTxt.setOnClickListener {
            isPassionSelected2 = if (isPassionSelected2) {
                binding.singingTxt.setBackgroundResource(R.drawable.button_selected_design)
                passionateString2 = binding.singingTxt.text.toString() + ","
                moreViewModel.stringPassion.value =
                    passionateString1 + passionateString2 + passionateString3 + passionateString4 +
                            passionateString5 + passionateString6 + passionateString7 + passionateString8 + passionateString9 +
                            passionateString10 + passionateString11 + passionateString12 + passionateString13 +
                            passionateString14 + passionateString15 + passionateString16 + passionateString17
                false
            } else {
                binding.singingTxt.setBackgroundResource(R.drawable.button_selector_color_2)
                passionateString2 = ""
                moreViewModel.stringPassion.value =
                    passionateString1 + passionateString2 + passionateString3 + passionateString4 +
                            passionateString5 + passionateString6 + passionateString7 + passionateString8 + passionateString9 +
                            passionateString10 + passionateString11 + passionateString12 + passionateString13 +
                            passionateString14 + passionateString15 + passionateString16 + passionateString17
                true
            }
        }

        binding.dancingTxt.setOnClickListener {
            isPassionSelected3 = if (isPassionSelected3) {
                binding.dancingTxt.setBackgroundResource(R.drawable.button_selected_design)
                passionateString3 = binding.dancingTxt.text.toString() + ","
                moreViewModel.stringPassion.value =
                    passionateString1 + passionateString2 + passionateString3 + passionateString4 +
                            passionateString5 + passionateString6 + passionateString7 + passionateString8 + passionateString9 +
                            passionateString10 + passionateString11 + passionateString12 + passionateString13 +
                            passionateString14 + passionateString15 + passionateString16 + passionateString17
                false
            } else {
                binding.dancingTxt.setBackgroundResource(R.drawable.button_selector_color_2)
                passionateString3 = ""
                moreViewModel.stringPassion.value =
                    passionateString1 + passionateString2 + passionateString3 + passionateString4 +
                            passionateString5 + passionateString6 + passionateString7 + passionateString8 + passionateString9 +
                            passionateString10 + passionateString11 + passionateString12 + passionateString13 +
                            passionateString14 + passionateString15 + passionateString16 + passionateString17
                true
            }
        }

        binding.writingTxt.setOnClickListener {
            isPassionSelected4 = if (isPassionSelected4) {
                binding.writingTxt.setBackgroundResource(R.drawable.button_selected_design)
                passionateString4 = binding.writingTxt.text.toString() + ","
                moreViewModel.stringPassion.value =
                    passionateString1 + passionateString2 + passionateString3 + passionateString4 +
                            passionateString5 + passionateString6 + passionateString7 + passionateString8 + passionateString9 +
                            passionateString10 + passionateString11 + passionateString12 + passionateString13 +
                            passionateString14 + passionateString15 + passionateString16 + passionateString17
                false
            } else {
                binding.writingTxt.setBackgroundResource(R.drawable.button_selector_color_2)
                passionateString4 = ""
                moreViewModel.stringPassion.value =
                    passionateString1 + passionateString2 + passionateString3 + passionateString4 +
                            passionateString5 + passionateString6 + passionateString7 + passionateString8 + passionateString9 +
                            passionateString10 + passionateString11 + passionateString12 + passionateString13 +
                            passionateString14 + passionateString15 + passionateString16 + passionateString17
                true
            }
        }

        binding.yogaTxt.setOnClickListener {
            isPassionSelected5 = if (isPassionSelected5) {
                binding.yogaTxt.setBackgroundResource(R.drawable.button_selected_design)
                passionateString5 = binding.yogaTxt.text.toString() + ","
                moreViewModel.stringPassion.value =
                    passionateString1 + passionateString2 + passionateString3 + passionateString4 +
                            passionateString5 + passionateString6 + passionateString7 + passionateString8 + passionateString9 +
                            passionateString10 + passionateString11 + passionateString12 + passionateString13 +
                            passionateString14 + passionateString15 + passionateString16 + passionateString17
                false
            } else {
                binding.yogaTxt.setBackgroundResource(R.drawable.button_selector_color_2)
                passionateString5 = ""
                moreViewModel.stringPassion.value =
                    passionateString1 + passionateString2 + passionateString3 + passionateString4 +
                            passionateString5 + passionateString6 + passionateString7 + passionateString8 + passionateString9 +
                            passionateString10 + passionateString11 + passionateString12 + passionateString13 +
                            passionateString14 + passionateString15 + passionateString16 + passionateString17
                true
            }
        }

        binding.runningTxt.setOnClickListener {
            isPassionSelected6 = if (isPassionSelected6) {
                binding.runningTxt.setBackgroundResource(R.drawable.button_selected_design)
                passionateString6 = binding.runningTxt.text.toString() + ","
                moreViewModel.stringPassion.value =
                    passionateString1 + passionateString2 + passionateString3 + passionateString4 +
                            passionateString5 + passionateString6 + passionateString7 + passionateString8 + passionateString9 +
                            passionateString10 + passionateString11 + passionateString12 + passionateString13 +
                            passionateString14 + passionateString15 + passionateString16 + passionateString17
                false
            } else {
                binding.runningTxt.setBackgroundResource(R.drawable.button_selector_color_2)
                passionateString6 = ""
                moreViewModel.stringPassion.value =
                    passionateString1 + passionateString2 + passionateString3 + passionateString4 +
                            passionateString5 + passionateString6 + passionateString7 + passionateString8 + passionateString9 +
                            passionateString10 + passionateString11 + passionateString12 + passionateString13 +
                            passionateString14 + passionateString15 + passionateString16 + passionateString17
                true
            }
        }

        binding.cricketTxt.setOnClickListener {
            isPassionSelected7 = if (isPassionSelected7) {
                binding.cricketTxt.setBackgroundResource(R.drawable.button_selected_design)
                passionateString7 = binding.cricketTxt.text.toString() + ","
                moreViewModel.stringPassion.value =
                    passionateString1 + passionateString2 + passionateString3 + passionateString4 +
                            passionateString5 + passionateString6 + passionateString7 + passionateString8 + passionateString9 +
                            passionateString10 + passionateString11 + passionateString12 + passionateString13 +
                            passionateString14 + passionateString15 + passionateString16 + passionateString17
                false
            } else {
                binding.cricketTxt.setBackgroundResource(R.drawable.button_selector_color_2)
                passionateString7 = ""
                moreViewModel.stringPassion.value =
                    passionateString1 + passionateString2 + passionateString3 + passionateString4 +
                            passionateString5 + passionateString6 + passionateString7 + passionateString8 + passionateString9 +
                            passionateString10 + passionateString11 + passionateString12 + passionateString13 +
                            passionateString14 + passionateString15 + passionateString16 + passionateString17
                true
            }
        }

        binding.gymTxt.setOnClickListener {
            isPassionSelected9 = if (isPassionSelected9) {
                binding.gymTxt.setBackgroundResource(R.drawable.button_selected_design)
                passionateString8 = binding.gymTxt.text.toString() + ","
                moreViewModel.stringPassion.value =
                    passionateString1 + passionateString2 + passionateString3 + passionateString4 +
                            passionateString5 + passionateString6 + passionateString7 + passionateString8 + passionateString9 +
                            passionateString10 + passionateString11 + passionateString12 + passionateString13 +
                            passionateString14 + passionateString15 + passionateString16 + passionateString17
                false
            } else {
                binding.gymTxt.setBackgroundResource(R.drawable.button_selector_color_2)
                passionateString8 = ""
                moreViewModel.stringPassion.value =
                    passionateString1 + passionateString2 + passionateString3 + passionateString4 +
                            passionateString5 + passionateString6 + passionateString7 + passionateString8 + passionateString9 +
                            passionateString10 + passionateString11 + passionateString12 + passionateString13 +
                            passionateString14 + passionateString15 + passionateString16 + passionateString17
                true
            }
        }

        binding.footballTxt.setOnClickListener {
            isPassionSelected10 = if (isPassionSelected10) {
                binding.footballTxt.setBackgroundResource(R.drawable.button_selected_design)
                passionateString10 = binding.footballTxt.text.toString() + ","
                moreViewModel.stringPassion.value =
                    passionateString1 + passionateString2 + passionateString3 + passionateString4 +
                            passionateString5 + passionateString6 + passionateString7 + passionateString8 + passionateString9 +
                            passionateString10 + passionateString11 + passionateString12 + passionateString13 +
                            passionateString14 + passionateString15 + passionateString16 + passionateString17
                false
            } else {
                binding.footballTxt.setBackgroundResource(R.drawable.button_selector_color_2)
                passionateString10 = ""
                moreViewModel.stringPassion.value =
                    passionateString1 + passionateString2 + passionateString3 + passionateString4 +
                            passionateString5 + passionateString6 + passionateString7 + passionateString8 + passionateString9 +
                            passionateString10 + passionateString11 + passionateString12 + passionateString13 +
                            passionateString14 + passionateString15 + passionateString16 + passionateString17
                true
            }
        }

        binding.tennisTxt.setOnClickListener {
            isPassionSelected11 = if (isPassionSelected11) {
                binding.tennisTxt.setBackgroundResource(R.drawable.button_selected_design)
                passionateString11 = binding.tennisTxt.text.toString() + ","
                moreViewModel.stringPassion.value =
                    passionateString1 + passionateString2 + passionateString3 + passionateString4 +
                            passionateString5 + passionateString6 + passionateString7 + passionateString8 + passionateString9 +
                            passionateString10 + passionateString11 + passionateString12 + passionateString13 +
                            passionateString14 + passionateString15 + passionateString16 + passionateString17
                false
            } else {
                binding.tennisTxt.setBackgroundResource(R.drawable.button_selector_color_2)
                passionateString11 = ""
                moreViewModel.stringPassion.value =
                    passionateString1 + passionateString2 + passionateString3 + passionateString4 +
                            passionateString5 + passionateString6 + passionateString7 + passionateString8 + passionateString9 +
                            passionateString10 + passionateString11 + passionateString12 + passionateString13 +
                            passionateString14 + passionateString15 + passionateString16 + passionateString17
                true
            }
        }

        binding.gamingTxt.setOnClickListener {
            isPassionSelected12 = if (isPassionSelected12) {
                binding.gamingTxt.setBackgroundResource(R.drawable.button_selected_design)
                passionateString12 = binding.gamingTxt.text.toString() + ","
                moreViewModel.stringPassion.value =
                    passionateString1 + passionateString2 + passionateString3 + passionateString4 +
                            passionateString5 + passionateString6 + passionateString7 + passionateString8 + passionateString9 +
                            passionateString10 + passionateString11 + passionateString12 + passionateString13 +
                            passionateString14 + passionateString15 + passionateString16 + passionateString17
                false
            } else {
                binding.gamingTxt.setBackgroundResource(R.drawable.button_selector_color_2)
                passionateString12 = ""
                moreViewModel.stringPassion.value =
                    passionateString1 + passionateString2 + passionateString3 + passionateString4 +
                            passionateString5 + passionateString6 + passionateString7 + passionateString8 + passionateString9 +
                            passionateString10 + passionateString11 + passionateString12 + passionateString13 +
                            passionateString14 + passionateString15 + passionateString16 + passionateString17
                true
            }
        }

        binding.travellingTxt.setOnClickListener {
            isPassionSelected13 = if (isPassionSelected13) {
                binding.travellingTxt.setBackgroundResource(R.drawable.button_selected_design)
                passionateString13 = binding.travellingTxt.text.toString() + ","
                moreViewModel.stringPassion.value =
                    passionateString1 + passionateString2 + passionateString3 + passionateString4 +
                            passionateString5 + passionateString6 + passionateString7 + passionateString8 + passionateString9 +
                            passionateString10 + passionateString11 + passionateString12 + passionateString13 +
                            passionateString14 + passionateString15 + passionateString16 + passionateString17
                false
            } else {
                binding.travellingTxt.setBackgroundResource(R.drawable.button_selector_color_2)
                passionateString13 = ""
                moreViewModel.stringPassion.value =
                    passionateString1 + passionateString2 + passionateString3 + passionateString4 +
                            passionateString5 + passionateString6 + passionateString7 + passionateString8 + passionateString9 +
                            passionateString10 + passionateString11 + passionateString12 + passionateString13 +
                            passionateString14 + passionateString15 + passionateString16 + passionateString17
                true
            }
        }

        binding.watchingMoviesTxt.setOnClickListener {
            isPassionSelected14 = if (isPassionSelected14) {
                binding.watchingMoviesTxt.setBackgroundResource(R.drawable.button_selected_design)
                passionateString14 = binding.watchingMoviesTxt.text.toString() + ","
                moreViewModel.stringPassion.value =
                    passionateString1 + passionateString2 + passionateString3 + passionateString4 +
                            passionateString5 + passionateString6 + passionateString7 + passionateString8 + passionateString9 +
                            passionateString10 + passionateString11 + passionateString12 + passionateString13 +
                            passionateString14 + passionateString15 + passionateString16 + passionateString17
                false
            } else {
                binding.watchingMoviesTxt.setBackgroundResource(R.drawable.button_selector_color_2)
                passionateString14 = ""
                moreViewModel.stringPassion.value =
                    passionateString1 + passionateString2 + passionateString3 + passionateString4 +
                            passionateString5 + passionateString6 + passionateString7 + passionateString8 + passionateString9 +
                            passionateString10 + passionateString11 + passionateString12 + passionateString13 +
                            passionateString14 + passionateString15 + passionateString16 + passionateString17
                true
            }
        }

        binding.campingTxt.setOnClickListener {
            isPassionSelected15 = if (isPassionSelected15) {
                binding.campingTxt.setBackgroundResource(R.drawable.button_selected_design)
                passionateString15 = binding.campingTxt.text.toString() + ","
                moreViewModel.stringPassion.value =
                    passionateString1 + passionateString2 + passionateString3 + passionateString4 +
                            passionateString5 + passionateString6 + passionateString7 + passionateString8 + passionateString9 +
                            passionateString10 + passionateString11 + passionateString12 + passionateString13 +
                            passionateString14 + passionateString15 + passionateString16 + passionateString17
                false
            } else {
                binding.campingTxt.setBackgroundResource(R.drawable.button_selector_color_2)
                passionateString15 = ""
                moreViewModel.stringPassion.value =
                    passionateString1 + passionateString2 + passionateString3 + passionateString4 +
                            passionateString5 + passionateString6 + passionateString7 + passionateString8 + passionateString9 +
                            passionateString10 + passionateString11 + passionateString12 + passionateString13 +
                            passionateString14 + passionateString15 + passionateString16 + passionateString17
                true
            }
        }

        binding.cookingTxt.setOnClickListener {
            isPassionSelected16 = if (isPassionSelected16) {
                binding.cookingTxt.setBackgroundResource(R.drawable.button_selected_design)
                passionateString16 = binding.cookingTxt.text.toString() + ","
                moreViewModel.stringPassion.value =
                    passionateString1 + passionateString2 + passionateString3 + passionateString4 +
                            passionateString5 + passionateString6 + passionateString7 + passionateString8 + passionateString9 +
                            passionateString10 + passionateString11 + passionateString12 + passionateString13 +
                            passionateString14 + passionateString15 + passionateString16 + passionateString17
                false
            } else {
                binding.cookingTxt.setBackgroundResource(R.drawable.button_selector_color_2)
                passionateString16 = ""
                moreViewModel.stringPassion.value =
                    passionateString1 + passionateString2 + passionateString3 + passionateString4 +
                            passionateString5 + passionateString6 + passionateString7 + passionateString8 + passionateString9 +
                            passionateString10 + passionateString11 + passionateString12 + passionateString13 +
                            passionateString14 + passionateString15 + passionateString16 + passionateString17
                true
            }
        }

        binding.swimmingTxt.setOnClickListener {
            isPassionSelected17 = if (isPassionSelected17) {
                binding.swimmingTxt.setBackgroundResource(R.drawable.button_selected_design)
                passionateString17 = binding.swimmingTxt.text.toString() + ","
                moreViewModel.stringPassion.value =
                    passionateString1 + passionateString2 + passionateString3 + passionateString4 +
                            passionateString5 + passionateString6 + passionateString7 + passionateString8 + passionateString9 +
                            passionateString10 + passionateString11 + passionateString12 + passionateString13 +
                            passionateString14 + passionateString15 + passionateString16 + passionateString17
                false
            } else {
                binding.swimmingTxt.setBackgroundResource(R.drawable.button_selector_color_2)
                passionateString17 = ""
                moreViewModel.stringPassion.value =
                    passionateString1 + passionateString2 + passionateString3 + passionateString4 +
                            passionateString5 + passionateString6 + passionateString7 + passionateString8 + passionateString9 +
                            passionateString10 + passionateString11 + passionateString12 + passionateString13 +
                            passionateString14 + passionateString15 + passionateString16 + passionateString17
                true
            }
        }

        binding.cyclingTxt.setOnClickListener {
            isPassionSelected9 = if (isPassionSelected9) {
                binding.cyclingTxt.setBackgroundResource(R.drawable.button_selected_design)
                passionateString9 = binding.cyclingTxt.text.toString() + ","
                moreViewModel.stringPassion.value =
                    passionateString1 + passionateString2 + passionateString3 + passionateString4 +
                            passionateString5 + passionateString6 + passionateString7 + passionateString8 + passionateString9 +
                            passionateString10 + passionateString11 + passionateString12 + passionateString13 +
                            passionateString14 + passionateString15 + passionateString16 + passionateString17
                false
            } else {
                binding.cyclingTxt.setBackgroundResource(R.drawable.button_selector_color_2)
                passionateString9 = ""
                moreViewModel.stringPassion.value =
                    passionateString1 + passionateString2 + passionateString3 + passionateString4 +
                            passionateString5 + passionateString6 + passionateString7 + passionateString8 + passionateString9 +
                            passionateString10 + passionateString11 + passionateString12 + passionateString13 +
                            passionateString14 + passionateString15 + passionateString16 + passionateString17
                true
            }
        }
    }


    override fun onResume() {
        super.onResume()
        moreViewModel.showLoader.set(false)
        getLastLocation(requireContext())
    }


}