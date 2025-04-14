package com.example.climasync.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.climasync.R
import com.example.climasync.databinding.FragmentFirstBinding
import com.example.climasync.utils.FragmentCommunicator

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class LoguinFragment : Fragment() {

private var _binding: FragmentFirstBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var communicator: FragmentCommunicator

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

      _binding = FragmentFirstBinding.inflate(inflater, container, false)
        communicator = requireActivity() as MainActivity
        setupView()
      return binding.root

    }

    private fun setupView(){
        binding.textRegistrarse.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }
        binding.textRestablecer.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_restore_password)
        }
    }


override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}