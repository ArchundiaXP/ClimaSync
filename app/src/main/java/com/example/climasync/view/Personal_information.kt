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
import com.example.climasync.databinding.FragmentPersonalInformationBinding
import com.example.climasync.utils.FragmentCommunicator
import com.example.climasync.viewModel.PersonalInformationViewModel


/**

A simple [Fragment] subclass.
Use the [personal_information.newInstance] factory method to
create an instance of this fragment.*/
class personal_information : Fragment() {
    // TODO: Rename and change types of parameters

    private var _binding: FragmentPersonalInformationBinding? = null
    private val binding get() = _binding!!
    var isValid:Boolean = false
    private lateinit var communicator: FragmentCommunicator
    private val viewModel by viewModels<PersonalInformationViewModel>()

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
        setupObservers()
        return binding.root
    }

    private fun setupView() {
        binding.personalButton.setOnClickListener {
            findNavController().navigate(R.id.action_personal_information_to_layoutPermission)
        }

        binding.personalButton.setOnClickListener {
            //validar que se ingresen datos
            if (isValid) {
                requestPersonalInformation()
            } else {
                Toast.makeText(activity, "Datos Invalidos ", Toast.LENGTH_SHORT).show()

            }
        }

        binding.editTextApellido.addTextChangedListener {
            if (binding.editTextApellido.text.toString().isEmpty()) {
                binding.textInputLayoutApellido.error = "Campo requerido"
                isValid = false
            } else {
                isValid = true
            }
        }
        binding.editTextNombre.addTextChangedListener {
            if (binding.editTextNombre.text.toString().isEmpty()) {
                binding.textInputLayoutNombre.error = "Campo requerido"
                isValid = false
            } else {
                isValid = true
            }
        }
        binding.textInputEditTextUsuario.addTextChangedListener {
            if (binding.textInputEditTextUsuario.text.toString().isEmpty()) {
                binding.textInputLayoutUsuario.error = "Campo requerido"
                isValid = false
            } else {
                isValid = true
            }
        }
        binding.textInputEditTextFechaNacimiento.addTextChangedListener {
            if (binding.textInputEditTextFechaNacimiento.text.toString().isEmpty()) {
                binding.textInputLayoutFechaNacimiento.error = "Campo requerido"
                isValid = false
            } else {
                isValid = true
            }
        }

        binding.textInputEditTextCodigoReferido.addTextChangedListener {
            if (binding.textInputEditTextCodigoReferido.text.toString().isEmpty()) {
                binding.textInputLayoutCodigoReferido.error = "Campo requerido"
                isValid = false
            } else {
                isValid = true
            }

        }
    }

    private fun setupObservers(){
        //accedemos a los publisher y con el observer definimos quien es el encargado del ciclo de vida
        viewModel.loaderState.observe(viewLifecycleOwner){ loaderState ->
            communicator.showLoader(loaderState)//llamamos al loader para mostrarlo
        }

        viewModel.msj.observe(viewLifecycleOwner){ msj ->
            if(msj){
                Toast.makeText(activity, "Datos guardados", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_personal_information_to_layoutPermission)
            }else{
                Toast.makeText(activity, "Error", Toast.LENGTH_SHORT).show()
            }

        }

    }

    private fun requestPersonalInformation() {
        viewModel.requestPersonalInformation(binding.editTextNombre.text.toString(),
            binding.editTextApellido.text.toString(),
            binding.textInputEditTextUsuario.text.toString(),
            binding.textInputEditTextFechaNacimiento.text.toString(),
            binding.textInputEditTextCodigoReferido.text.toString())
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


