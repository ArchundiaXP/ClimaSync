package com.example.climasync.view

import android.content.Intent
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
import com.example.climasync.databinding.FragmentFirstBinding
import com.example.climasync.utils.FragmentCommunicator
import com.example.climasync.viewModel.FirstFragmentViewModel

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<FirstFragmentViewModel>() //enlazamos el viewModel
    var isValid:Boolean = false
    private lateinit var communicator: FragmentCommunicator



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

      _binding = FragmentFirstBinding.inflate(inflater, container, false)
        communicator = requireActivity() as MainActivity//inicializamos punto de entrada al contrato
        setupView()
        setupObservers()
      return binding.root

    }

    private fun setupView(){
        binding.textRegistrarse.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }
        //cuando de click en el boton de login
        binding.registrarse.setOnClickListener {
            //validar que se ingresen en los campos datos
            if(isValid){
                requestLogin()
            }else{
                Toast.makeText(activity, "Datos Invalidos ", Toast.LENGTH_SHORT).show()
            }
        }

        binding.emailTIET.addTextChangedListener{
            if(binding.emailTIET.text.toString().isEmpty()){
                binding.textInputLayout.error = "Campo requerido"
                isValid = false
            }else{
                isValid = true
            }
        }

        binding.passwordTIET.addTextChangedListener {
            if (binding.passwordTIET.text.toString().isEmpty()) {
                binding.textInputLayout2.error = "Campo requerido"
                isValid = false
            } else {
                isValid = true
            }
        }
        binding.textRestablecer.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_restore_password)
        }
    }

    private fun setupObservers(){
        //accedemos a los publisher y con el observer definimos quien es el encargado del ciclo de vida
        viewModel.loaderState.observe(viewLifecycleOwner){ loaderState ->
            communicator.showLoader(loaderState)//llamamos al loader para mostrarlo
        }
        //accedemos a los publisher y con el observer definimos quien es el encargado del ciclo de vida
        viewModel.sessionValid.observe(viewLifecycleOwner) { sessionValid ->
            if (sessionValid) {//si el usuario existe
                //llamamos a la actividad principal
                val intent = Intent(activity, MainActivity::class.java)
                //activamos la  actividad principal
                startActivity(intent)
                activity?.finish() //cerramos la actividad actual
            }else{
                //mandamos mensaje
                Toast.makeText(activity, "Ingreso invalido", Toast.LENGTH_SHORT).show()
            }

        }
    }

private fun requestLogin() {
    viewModel.requestSingnIn(binding.emailTIET.text.toString(),
        binding.passwordTIET.text.toString())
}



override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}