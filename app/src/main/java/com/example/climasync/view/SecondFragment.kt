package com.example.climasync.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.climasync.R
import com.example.climasync.databinding.FragmentSecondBinding
import com.example.climasync.utils.FragmentCommunicator
import com.example.climasync.viewModel.RegistroViewModel

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment() {

    private var _binding: FragmentSecondBinding? = null


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    var isValid: Boolean = false
    private val viewModel by viewModels<RegistroViewModel>()
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

    private fun setupView() {
        binding.flechaLogin.setOnClickListener {
            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
        }

        binding.buttonRegistrar.setOnClickListener {
            viewModel.requestSignUp(
                binding.tietEmail.text.toString(),
                binding.tietPassword.text.toString()
            )
        }


        //valida que se ingresen los datos
        binding.tietEmail.addTextChangedListener {
            if (binding.tietEmail.text.toString().isEmpty()) {
                binding.textInputLayout.error = "Por favor introduce un correo"
                isValid = false
            } else {
                isValid = true
            }
        }

        binding.tietPassword.addTextChangedListener {
            if (binding.tietPassword.text.toString().isEmpty()) {
                binding.tilPasword.error = "Por favor introduce un correo"
                isValid = false
            } else {
                isValid = true
            }
        }

    }

    private fun setupObservers() {
        viewModel.loaderState.observe(viewLifecycleOwner) { loaderState ->
            communicator.showLoader(loaderState)
        }

        viewModel.validRegister.observe(viewLifecycleOwner) { validRegister ->
            if (validRegister) {
                findNavController().navigate(R.id.action_SecondFragment_to_personal_information)
            }
        }
    }
}