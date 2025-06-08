package com.example.climasync.view.onboarding

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.climasync.R
import com.example.climasync.databinding.FragmentLoginBinding
import com.example.climasync.utils.FragmentCommunicator
import com.example.climasync.view.HomeActivity
import com.example.climasync.viewModel.LoginViewModel

/**
 * A simple [androidx.fragment.app.Fragment] subclass as the default destination in the navigation.
 */
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null//hacemos referencia al fragmento
    private val binding get() = _binding!!
    private val viewModel by viewModels<LoginViewModel>() //enlazamos el viewModel
    var isValid:Boolean = false
    private lateinit var communicator: FragmentCommunicator



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

      _binding = FragmentLoginBinding.inflate(inflater, container, false)
        communicator = requireActivity() as Onboarding //inicializamos punto de entrada al contrato
        setupView()
        setupObservers()
      return binding.root

    }

    private fun setupView(){
        binding.textRegistrarse.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
        //cuando de click en el boton de login
        binding.btnLogin.setOnClickListener {
            //validar que se ingresen en los campos datos
            if(isValid){
                requestLogin()
            }else{
                Toast.makeText(activity, "Datos Invalidos ", Toast.LENGTH_SHORT).show()
            }
        }

        binding.emailTIET.addTextChangedListener {
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
            findNavController().navigate(R.id.action_loginFragment_to_restore_password)
        }
    }

    private fun setupObservers() {
        // Observa el estado del loader
        viewModel.loaderState.observe(viewLifecycleOwner) { loaderState ->
            communicator.showLoader(loaderState)
        }

        // Observa si la sesión fue válida
        viewModel.sessionValid.observe(viewLifecycleOwner) { sessionValid ->
            if (sessionValid) {
                // Usuario autenticado con éxito
                val intent = Intent(activity, HomeActivity::class.java)
                startActivity(intent)
                activity?.finish()
            }
            // Ya no se necesita else aquí porque los errores se mostrarán con errorMessage
        }

        // Observa errores y los muestra como Toast
        viewModel.errorMessage.observe(viewLifecycleOwner) { message ->
            Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
        }
    }


    private fun requestLogin() { //aqui le mandamos a la funcion requestSingnIn del viewModel los datos que ingresaron
        viewModel.requestSingnIn(binding.emailTIET.text.toString(),
            binding.passwordTIET.text.toString())
    }

    override fun onDestroyView() {
            super.onDestroyView()
            _binding = null
    }
}