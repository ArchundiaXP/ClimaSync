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
    var isValid: Boolean = false
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
        _binding = FragmentRestorePasswordBinding.inflate(inflater, container, false)
        communicator = requireActivity() as MainActivity
        setupView()
        setupObservers()
        requestRestorePassword()
        return (binding.root)
    }

    private fun setupView() {
        binding.flechaRestorePassword.setOnClickListener {
            findNavController().navigate(R.id.action_restore_password_to_FirstFragment)
        }
        binding.resertButton.setOnClickListener {
            findNavController().navigate(R.id.action_restore_password_to_FirstFragment)
        }

        binding.resertButton.setOnClickListener {
            if(isValid){
                requestRestorePassword()

                Toast.makeText(activity, "Datos válidos", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(activity, "Datos inválidos", Toast.LENGTH_SHORT).show()
            }
        }

        binding.editTextEmail.addTextChangedListener{

            if(binding.editTextEmail.text.toString().isEmpty()){
                binding.textViewInstruction.error= "Campo requerido"
                isValid = false
            }else{
                isValid = true
            }
        }
    }

    private fun setupObservers() {
        //accedemos a los publisher y con el observer definimos quien es el encargado del ciclo de vida
        viewModel.loaderState.observe(viewLifecycleOwner) { loaderState ->
            communicator.showLoader(loaderState)//llamamos al loader para mostrarlo

            viewModel.msj.observe(viewLifecycleOwner) { msj ->
                if (msj) {
                    Toast.makeText(activity, "Datos guardados", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_personal_information_to_layoutPermission)
                } else {
                    Toast.makeText(activity, "Error", Toast.LENGTH_SHORT).show()
                }


            }

        }

    }


    private fun requestRestorePassword(){
        viewModel.requestRestorePassword(binding.editTextEmail.text.toString())

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}