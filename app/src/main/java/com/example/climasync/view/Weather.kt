package com.example.climasync.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.climasync.R
import com.example.climasync.databinding.FragmentWeatherBinding
import com.example.climasync.utils.FragmentCommunicator
import com.example.climasync.viewModel.WeatherViewModel


/**
 * A simple [Fragment] subclass.
 * Use the [weather.newInstance] factory method to
 * create an instance of this fragment.
 */
class Weather : Fragment() {
    // TODO: Rename and change types of parameters

    private var _binding: FragmentWeatherBinding? = null
    private val binding get() = _binding!!
    var isValid:Boolean = false
    private lateinit var communicator: FragmentCommunicator
    private val viewModel by viewModels<WeatherViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_weather, container, false)
        _binding = FragmentWeatherBinding.inflate(inflater, container, false)
        communicator = requireActivity() as MainActivity
        setupObservers()
        return binding.root

    }

    private fun setupView() {//aqui publisher para que muestre mensaje error o exito de carga de pantalla
        //???
    }

    private fun setupObservers(){//observamos el publisher para ver si la API nos manda todos los datos
        viewModel.loaderState.observe(viewLifecycleOwner) { loaderState ->
            communicator.showLoader(loaderState)

        }

        viewModel.msj.observe(viewLifecycleOwner){ msj ->
            if(msj){
                Toast.makeText(activity, "Cargando.", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_personal_information_to_layoutPermission)
            }else{
                Toast.makeText(activity, "Error", Toast.LENGTH_SHORT).show()
            }

        }

    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}