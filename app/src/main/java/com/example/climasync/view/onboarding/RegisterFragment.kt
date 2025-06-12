package com.example.climasync.view.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.climasync.R
import com.example.climasync.databinding.FragmentRegisterBinding
import com.example.climasync.utils.FragmentCommunicator
import androidx.core.widget.addTextChangedListener
import com.example.climasync.viewModel.RegistroViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * A simple [androidx.fragment.app.Fragment] subclass as the second destination in the navigation.
 */
@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<RegistroViewModel>()
    private lateinit var communicator: FragmentCommunicator

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        communicator = requireActivity() as Onboarding
        setupView()
        setupObservers()
        return binding.root
    }

    private fun setupView() {
        binding.flechaLogin.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }

        binding.buttonRegistrar.setOnClickListener {
            if (validateInputs()) {
                val email = binding.tietEmail.text.toString().trim()
                val password = binding.tietPassword.text.toString()
                viewModel.requestSignUp(email, password)
            } else {
                Toast.makeText(requireContext(), "Revisa los campos", Toast.LENGTH_SHORT).show()
            }
        }

        binding.tietEmail.addTextChangedListener {
            if (!it.isNullOrBlank()) {
                binding.textInputLayout.error = null
            }
        }

        binding.tietPassword.addTextChangedListener {
            if (!it.isNullOrBlank()) {
                binding.tilPasword.error = null
            }
        }
    }

    private fun validateInputs(): Boolean {
        var isValid = true

        val email = binding.tietEmail.text.toString().trim()
        val password = binding.tietPassword.text.toString()

        if (email.isEmpty()) {
            binding.textInputLayout.error = "Por favor introduce un correo"
            isValid = false
        } else {
            binding.textInputLayout.error = null
        }

        if (password.isEmpty()) {
            binding.tilPasword.error = "Por favor introduce una contraseña"
            isValid = false
        } else if (password.length < 6) {
            binding.tilPasword.error = "La contraseña debe tener al menos 6 caracteres"
            isValid = false
        } else {
            binding.tilPasword.error = null
        }

        return isValid
    }



    private fun setupObservers() {
        viewModel.loaderState.observe(viewLifecycleOwner) { loaderState ->
            communicator.showLoader(loaderState)
        }

        viewModel.validRegister.observe(viewLifecycleOwner) { validRegister ->
            if (validRegister) {
                findNavController().navigate(R.id.action_registerFragment_to_personal_information)
            }
        }

        // Nuevo: mostrar mensajes de error personalizados
        viewModel.errorMessage.observe(viewLifecycleOwner) { message ->
            Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
