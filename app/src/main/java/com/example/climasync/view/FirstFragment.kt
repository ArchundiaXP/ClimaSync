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
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null
    private val binding get() = _binding!!
    //private val viewModel by viewModels<FirstFragmentViewModel>() //enlazamos el viewModel

    private lateinit var communicator: FragmentCommunicator



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

      _binding = FragmentFirstBinding.inflate(inflater, container, false)
        communicator = requireActivity() as MainActivity//inicializamos punto de entrada al contrato
        setupView()
      return binding.root

    }

    private fun setupView(){
        binding.textRegistrarse.setOnClickListener {
            //communicator.showLoader(true)//mandamos a ejecutar el contrato(loader)
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