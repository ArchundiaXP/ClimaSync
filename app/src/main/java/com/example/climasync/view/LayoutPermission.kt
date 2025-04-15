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
import com.example.climasync.databinding.FragmentLayoutPermissionBinding
import com.example.climasync.utils.FragmentCommunicator
import com.example.climasync.viewModel.LayoutPermisionViewModel
import com.example.climasync.viewModel.PersonalInformationViewModel
import com.google.firebase.auth.FirebaseAuth

class LayoutPermission : Fragment() {


    private var _biding: FragmentLayoutPermissionBinding? = null
    private val binding get() = _biding!!
    var isValid: Boolean = false
    private lateinit var communicator: FragmentCommunicator
    private val viewModel by viewModels<LayoutPermisionViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setupObservers()
        return inflater.inflate(R.layout.fragment_layout_permission, container, false)
    }

    companion object {

        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LayoutPermission().apply {
                arguments = Bundle().apply {
                }
            }
    }

    private fun setupObservers() {
        viewModel.msj.observe(viewLifecycleOwner) { msj ->
            if (msj) {
                Toast.makeText(activity, "Datos guardados", Toast.LENGTH_SHORT).show()
                //findNavController().navigate(R.id.action_FirstFragment_to_restore_password)
            } else {
                Toast.makeText(activity, "Error", Toast.LENGTH_SHORT).show()
            }
        }
    }
}