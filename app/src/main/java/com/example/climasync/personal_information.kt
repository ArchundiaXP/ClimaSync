package com.example.climasync

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.climasync.databinding.FragmentPersonalInformationBinding
import com.example.climasync.utils.FragmentCommunicator



/**

A simple [Fragment] subclass.
Use the [personal_information.newInstance] factory method to
create an instance of this fragment.*/
class personal_information : Fragment() {
    // TODO: Rename and change types of parameters

    private var _binding: FragmentPersonalInformationBinding? = null
    private val binding get() = _binding!!
    private lateinit var communicator: FragmentCommunicator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPersonalInformationBinding.inflate(inflater, container, false)
        communicator = requireActivity() as MainActivity
        setupView()
        return binding.root
    }

    private fun setupView() {
        binding.personalButton.setOnClickListener{
            findNavController().navigate(R.id.action_personal_information_to_layoutPermission)
        }

    }

}