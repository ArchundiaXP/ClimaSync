package com.example.climasync

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.climasync.databinding.FragmentRestorePasswordBinding
import com.example.climasync.utils.FragmentCommunicator


/**
 * A simple [Fragment] subclass.
 * Use the [restore_password.newInstance] factory method to
 * create an instance of this fragment.
 */
class restore_password : Fragment() {
    // TODO: Rename and change types of parameters

    private var _binding:FragmentRestorePasswordBinding? = null
    private val binding get()=_binding!!
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_restore_password, container, false)
    }

    private fun setupView(){
        binding.flechaRestorePassword.setOnClickListener {
            findNavController().navigate(R.id.action_restore_password_to_FirstFragment)
        }
        binding.resertButton.setOnClickListener {
            findNavController().navigate(R.id.action_restore_password_to_FirstFragment)
        }
    }

}