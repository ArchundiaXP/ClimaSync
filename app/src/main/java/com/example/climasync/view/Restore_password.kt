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
import com.example.climasync.databinding.FragmentRestorePasswordBinding
import com.example.climasync.utils.FragmentCommunicator
import com.example.climasync.viewModel.RestorePasswordViewModel


class Restore_password : Fragment() {

    private var _binding: FragmentRestorePasswordBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<RestorePasswordViewModel>()
    private lateinit var communicator: FragmentCommunicator

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRestorePasswordBinding.inflate(inflater, container, false)
        communicator = requireActivity() as FragmentCommunicator
        setupView()
        setupObservers()
        return binding.root
    }

    private fun setupView() {
        binding.flechaRestorePassword.setOnClickListener {
            findNavController().navigate(R.id.action_restore_password_to_loginFragment)
        }

        binding.resertButton.setOnClickListener {
            if (validateInput()) {
                requestRestorePassword()
            } else {
                Toast.makeText(requireContext(), "Por favor introduce tu correo", Toast.LENGTH_SHORT).show()
            }
        }

        binding.editTextEmail.addTextChangedListener { editable ->
            if (!editable.isNullOrBlank()) {
                binding.textInputLayoutEmail.error = null
            }
        }
    }

    private fun validateInput(): Boolean {
        val email = binding.editTextEmail.text.toString().trim()

        return if (email.isEmpty()) {
            binding.textInputLayoutEmail.error = "Campo requerido"
            false
        } else {
            binding.textInputLayoutEmail.error = null
            true
        }
    }

    private fun setupObservers() {
        viewModel.loaderState.observe(viewLifecycleOwner) { loaderState ->
            communicator.showLoader(loaderState)
        }

        viewModel.msj.observe(viewLifecycleOwner) { msj ->
            if (msj) {
                Toast.makeText(requireContext(), "Correo enviado correctamente", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_restore_password_to_loginFragment)
            } else {
                Toast.makeText(requireContext(), "No se pudo enviar el correo", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun requestRestorePassword() {
        val email = binding.editTextEmail.text.toString().trim()
        viewModel.requestRestorePassword(email)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
