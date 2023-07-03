package com.dil.singles.fragments.more

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.dil.singles.R
import com.dil.singles.databinding.FragmentDateofBirthBinding
import com.dil.singles.fragments.more.viewModel.MoreViewModel
import com.dil.singles.helper.*


class DateofBirthFragment : BaseFragment() {
    private var fragmentDateofBirthBinding: FragmentDateofBirthBinding? = null
    private val binding get() = fragmentDateofBirthBinding!!
    private val moreViewModel: MoreViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentDateofBirthBinding =
            FragmentDateofBirthBinding.inflate(layoutInflater, container, false)
        binding.viewModel = moreViewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        clickEvents()
    }

    private fun clickEvents() {
        val day = resources.getStringArray(R.array.day)
        val dayAdapter = ArrayAdapter(requireContext(), R.layout.layout_spinner, day)
        binding.spinnerDay.adapter = dayAdapter
        binding.spinnerDay.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                selectedItemView: View?,
                position: Int,
                id: Long) {
                moreViewModel.day.value = parentView?.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {}
        }

        val month = resources.getStringArray(R.array.month)
        val monthAdapter = ArrayAdapter(requireContext(), R.layout.layout_spinner, month)
        binding.spinnerMonth.adapter = monthAdapter
        binding.spinnerMonth.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                selectedItemView: View?,
                position: Int,
                id: Long) {
                moreViewModel.month.value = position.toString()
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {}
        }

        val year = resources.getStringArray(R.array.year)
        val yearAdapter = ArrayAdapter(requireContext(), R.layout.layout_spinner, year)
        binding.spinnerYear.adapter = yearAdapter
        binding.spinnerYear.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                selectedItemView: View?,
                position: Int,
                id: Long) {
                moreViewModel.year.value = parentView?.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {}
        }


        moreViewModel.onSuccessOccurred.consume(viewLifecycleOwner)
        {
            SharedPrefData(requireContext()).saveDataString(Constants.ONBOARD_DATE_OF_BIRTH, "2")
            findNavController().navigate(R.id.nav_gender_fragment)
        }
    }

    override fun onResume() {
        super.onResume()
        getLastLocation(requireContext())
        moreViewModel.showLoader.set(false)
    }
}