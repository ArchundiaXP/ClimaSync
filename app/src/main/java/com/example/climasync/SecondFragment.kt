package com.example.climasync

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.climasync.databinding.FragmentSecondBinding
import com.example.climasync.utils.FragmentCommunicator

/**

A simple [Fragment] subclass as the second destination in the navigation.*/
class SecondFragment : Fragment() {

    private var _binding: FragmentSecondBinding? = null
    private val binding get() = _binding!!
    private lateinit var communicator: FragmentCommunicator

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        communicator = requireActivity() as MainActivity
        setupView()
        return binding.root

    }

    private fun setupView(){
        binding.flechaLogin.setOnClickListener {
            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
        }
        binding.buttonRegistrar.setOnClickListener {
            findNavController().navigate(R.id.action_SecondFragment_to_personal_information)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}